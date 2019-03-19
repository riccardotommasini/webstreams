package it.polimi.deib.rsp.webstreams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainDBPedia {

    public static void main(String[] args) throws IOException {

        //curl -X GET "https://api.dbpedia-spotlight.org/en/annotate?text=donald%20trump" -H "accept: application/json"

        URL url = new URL("https://api.dbpedia-spotlight.org/en/annotate?text=donald%20trump");


        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.addRequestProperty("accept", "application/json");

        BufferedReader br;
        if (200 <= connection.getResponseCode() && connection.getResponseCode() <= 299) {
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));


            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);


            }
        }
    }


}
