package it.polimi.deib.rsp.junk;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class PageCrawler {
    public static void main(String[] args) throws IOException {

        Document doc = Jsoup.connect("https://www.outlookindia.com/website/story/world-news-pakistanis-collect-rs-10-bn-through-crowdfunding-for-dams-project-imran-khan-says-thank-you/326946").get();

        System.out.println(doc.title());

        doc.body().children().stream().filter(element -> !element.tag().getName().contains("script"))
                .flatMap(x -> x.children().stream())
                .flatMap(x -> x.children().stream())
                .flatMap(x -> x.children().stream())
                .map(Element::text)
                .filter(s -> s.length() > 1000)
                .map(StringEscapeUtils::escapeHtml4)
                .forEach(x ->
                {


                    System.out.println(x);


                    URL url = null;
                    try {
                        url = new URL("https://api.dbpedia-spotlight.org/en/annotate?text=" + x);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    HttpURLConnection connection = null;
                    try {
                        connection = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        connection.setRequestMethod("GET");
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                    connection.addRequestProperty("accept", "application/json");

                    BufferedReader br;
                    try {
                        if (200 <= connection.getResponseCode() && connection.getResponseCode() <= 299) {
                            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));


                            String line;
                            while ((line = br.readLine()) != null) {
                                System.out.println(line);


                            }
                        }
                        System.out.println(connection.getResponseCode());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

    }
}
