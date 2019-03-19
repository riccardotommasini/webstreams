package geldt.streaming;

import it.polimi.deib.rsp.geldt.GELDTArticleExample;
import spark.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class NewsWave {

    private static final String spec = "http://data.gdeltproject.org/gdeltv2/lastupdate.txt";
    private static final String header_export = "GLOBALEVENTID    SQLDATE    MonthYear    Year    FractionDate    Actor1Code    Actor1Name    Actor1CountryCode    Actor1KnownGroupCode    Actor1EthnicCode    Actor1Religion1Code    Actor1Religion2Code    Actor1Type1Code    Actor1Type2Code    Actor1Type3Code    Actor2Code    Actor2Name    Actor2CountryCode    Actor2KnownGroupCode    Actor2EthnicCode    Actor2Religion1Code    Actor2Religion2Code    Actor2Type1Code    Actor2Type2Code    Actor2Type3Code    IsRootEvent    EventCode    EventBaseCode    EventRootCode    QuadClass    GoldsteinScale    NumMentions    NumSources    NumArticles    AvgTone    Actor1Geo_Type    Actor1Geo_FullName    Actor1Geo_CountryCode    Actor1Geo_ADM1Code    Actor1Geo_ADM2Code    Actor1Geo_Lat    Actor1Geo_Long    Actor1Geo_FeatureID    Actor2Geo_Type    Actor2Geo_FullName    Actor2Geo_CountryCode    Actor2Geo_ADM1Code    Actor2Geo_ADM2Code    Actor2Geo_Lat    Actor2Geo_Long    Actor2Geo_FeatureID    ActionGeo_Type    ActionGeo_FullName    ActionGeo_CountryCode     ActionGeo_ADM1Code    ActionGeo_ADM2Code    ActionGeo_Lat    ActionGeo_Long    ActionGeo_FeatureID    DATEADDED    SOURCEURL";
    private static final String mapping_export = "export.ttl";
    private static final String header_mentions = "GLOBALEVENTID    SQLDATE    MonthYear    Year    FractionDate    Actor1Code    Actor1Name    Actor1CountryCode    Actor1KnownGroupCode    Actor1EthnicCode    Actor1Religion1Code    Actor1Religion2Code    Actor1Type1Code    Actor1Type2Code    Actor1Type3Code    Actor2Code    Actor2Name    Actor2CountryCode    Actor2KnownGroupCode    Actor2EthnicCode    Actor2Religion1Code    Actor2Religion2Code    Actor2Type1Code    Actor2Type2Code    Actor2Type3Code    IsRootEvent    EventCode    EventBaseCode    EventRootCode    QuadClass    GoldsteinScale    NumMentions    NumSources    NumArticles    AvgTone    Actor1Geo_Type    Actor1Geo_FullName    Actor1Geo_CountryCode    Actor1Geo_ADM1Code    Actor1Geo_ADM2Code    Actor1Geo_Lat    Actor1Geo_Long    Actor1Geo_FeatureID    Actor2Geo_Type    Actor2Geo_FullName    Actor2Geo_CountryCode    Actor2Geo_ADM1Code    Actor2Geo_ADM2Code    Actor2Geo_Lat    Actor2Geo_Long    Actor2Geo_FeatureID    ActionGeo_Type    ActionGeo_FullName    ActionGeo_CountryCode     ActionGeo_ADM1Code    ActionGeo_ADM2Code    ActionGeo_Lat    ActionGeo_Long    ActionGeo_FeatureID    DATEADDED    SOURCEURL";
    private static final String mapping_mentions = "mentions.ttl";
    private static final String header_gkg = "GLOBALEVENTID    EventTimeDate    MentionTimeDate    MentionType    MentionSourceName    MentionIdentifier    SentenceID    Actor1CharOffset    Actor2CharOffset    ActionCharOffset    InRawText    Confidence    MentionDocLen    MentionDocTone    MentionDocTranslationInfo    Extras";
    private static final String mapping_gkg = "gkg.ttl";
    private static GELDTWebSocketHandler handler;

    public static void main(String[] args) throws IOException {

        Service service1 = Service.ignite().port(80).threadPool(20);
        Service service2 = Service.ignite().port(8080).threadPool(10);


        service1.get("/sgraph", (req, res) -> {

            return "";

        });

        GELDTWebSocketHandler events;
        service2.webSocket("/events", events = new GELDTWebSocketHandler(header_export, mapping_export));

//        GELDTWebSocketHandler mentions;
//        webSocket("/mentions", mentions = new GELDTWebSocketHandler(header_mentions, mapping_mentions));
//
//        GELDTWebSocketHandler gkg;
//        webSocket("/gkg", gkg = new GELDTWebSocketHandler(header_gkg, mapping_gkg));

        service1.init();
        service2.init();

        URL dest = GELDTArticleExample.class.getResource("/geldt/csv/");

        URL url = new URL(spec);
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
            String todownload = line.split(" ")[2];

            URL todownload_url = new URL(todownload);
            HttpURLConnection connection1 = (HttpURLConnection) todownload_url.openConnection();
            connection1.setRequestMethod("GET");

            ZipInputStream zis = new ZipInputStream(connection1.getInputStream());

            ByteArrayOutputStream dos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];

            ZipEntry ze = zis.getNextEntry();

            System.out.println(ze.getName());

            if(ze.getName().contains("export"))
                handler=events;
            else
                continue;

            while (ze != null) {
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    dos.write(buffer, 0, len);
                }
                dos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();

            handler.bindInputStream("GELDTStream", new ByteArrayInputStream(dos.toByteArray()));


//            FileOutputStream fos = new FileOutputStream("/Users/riccardo/_Projects/web/geldt/src/main/resources/export.ttl");
//            Rio.write(map, fos, RDFFormat.TURTLE);
//            FileOutputStream fos1 = new FileOutputStream("/Users/riccardo/_Projects/web/geldt/src/main/resources/export.trig");
//
//            Rio.write(map, fos1, RDFFormat.TRIG);

//            readzip(dest.getPath(), connection1.getInputStream(), new byte[1024]);
        }

    }
}
