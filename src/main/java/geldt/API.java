package geldt;

import com.opencsv.CSVReader;
import it.polimi.deib.rsp.geldt.GELDTArticleExample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipInputStream;

import static geldt.UnZip.readzip;

public class API {

    public static void main(String[] args) throws IOException {
        URL dest = GELDTArticleExample.class.getResource("/geldt/csv/");

        URL url = new URL("http://data.gdeltproject.org/gdeltv2/lastupdate.txt");
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
            System.out.println(todownload);

            String header;

            if (todownload.contains("export")) {
                header = "GLOBALEVENTID    SQLDATE    MonthYear    Year    FractionDate    Actor1Code    Actor1Name    Actor1CountryCode    Actor1KnownGroupCode    Actor1EthnicCode    Actor1Religion1Code    Actor1Religion2Code    Actor1Type1Code    Actor1Type2Code    Actor1Type3Code    Actor2Code    Actor2Name    Actor2CountryCode    Actor2KnownGroupCode    Actor2EthnicCode    Actor2Religion1Code    Actor2Religion2Code    Actor2Type1Code    Actor2Type2Code    Actor2Type3Code    IsRootEvent    EventCode    EventBaseCode    EventRootCode    QuadClass    GoldsteinScale    NumMentions    NumSources    NumArticles    AvgTone    Actor1Geo_Type    Actor1Geo_FullName    Actor1Geo_CountryCode    Actor1Geo_ADM1Code    Actor1Geo_ADM2Code    Actor1Geo_Lat    Actor1Geo_Long    Actor1Geo_FeatureID    Actor2Geo_Type    Actor2Geo_FullName    Actor2Geo_CountryCode    Actor2Geo_ADM1Code    Actor2Geo_ADM2Code    Actor2Geo_Lat    Actor2Geo_Long    Actor2Geo_FeatureID    ActionGeo_Type    ActionGeo_FullName    ActionGeo_CountryCode     ActionGeo_ADM1Code    ActionGeo_ADM2Code    ActionGeo_Lat    ActionGeo_Long    ActionGeo_FeatureID    DATEADDED    SOURCEURL";
            } else if (todownload.contains("mentions")) {
                header = "GLOBALEVENTID    EventTimeDate    MentionTimeDate    MentionType    MentionSourceName    MentionIdentifier    SentenceID    Actor1CharOffset    Actor2CharOffset    ActionCharOffset    InRawText    Confidence    MentionDocLen    MentionDocTone    MentionDocTranslationInfo    Extras";
            } else if (todownload.contains("gkg")) {

            }

            URL todownload_url = new URL(todownload);
            HttpURLConnection connection1 = (HttpURLConnection) todownload_url.openConnection();
            connection1.setRequestMethod("GET");

//            ZipInputStream zis = new ZipInputStream(connection1.getInputStream());
//            InputStreamReader isr = new InputStreamReader(zis);
//            BufferedReader br1 = new BufferedReader(isr);
//            CSVReader reader = new CSVReader(br1, ' ');
//
//
//            String[] line2;
//            while ((line2 = reader.readNext()) != null) {
//                System.out.println(line2[0]);
//            }

            readzip(dest.getPath(), connection1.getInputStream(), new byte[1024]);
        }

    }
}
