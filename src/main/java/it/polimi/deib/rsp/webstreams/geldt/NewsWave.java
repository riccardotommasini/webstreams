package it.polimi.deib.rsp.webstreams.geldt;

import it.polimi.deib.rsp.webstreams.geldt.functions.*;
import lombok.extern.log4j.Log4j;
import org.apache.jena.base.Sys;
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
    First, the SPARK endpoint is created; then, the data downloaded and the three streams (events, mentions and gkg)
    are bounded to the webSockets.
    The webSockets will offer those stream in RDF.
*/

//TODO: set it to download the data in loop, for example every 15 minutes.

@Log4j
public class NewsWave {

    private static String geldt_lastUpdate_url = "http://data.gdeltproject.org/gdeltv2/lastupdate.txt";

    private static final String header_export = "GLOBALEVENTID\tSQLDATE\tMonthYear\tYear\tFractionDate\tActor1Code\tActor1Name\tActor1CountryCode\tActor1KnownGroupCode\tActor1EthnicCode\tActor1Religion1Code\tActor1Religion2Code\tActor1Type1Code\tActor1Type2Code\tActor1Type3Code\tActor2Code\tActor2Name\tActor2CountryCode\tActor2KnownGroupCode\tActor2EthnicCode\tActor2Religion1Code\tActor2Religion2Code\tActor2Type1Code\tActor2Type2Code\tActor2Type3Code\tIsRootEvent\tEventCode\tEventBaseCode\tEventRootCode\tQuadClass\tGoldsteinScale\tNumMentions\tNumSources\tNumArticles\tAvgTone\tActor1Geo_Type\tActor1Geo_FullName\tActor1Geo_CountryCode\tActor1Geo_ADM1Code\tActor1Geo_ADM2Code\tActor1Geo_Lat\tActor1Geo_Long\tActor1Geo_FeatureID\tActor2Geo_Type\tActor2Geo_FullName\tActor2Geo_CountryCode\tActor2Geo_ADM1Code\tActor2Geo_ADM2Code\tActor2Geo_Lat\tActor2Geo_Long\tActor2Geo_FeatureID\tActionGeo_Type\tActionGeo_FullName\tActionGeo_CountryCode\tActionGeo_ADM1Code\tActionGeo_ADM2Code\tActionGeo_Lat\tActionGeo_Long\tActionGeo_FeatureID\tDATEADDED\tSOURCEURL";
    private static final String mapping_export = "events.ttl";

    private static final String header_mentions = "GLOBALEVENTID\tEventTimeDate\tMentionTimeDate\tMentionType\tMentionSourceName\tMentionIdentifier\tSentenceID\tActor1CharOffset\tActor2CharOffset\tActionCharOffset\tInRawText\tConfidence\tMentionDocLen\tMentionDocTone\tMentionDocTranslationInfo\tExtras";
    private static final String mapping_mentions = "mentions.ttl";

    private static final String header_gkg = "GKGRECORDID\tDATE\tSourceCollectionIdentifier\tSourceCommonName\tDocumentIdentifier\tCounts\tV2Counts\tThemes\tV2Themes\tLocations\tV2Locations\tPersons\tV2Persons\tOrganizations\tV2Organizations\tV2Tone\tDates\tGCAM\tSharingImage\tRelatedImages\tSocialImageEmbeds\tSocialVideoEmbeds\tQuotations\tAllNames\tAmounts\tTranslationInfo\tExtras";
    private static final String mapping_gkg = "gkg.ttl";

    private static GELDTWebSocketHandler webSocketHandler;

    // Names of all the streams
    private static final String gkg_stream_name = "gkg";
    private static final String mentions_stream_name = "mentions";
    private static final String events_stream_name = "events";

    // GELDT "export" CSV will be offered as "events" stream
    private static final String export_name = "export";

    private static int sgraph_port;
    private static final int sgraph_thread = 10;
    private static int stream_port;
    private static final int stream_thread = 20;

    private static final String get_method = "GET";
    public static final String stream_name = "GELDTStream";
    public static final String prefix = "http://geldt.org/gkg/";
    public static final String semicolon = ";";

    private static GELDTWebSocketHandler mentions;
    private static GELDTWebSocketHandler events;
    private static GELDTWebSocketHandler gkg;

    private static Service endPointService;
    private static Service webSocketService;

    private static Model geldt;
    private static ValueFactory factory = SimpleValueFactory.getInstance();

