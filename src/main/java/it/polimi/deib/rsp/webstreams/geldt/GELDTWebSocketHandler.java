package it.polimi.deib.rsp.webstreams.geldt;

import com.taxonic.carml.engine.RmlMapper;
import com.taxonic.carml.model.TermMap;
import com.taxonic.carml.model.TriplesMap;
import com.taxonic.carml.rdf_mapper.util.ImmutableCollectors;
import com.taxonic.carml.util.RmlMappingLoader;
import com.taxonic.carml.vocab.Rdf;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.BasicWriterSettings;
import org.eclipse.rdf4j.rio.helpers.JSONLDMode;
import org.eclipse.rdf4j.rio.helpers.JSONLDSettings;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebSocket
public class GELDTWebSocketHandler {

    private final RmlMapper mapper;
    private final Set<TriplesMap> mapping;
    private String sender, msg;
    private List<Session> users = new ArrayList<>();

    public GELDTWebSocketHandler(String header, String mappingfile, char delimiter, Object... functions) {
        this.mapper =
                RmlMapper
                        .newBuilder()
                        .setLogicalSourceResolver(Rdf.Ql.Csv, new MyCsvResolver(header.split("\t"), delimiter))
                        .addFunctions(functions)
                        .build();
        String first = "/Users/riccardo/_Projects/web/geldt/src/main/resources/streams/geldt_" + mappingfile;

        System.out.println(first);
        this.mapping =
                RmlMappingLoader
                        .build()
                        .load(RDFFormat.TURTLE,
                                Paths.get(first));

    }

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        this.users.add(user);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        this.users.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
    }

    public void bindInputStream(String geldtStream, ByteArrayInputStream byteArrayInputStream) {
        mapper.bindInputStream(geldtStream, byteArrayInputStream);


        Set functionValueTriplesMaps = mapper.getTermMaps(mapping).filter((t) -> t.getFunctionValue() != null).map(TermMap::getFunctionValue).collect(ImmutableCollectors.toImmutableSet());
        Set<TriplesMap> refObjectTriplesMaps = mapper.getAllTriplesMapsUsedInRefObjectMap(mapping);

        mapping.stream()
                .filter((tm) -> !functionValueTriplesMaps.contains(tm) && !refObjectTriplesMaps.contains(tm))
                .flatMap(tm -> mapper.map(tm, refObjectTriplesMaps).stream())
                .map(this::toJSONLD)
                .forEach(s -> users.stream()
                        .filter(Session::isOpen)
                        .map(Session::getRemote)
                        .forEach(session -> send(s, session)));

        mapper.clearSourceManager();

    }

    private String toJSONLD(Model model) {
        StringWriter stringWriter = new StringWriter();
        RDFWriter rdfWriter = Rio.createWriter(RDFFormat.TURTLE, stringWriter);

        //rdfWriter.getWriterConfig().set(JSONLDSettings.JSONLD_MODE, JSONLDMode.COMPACT);
        //rdfWriter.getWriterConfig().set(JSONLDSettings.OPTIMIZE, true);
        //rdfWriter.getWriterConfig().set(BasicWriterSettings.PRETTY_PRINT, true);

        model.setNamespace("geldt", "http://geldt.org/vocab/");
        model.setNamespace("gkg", "http://geldt.org/gkg/");
        model.setNamespace("geldti", "http://geldt.org/instance/");
        Rio.write(model, rdfWriter);

        return stringWriter.toString();
    }

    private void send(String s, RemoteEndpoint session) {
        try {
            session.sendString(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}