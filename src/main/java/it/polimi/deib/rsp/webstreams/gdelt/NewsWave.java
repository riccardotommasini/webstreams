package it.polimi.deib.rsp.webstreams.gdelt;

import it.polimi.deib.rsp.webstreams.gdelt.functions.*;
import lombok.extern.log4j.Log4j;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import spark.Response;
import spark.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/*
    This class starts a WebSocket from which the streams for GELDT can be retrieved.
    First, the SPARK endpoint is created; then, the data downloaded and the three streams (queries, mentions and gkg)
    are bounded to the webSockets.
    The webSockets will offer those stream in RDF.
*/

@Log4j
public class NewsWave {

    private static String source_url;

    private static String stream_header;
    private static String stream_mapping_path;
    private static String stream_sgraph_path;

    private static GELDTWebSocketHandler webSocketHandler;

    private static String stream_name;

    private static int sgraph_port;
    private static final int sgraph_thread = 10;
    private static int stream_port;
    private static final int stream_thread = 20;

    private static final String get_method = "GET";
    public static final String GELDT_stream_name = "GELDTStream";
    public static final String prefix = "http://geldt.org/gkg/";
    public static final String semicolon = ";";

    private static Service endPointService;
    private static Service webSocketService;

    private static Model geldt;
    private static ValueFactory factory = SimpleValueFactory.getInstance();

    private static IRI sGDELTStreamCSVEndpoint;
    private static IRI s;
    private static IRI p = factory.createIRI(" http://www.w3.org/TR/vocab-dcat/accessURL");

    private static int polling_delay = 300000;


    public static void startGeldt(int sgraphport, int streamport, String geldtlastUpdateurl, String streamName, String header, String mappingPath, String sgraphPath) {

        sgraph_port = sgraphport;
        stream_port = streamport;
        source_url = geldtlastUpdateurl;
        stream_name = streamName;
        stream_header = header;
        stream_mapping_path = mappingPath;
        stream_sgraph_path = sgraphPath;

        sGDELTStreamCSVEndpoint = factory.createIRI("https://www.gdelt.org/stream#GDELT" + stream_name + "CSVStreamEndpoint");

        Object[] functions = new Object[]{new DBPediaPeopleLookup(),
                new DBPediaPeopleLookup("http://xmlns.com/foaf/0.1/Person,Wikidata:Q5,Wikidata:Q24229398,Wikidata:Q215627,DUL:NaturalPerson,DUL:Agent,Schema:Person,DBpedia:Person,DBpedia:Agent".split(",")),
                new URISplitFunction(semicolon),
                new RegexSplitterFunction("(.*)<PAGE_PRECISEPUBTIMESTAMP>([0-9]+)</PAGE_PRECISEPUBTIMESTAMP>(.*)", 2),
                new GenericSplitFunction(semicolon, prefix),
                new ThemeV2SplitFunction(semicolon, prefix)};

        try {
            ignite(functions);
            retrieveDataAndSetStreams();
        } catch (IOException e) {
        }
    }

    private static void ignite(Object[] functions) throws IOException {

        // TODO: are two services really needed?

        endPointService = Service.ignite().port(sgraph_port).threadPool(sgraph_thread);
        webSocketService = Service.ignite().port(stream_port).threadPool(stream_thread);

        geldt = Rio.parse(NewsWave.class.getResourceAsStream("/streams/sgraphs/geldt.ttl"), "", RDFFormat.TURTLE);

        //TODO Stream descriptor for three raw streams
        endPointService.get(File.separator + "geldt", (req, res) ->
                toJsonLD(geldt, res));

//        //TODO stream descriptor for three rdf streams
        endPointService.get(File.separator + "geldt" + File.separator + "rsp", (req, res) -> toJsonLD(Rio.parse(NewsWave.class.getResourceAsStream("/streams/sgraphs/rsp.ttl"), "", RDFFormat.TURTLE), res));

//        //TODO detailed description of the event stream

        endPointService.get(File.separator + stream_name, (req, res) -> toJsonLD(Rio.parse(NewsWave.class.getResourceAsStream(stream_sgraph_path), "", RDFFormat.TURTLE), res));
//
        webSocketService.webSocket(File.separator + stream_name, webSocketHandler = new GELDTWebSocketHandler(stream_header, stream_mapping_path, '\t', 2000, functions));

        endPointService.init();
        webSocketService.init();

    }

