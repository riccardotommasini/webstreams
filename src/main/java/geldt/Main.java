package geldt;

import com.taxonic.carml.engine.RmlMapper;
import com.taxonic.carml.logical_source_resolver.CsvResolver;
import com.taxonic.carml.logical_source_resolver.JsonPathResolver;
import com.taxonic.carml.model.TriplesMap;
import com.taxonic.carml.util.RmlMappingLoader;
import com.taxonic.carml.vocab.Rdf;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;

import java.nio.file.Paths;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Set<TriplesMap> mapping =
                RmlMappingLoader
                        .build()
                        .load(RDFFormat.TURTLE,
                                Paths.get("/Users/riccardo/_Projects/web/geldt/src/main/resources/airport.ttl"));

        RmlMapper mapper =
                RmlMapper
                        .newBuilder()
                        // Add the resolvers to suit your need
                        .setLogicalSourceResolver(Rdf.Ql.Csv, new MyCsvResolver(new String[]{"id", "stop", "latitude", "longitude"}))
                        // set file directory for sources in mapping.ttl
                        .fileResolver(Paths.get("/Users/riccardo/_Projects/web/geldt/src/main/resources/airport.csv"))
                        // set classpath basepath for sources in mapping.ttl
                        .build();

        Model result = mapper.map(mapping);

        result.forEach(statement -> System.out.println(statement.getSubject() + " - " + statement.getPredicate() + " - " + statement.getObject()));

    }
}
