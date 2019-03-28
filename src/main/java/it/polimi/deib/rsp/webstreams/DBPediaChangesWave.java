package it.polimi.deib.rsp.webstreams;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

public class DBPediaChangesWave {

    public static String added = ".added.nt.gz";
    //TODO not immediately useful public static String clear = ".clear.nt.gz";
    public static String reinserted = ".reinserted.nt.gz";
    public static String removed = ".removed.nt.gz";
    static List<String> files = new ArrayList<>();

    static ConcurrentLinkedQueue<Statement> sharedQueue = new ConcurrentLinkedQueue<>();

    public static void startDBPediaChanges(String DBPedialastchangesurl) throws IOException {

        File f = new File("/Users/riccardo/_Projects/web/geldt/src/main/resources/dbpedia.stream");
        FileWriter fr = new FileWriter(f, true);

        URL url = new URL(DBPedialastchangesurl);

        ExecutorService pes = Executors.newFixedThreadPool(10);
        ExecutorService ces = Executors.newFixedThreadPool(1);

        ces.submit(() -> {
            System.out.println("consumer spawned");
            while (true) {
                if (!sharedQueue.isEmpty()) {
                    Statement take = sharedQueue.poll();
                    System.out.println(take.toString());
                    fr.write("<" +
                            take.getSubject().getURI() +
                            "> <" +
                            take.getPredicate().getURI() +
                            "> <" +
                            take.getObject().toString() +
                            "> ." + "\n");
                    fr.flush();
                } else {
                    System.out.println("sleep");
                    Thread.sleep(1000);
                }
            }
        });

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
                    oldline = line;
                    files.clear();
                    String finalLine = line;
                    System.out.println(finalLine);
                    pes.submit(() -> {
                        try {
                            System.out.println("spawned");
                            download(finalLine);
                            System.out.println("terminated");
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            System.out.println("retry");

                            try {
                                download(finalLine);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                        } catch (IOException e) {
                            System.out.println(files.get(0));
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
    }


    private static void download(String finalLine) throws IOException {
        String[] split = finalLine.split("-");

        String reduce = Arrays.stream(split).reduce("", (s, s2) -> s + "/" + s2);

        String s = reduce + reinserted;

        files.add(s);
        URL file = new URL("http://live.dbpedia.org/changesets/" + s);

        HttpURLConnection fc = (HttpURLConnection) file.openConnection();

        fc.setRequestMethod("GET");


        InputStream inputStream = fc.getInputStream();
        GZIPInputStream gzis = new GZIPInputStream(inputStream);
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader in = new BufferedReader(reader);

        Model nt = ModelFactory.createDefaultModel().read(in, "", "NT");

        StmtIterator stmtIterator = nt.listStatements();

        while (stmtIterator.hasNext())
            sharedQueue.offer(stmtIterator.nextStatement());

        System.out.println(sharedQueue.size());
    }
}
