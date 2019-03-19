package it.polimi.deib.rsp.webstreams.geldt.functions;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.taxonic.carml.engine.function.FnoFunction;
import com.taxonic.carml.engine.function.FnoParam;
import net.minidev.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DBPediaPeopleLookup {

    Gson gson = new Gson();
    public String dbpedia = "https://api.dbpedia-spotlight.org/en/annotate?";
    private String types = "Http%3A%2F%2Fxmlns.com%2Ffoaf%2F0.1%2FPerson";

    @FnoFunction("http://example.org/lookup")
    public List<String> lookup(@FnoParam("http://example.org/toLookup") String peeps) throws IOException {

        long l = System.currentTimeMillis();
        if (peeps != null) {
            String x = URLEncoder.encode(peeps);

            URL url = new URL(dbpedia + "text=" + x + "&types=" + types);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("accept", "application/json");
            BufferedReader br;
            if (200 <= connection.getResponseCode() && connection.getResponseCode() <= 299) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    JSONObject jsonElement = gson.fromJson(line, JSONObject.class);
                    ArrayList<LinkedTreeMap> resources = (ArrayList<LinkedTreeMap>) jsonElement.get("Resources");
                    if (resources != null) {
                        System.out.println(System.currentTimeMillis()- l);
                        return resources
                                .stream().map(jsonObject -> jsonObject.get("@URI"))
                                .map(Object::toString).collect(Collectors.toList());

                    }
                }
            }
        }
        System.out.println(System.currentTimeMillis()- l);
        return Collections.emptyList();
    }

}
