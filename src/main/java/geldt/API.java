package geldt;

import com.taxonic.carml.engine.RmlMapper;
import com.taxonic.carml.model.TriplesMap;
import com.taxonic.carml.util.RmlMappingLoader;
import com.taxonic.carml.vocab.Rdf;
import it.polimi.deib.rsp.geldt.GELDTArticleExample;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Set;
import java.util.zip.ZipEntry;
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

            String header = "";
            String mappingfile = "";

            if (todownload.contains("export")) {
                header = "GLOBALEVENTID    SQLDATE    MonthYear    Year    FractionDate    Actor1Code    Actor1Name    Actor1CountryCode    Actor1KnownGroupCode    Actor1EthnicCode    Actor1Religion1Code    Actor1Religion2Code    Actor1Type1Code    Actor1Type2Code    Actor1Type3Code    Actor2Code    Actor2Name    Actor2CountryCode    Actor2KnownGroupCode    Actor2EthnicCode    Actor2Religion1Code    Actor2Religion2Code    Actor2Type1Code    Actor2Type2Code    Actor2Type3Code    IsRootEvent    EventCode    EventBaseCode    EventRootCode    QuadClass    GoldsteinScale    NumMentions    NumSources    NumArticles    AvgTone    Actor1Geo_Type    Actor1Geo_FullName    Actor1Geo_CountryCode    Actor1Geo_ADM1Code    Actor1Geo_ADM2Code    Actor1Geo_Lat    Actor1Geo_Long    Actor1Geo_FeatureID    Actor2Geo_Type    Actor2Geo_FullName    Actor2Geo_CountryCode    Actor2Geo_ADM1Code    Actor2Geo_ADM2Code    Actor2Geo_Lat    Actor2Geo_Long    Actor2Geo_FeatureID    ActionGeo_Type    ActionGeo_FullName    ActionGeo_CountryCode     ActionGeo_ADM1Code    ActionGeo_ADM2Code    ActionGeo_Lat    ActionGeo_Long    ActionGeo_FeatureID    DATEADDED    SOURCEURL";
                mappingfile = "export";
            } else if (todownload.contains("mentions")) {
                header = "GLOBALEVENTID    EventTimeDate    MentionTimeDate    MentionType    MentionSourceName    MentionIdentifier    SentenceID    Actor1CharOffset    Actor2CharOffset    ActionCharOffset    InRawText    Confidence    MentionDocLen    MentionDocTone    MentionDocTranslationInfo    Extras";
                mappingfile = "mentions";
                continue;
            } else if (todownload.contains("gkg")) {
                continue;
            }

            URL todownload_url = new URL(todownload);
            HttpURLConnection connection1 = (HttpURLConnection) todownload_url.openConnection();
            connection1.setRequestMethod("GET");

            ZipInputStream zis = new ZipInputStream(connection1.getInputStream());
            RmlMapper mapper =
                    RmlMapper
                            .newBuilder()
                            .setLogicalSourceResolver(Rdf.Ql.Csv, new MyCsvResolver(header.split("\\s+")))
                            .build();

            Set<TriplesMap> mapping =
                    RmlMappingLoader
                            .build()
                            .load(RDFFormat.TURTLE,
                                    Paths.get("/Users/riccardo/_Projects/web/geldt/src/main/resources/geldt_" + mappingfile + ".ttl"));


            ByteArrayOutputStream dos = new ByteArrayOutputStream();


            byte[] buffer = new byte[1024];

            ZipEntry ze = zis.getNextEntry();
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

            mapper.bindInputStream("GELDTStream", new ByteArrayInputStream(dos.toByteArray()));

            Model map = mapper.map(mapping);

            FileOutputStream fos = new FileOutputStream("/Users/riccardo/_Projects/web/geldt/src/main/resources/export.ttl");
            Rio.write(map, fos, RDFFormat.TURTLE);
            FileOutputStream fos1 = new FileOutputStream("/Users/riccardo/_Projects/web/geldt/src/main/resources/export.trig");

            Rio.write(map, fos1, RDFFormat.TRIG);

            readzip(dest.getPath(), connection1.getInputStream(), new byte[1024]);
        }

    }
}