    private static IRI sGDELTEventCSVStreamEndpoint = factory.createIRI("https://www.geldt.org/stream#GDELTEventCSVStreamEndpoint");
    private static IRI sGDELTGKGCSVStreamEndpoint = factory.createIRI("https://www.geldt.org/stream#GDELTGKGCSVStreamEndpoint");
    private static IRI sGDELTMentionCSVStreamEndpoint = factory.createIRI("https://www.geldt.org/stream#GDELTMentionCSVStreamEndpoint");
    private static IRI s;
    private static IRI p = factory.createIRI(" http://www.w3.org/TR/vocab-dcat/accessURL");


    public static void startGeldt(int sgraphport, int streamport, String geldtlastUpdateurl) {

        sgraph_port = sgraphport;
        stream_port = streamport;
        geldt_lastUpdate_url = geldtlastUpdateurl;

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

//
//        //TODO detailed description of the event stream
        endPointService.get(File.separator + events_stream_name, (req, res) -> toJsonLD(Rio.parse(NewsWave.class.getResourceAsStream("/streams/sgraphs//" + events_stream_name + ".ttl"), "", RDFFormat.TURTLE), res));
//
//        //TODO detailed description of the mention stream
        endPointService.get(File.separator + mentions_stream_name, (req, res) -> toJsonLD(Rio.parse(NewsWave.class.getResourceAsStream("/streams/sgraphs//" + mentions_stream_name + ".ttl"), "", RDFFormat.TURTLE), res));
//
//        //TODO detailed description of the gkg stream
        endPointService.get(File.separator + gkg_stream_name, (req, res) -> toJsonLD(Rio.parse(NewsWave.class.getResourceAsStream("/streams/sgraphs//" + gkg_stream_name + ".ttl"), "", RDFFormat.TURTLE), res));
//
        webSocketService.webSocket(File.separator + events_stream_name, events = new GELDTWebSocketHandler(header_export, mapping_export, '\t', 1000));

        webSocketService.webSocket(File.separator + mentions_stream_name, mentions = new GELDTWebSocketHandler(header_mentions, mapping_mentions, '\t', 2000));

        webSocketService.webSocket(File.separator + gkg_stream_name, gkg = new GELDTWebSocketHandler(header_gkg, mapping_gkg, '\t', 2000, functions));

        endPointService.init();
        webSocketService.init();

    }

    private static String toJsonLD(Model export2, Response res) {
        res.type("application/ld+json");
        StringWriter output = new StringWriter();
        Rio.write(export2, output, RDFFormat.JSONLD);
        return output.toString();
    }

    private static void retrieveDataAndSetStreams() throws IOException {
        //TODO: Try to force it to download CSV in CSV folder: easy when run in IntelliJ, but try it
        // while running the jar...
        String downloadDestination = "./";

        URL downloadUrl = new URL(geldt_lastUpdate_url);
        HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
        connection.setRequestMethod(get_method);

        // Download the txt file from GELDT servers
        BufferedReader br;
        if (200 <= connection.getResponseCode() && connection.getResponseCode() <= 299) {
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }

        /*
        The txt file downloaded from GELDT server is in the form:

        <numbers> <string> http://data.gdeltproject.org/gdeltv2/<timestamp>.events_stream_name.CSV.zip
        <numbers> <string> http://data.gdeltproject.org/gdeltv2/<timestamp>.mentions.CSV.zip
        <numbers> <string> http://data.gdeltproject.org/gdeltv2/<timestamp>.gkg.CSV.zip

        We are interested in the urls because they point to the CSV related to the three streams:
        events, mentions and gkg.
        */

        String line;
        while ((line = br.readLine()) != null) {
            String todownload_address = line.split(" ")[2];

            IRI o = factory.createIRI(todownload_address);

            URL todownload_url = new URL(todownload_address);
            HttpURLConnection connection1 = (HttpURLConnection) todownload_url.openConnection();
            connection1.setRequestMethod(get_method);

            ZipInputStream zis = new ZipInputStream(connection1.getInputStream());

            ZipEntry ze = zis.getNextEntry();

            log.info(ze.getName());

            if (ze.getName().contains(export_name)) {
                webSocketHandler = events;
                s = sGDELTEventCSVStreamEndpoint;

            } else if (ze.getName().contains(mentions_stream_name)) {
                webSocketHandler = mentions;
                s = sGDELTMentionCSVStreamEndpoint;

            } else if (ze.getName().contains(gkg_stream_name)) {
                webSocketHandler = gkg;
                s = sGDELTGKGCSVStreamEndpoint;
            }

            // The statement that shows which stream has been created and his access point is added
            geldt.add(factory.createStatement(s, p, o));

            // The stream is created and bound to the WebSocket
            ByteArrayOutputStream dos = getByteArrayOutputStream(downloadDestination, zis, ze);

            if (dos != null)
                webSocketHandler.bindInputStream(stream_name, new ByteArrayInputStream(dos.toByteArray()));
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
