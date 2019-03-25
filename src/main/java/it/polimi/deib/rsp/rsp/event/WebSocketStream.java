package it.polimi.deib.rsp.rsp.event;

import it.polimi.jasper.streams.RegisteredEPLStream;
import it.polimi.jasper.streams.schema.GraphStreamSchema;
import it.polimi.yasper.core.stream.rdf.RDFStream;
import it.polimi.yasper.core.stream.schema.StreamSchema;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.*;
import java.net.URI;

/**
 * Created by Riccardo on 13/08/16.
 */
@Getter
@Log4j
@WebSocket
public class WebSocketStream extends RDFStream implements Runnable {

    private String igraphpath;
    private Model sgraph;
    private Model igraphlast;

    private RegisteredEPLStream s;
    private int sample;

    private String type;
    private StreamSchema schema = new GraphStreamSchema();

    public WebSocketStream(String uri) {
        super(null);
        this.stream_uri = uri;
    }

    @OnWebSocketConnect
    public void connected(Session session) {
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
    }

    @OnWebSocketMessage
    //TODO this is the method that represents await
    public void message(Session session, String message) throws IOException {
        Model read = ModelFactory.createDefaultModel().read(new BufferedInputStream(new ByteArrayInputStream(message.getBytes())), "http://localhost:8181/jasper", "TTL");
        //read.write(System.out, "JSON-LD");
        System.out.println(stream_uri);
        // this.s.put(read.getGraph(), System.currentTimeMillis());
    }

    @Override
    public StreamSchema getSchema() {
        return schema;
    }


    public void setWritable(RegisteredEPLStream e) {
        this.s = e;
    }


    @Override
    public String toString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        RDFDataMgr.write(baos, sgraph, Lang.TTL);
        return baos.toString();
    }

    @Override
    public void run() {
        WebSocketClient client = new WebSocketClient();
        try {
            client.start();
            URI echoUri = new URI(stream_uri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(this, echoUri, request);
            System.out.printf("Connecting to : %s%n", echoUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