    private static String toJsonLD(Model export2, Response res) {
        res.type("application/ld+json");
        StringWriter output = new StringWriter();
        Rio.write(export2, output, RDFFormat.JSONLD);
        return output.toString();
    }

    /*
        The txt file downloaded from GELDT server is in the form:

        <numbers> <string> http://data.gdeltproject.org/gdeltv2/<timestamp>.events_stream_name.CSV.zip
        <numbers> <string> http://data.gdeltproject.org/gdeltv2/<timestamp>.mentions.CSV.zip
        <numbers> <string> http://data.gdeltproject.org/gdeltv2/<timestamp>.gkg.CSV.zip

        We are interested in the urls because they point to the CSV related to the three streams:
        queries, mentions and gkg.
    */
    private static void retrieveDataAndSetStreams() throws IOException {

        String oldLine = "";
        String downloadDestination = "./";

        while(true) {

            URL downloadUrl = new URL(source_url);
            HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
            connection.setRequestMethod(get_method);

            // Download the txt file from GELDT servers
            BufferedReader br;
            if (200 <= connection.getResponseCode() && connection.getResponseCode() <= 299) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            // TODO: Handle this particular case.
            if (stream_name.equals("rsp/queries")) stream_name = "export";

            String line;
            while ((line = br.readLine()) != null) {
                String todownload_address = line.split(" ")[2];

                if ((todownload_address.contains(stream_name)) && !(todownload_address.equals(oldLine))) {

                    oldLine = todownload_address;
                    IRI o = factory.createIRI(todownload_address);

                    URL todownload_url = new URL(todownload_address);
                    HttpURLConnection connection1 = (HttpURLConnection) todownload_url.openConnection();
                    connection1.setRequestMethod(get_method);

                    ZipInputStream zis = new ZipInputStream(connection1.getInputStream());

                    ZipEntry ze = zis.getNextEntry();

                    log.info(ze.getName());

                    System.out.println("Data for stream found!");
                    s = sGDELTStreamCSVEndpoint;
                    geldt.add(factory.createStatement(s, p, o));
                    ByteArrayOutputStream dos = getByteArrayOutputStream(downloadDestination, zis, ze);

                    if (dos != null) {
                        webSocketHandler.bindInputStream(GELDT_stream_name, new ByteArrayInputStream(dos.toByteArray()));
                    }

                } else if (todownload_address.equals(oldLine)) System.out.println("Already downloaded this.");
            }

            // Check for new content every 10 seconds
            try {
                Thread.sleep(polling_delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private static ByteArrayOutputStream getByteArrayOutputStream(String dest, ZipInputStream zis, ZipEntry ze) throws IOException {

        ByteArrayOutputStream dos = new ByteArrayOutputStream();

        byte[] buffer = new byte[5000];


        while (ze != null) {
            int len;
            File f = new File(dest + ze.getName());
            FileOutputStream fos = new FileOutputStream(f);
            log.info("Saving file at [" + f.getAbsolutePath() + "]");
            while ((len = zis.read(buffer)) > 0) {
                dos.write(buffer, 0, len);
                //save to a file
                fos.write(buffer, 0, len);
            }
            dos.close();
            fos.close();
            //close this ZipEntry
            zis.closeEntry();
            ze = zis.getNextEntry();
        }
        //close last ZipEntry
        zis.closeEntry();
        return dos;
    }
}
