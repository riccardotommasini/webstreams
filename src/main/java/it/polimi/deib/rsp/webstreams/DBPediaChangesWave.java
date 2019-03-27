package it.polimi.deib.rsp.webstreams;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

public class DBPediaChangesWave {

    public static String added = ".added.nt.gz";
    //TODO not immediately useful public static String clear = ".clear.nt.gz";
    public static String reinserted = ".reinserted.nt.gz";
    public static String removed = ".removed.nt.gz";


    public static void startDBPediaChanges(String DBPedialastchangesurl) throws IOException {


        URL url = new URL(DBPedialastchangesurl);
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
            System.out.println(line);
            String[] split = line.split("-");

            String reduce = Arrays.stream(split).reduce("", (s, s2) -> s + "/" + s2);
            System.out.println(reduce);

            String s = reduce + removed;

            System.out.println(s);
            URL file = new URL("http://live.dbpedia.org/changesets/" + s);

            HttpURLConnection fc = (HttpURLConnection) file.openConnection();
            fc.setRequestMethod("GET");


            InputStream inputStream = fc.getInputStream();
            GZIPInputStream gzis = new GZIPInputStream(inputStream);
            InputStreamReader reader = new InputStreamReader(gzis);
            BufferedReader in = new BufferedReader(reader);
            Model nt = ModelFactory.createDefaultModel().read(in, "", "NT");

            nt.write(System.out, "TTL");

        }
    }
}
