package geldt;

import com.taxonic.carml.engine.RmlMapper;
import com.taxonic.carml.model.TriplesMap;
import com.taxonic.carml.util.RmlMappingLoader;
import com.taxonic.carml.vocab.Rdf;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Set;

public class MainIS {


    public static void main(String[] args) {

        String header = "GLOBALEVENTID    SQLDATE    MonthYear    Year    FractionDate    Actor1Code    Actor1Name    Actor1CountryCode    Actor1KnownGroupCode    Actor1EthnicCode    Actor1Religion1Code    Actor1Religion2Code    Actor1Type1Code    Actor1Type2Code    Actor1Type3Code    Actor2Code    Actor2Name    Actor2CountryCode    Actor2KnownGroupCode    Actor2EthnicCode    Actor2Religion1Code    Actor2Religion2Code    Actor2Type1Code    Actor2Type2Code    Actor2Type3Code    IsRootEvent    EventCode    EventBaseCode    EventRootCode    QuadClass    GoldsteinScale    NumMentions    NumSources    NumArticles    AvgTone    Actor1Geo_Type    Actor1Geo_FullName    Actor1Geo_CountryCode    Actor1Geo_ADM1Code    Actor1Geo_ADM2Code    Actor1Geo_Lat    Actor1Geo_Long    Actor1Geo_FeatureID    Actor2Geo_Type    Actor2Geo_FullName    Actor2Geo_CountryCode    Actor2Geo_ADM1Code    Actor2Geo_ADM2Code    Actor2Geo_Lat    Actor2Geo_Long    Actor2Geo_FeatureID    ActionGeo_Type    ActionGeo_FullName    ActionGeo_CountryCode     ActionGeo_ADM1Code    ActionGeo_ADM2Code    ActionGeo_Lat    ActionGeo_Long    ActionGeo_FeatureID    DATEADDED    SOURCEURL";
        InputStream zis = MainIS.class.getResourceAsStream("/20190312143000.export.CSV");

        Set<TriplesMap> mapping =
                RmlMappingLoader
                        .build()
                        .load(RDFFormat.TURTLE,
                                Paths.get("/Users/riccardo/_Projects/web/geldt/src/main/resources/geldt_export.ttl"));

        RmlMapper mapper =
                RmlMapper
                        .newBuilder()
                        // Add the resolvers to suit your need
                        .setLogicalSourceResolver(Rdf.Ql.Csv, new MyCsvResolver(header.split("\\s+")))
                        // set file directory for sources in mapping.ttl
                        // set classpath basepath for sources in mapping.ttl
                        .build();

        mapper.bindInputStream("GELDTStream", zis);

        Model map = mapper.map(mapping);

        map.forEach(statement -> System.out.println(statement.getSubject() + " - " + statement.getPredicate() + " - " + statement.getObject()));

    }
}
