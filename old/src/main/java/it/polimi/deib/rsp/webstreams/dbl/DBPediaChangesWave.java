package it.polimi.deib.rsp.webstreams.dbl;

import it.polimi.jasper.streams.RegisteredEPLStream;
import it.polimi.yasper.core.stream.rdf.RDFStream;
import org.apache.jena.graph.Graph;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

public class DBPediaChangesWave extends RDFStream implements Runnable {

    public static String added = ".added.nt.gz";
    //TODO not immediately useful public static String clear = ".clear.nt.gz";
    public static String reinserted = ".reinserted.nt.gz";
    public static String removed = ".removed.nt.gz";
    static List<String> files = new ArrayList<>();
    private final String name;

    ExecutorService pes = Executors.newFixedThreadPool(2);
    ExecutorService ces = Executors.newFixedThreadPool(2);

    static ConcurrentLinkedQueue<IGraph> sharedQueue = new ConcurrentLinkedQueue<>();
    private List<RegisteredEPLStream> dtrs = new ArrayList<>();

    public DBPediaChangesWave(String stream_uri) {
        super(stream_uri);
        this.name = stream_uri.replace("http://live.dbpedia.org/changesets/", "");
    }

    public void startDBPediaChanges(String DBPedialastchangesurl) throws IOException, ExecutionException, InterruptedException {

        URL url = new URL(DBPedialastchangesurl);
        long ts = 0;
        String oldline = "";
        while (true) {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader br;
            if (200 <= connection.getResponseCode() && connection.getResponseCode() <= 299) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String line;
            while ((line = br.readLine()) != null) {
                if (oldline.equals(line)) {
                    continue;
                } else {
                    ts += 5000;
                    oldline = line;
                    files.clear();
                    String finalLine = line;
                    long finalTs = ts;
                    pes.submit(() -> download(finalLine, finalTs));
                }
            }
        }
    }


    private void download(String finalLine, long ts) {
        try {
            String[] split = finalLine.split("-");

            String reduce = Arrays.stream(split).reduce("", (s, s2) -> s + "/" + s2);

            String s = reduce + "." + this.name + ".nt.gz";

            // files.add(s);
            String spec = "http://live.dbpedia.org/changesets" + s;
            URL file = new URL(spec);

            System.out.println("[" +
                    System.currentTimeMillis() +
                    "]" + spec);

            Thread.sleep(3000);
            URLConnection urlConnection = file.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedInputStream tosave = new BufferedInputStream(inputStream);
            //        save(finalLine, new GZIPInputStream(tosave));

            Model nt = ModelFactory.createDefaultModel().read(new GZIPInputStream(tosave), "", "NT");

            if(!dtrs.isEmpty()){
                dtrs.forEach(rs -> rs.put(nt.getGraph(), System.currentTimeMillis()));
            }

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {


        }

//        Model nt = ModelFactory.createDefaultModel().read(in, "", "NT");

//        System.out.println(nt.size());
//        StmtIterator stmtIterator = nt.listStatements();
//
//        while (stmtIterator.hasNext())
//            sharedQueue.offer(stmtIterator.nextStatement());

    }

    private static void save(String finalLine, InputStream tosave) throws IOException {
        File targetFile = new File("src/main/resources/" + finalLine + ".nt");
        OutputStream outStream = new FileOutputStream(targetFile);
        byte[] buffer = new byte[10 * 1024];
        int bytesRead;
        while ((bytesRead = tosave.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        tosave.close();
        outStream.close();
    }

    @Override
    public void run() {
        try {
            this.startDBPediaChanges("http://live.dbpedia.org/changesets/lastPublishedFile.txt");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void setWritable(RegisteredEPLStream dtr) {
        this.dtrs.add(dtr);

//        ces.submit(() -> {
//            while (true) {
//                if (!sharedQueue.isEmpty()) {
//                    IGraph take = sharedQueue.poll();
//                    dtr.put(take.g, take.ts);
//                    System.out.println(take.ts);
//                } else {
//                    System.out.println("Consumer goes to sleep [" + sharedQueue.size() + "]");
//                    Thread.sleep(3000);
//                }
//            }
//        });
    }

    private static class IGraph {

        public long ts = System.currentTimeMillis();

        public IGraph(Graph g) {
            this.g = g;
        }

        public Graph g;

    }
}
