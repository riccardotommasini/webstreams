package it.polimi.deib.rsp.junk;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import net.minidev.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Spotlight {

    public static void main(String[] args) throws IOException {

        //curl -X GET "https://api.dbpedia-spotlight.org/en/annotate?text=donald%20trump" -H "accept: application/json"

        String x = URLEncoder.encode("warren beatty;faye dunaway");
        System.out.println(x);
        URL url = new URL("https://api.dbpedia-spotlight.org/en/annotate?text=" +
                x + "&types=Http%3A%2F%2Fxmlns.com%2Ffoaf%2F0.1%2FPerson");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.addRequestProperty("accept", "application/json");

        BufferedReader br;
        if (200 <= connection.getResponseCode() && connection.getResponseCode() <= 299) {
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                Gson gson = new Gson();

                JSONObject jsonElement = gson.fromJson(line, JSONObject.class);
                System.out.println(jsonElement.getAsString("Resources"));
                List<LinkedTreeMap> arr = (ArrayList<LinkedTreeMap>) jsonElement.get("Resources");

                arr.stream().map(jsonObject -> jsonObject.get("@URI")).collect(Collectors.toList());

            }
        }
        System.out.println(
                connection.getResponseCode());
    }


}
