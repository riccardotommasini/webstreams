package it.polimi.deib.rsp.webstreams.wikimedia;

import lombok.extern.log4j.Log4j;
import spark.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;
import java.io.ByteArrayInputStream;
import java.io.File;

/*
    This class starts a WebSocket from which the stream for Wikipedia last changes can be retrieved.
    First, the SPARK endpoint is created; then, the Wikimedia's stream is bound to the webService.
    The webSocket will offer the stream in RDF.
*/

@Log4j
public class WikiWave {

    private static final String mapping_export = "recentchanges.ttl";
    private static final String recentchanges_stream_name = "recentchanges";
    private static final String sgraph = "sgraph";

    private static WikimediaWebSocketHandler handler;

    private static final int sgraph_port = 8081;
    private static final int sgraph_thread = 10;
    private static final int stream_port = 8080;
    private static final int stream_thread = 20;

    public static final String stream_name = "WikimediaChanges";
    public static final String prefix = "http://wikimedia.streams.org/";

    private static Service endPointService;
    private static Service webSocketService;


    private static Client client = ClientBuilder.newClient();
    private static String wikipedia_stream_address = "https://stream.wikimedia.org/v2/stream/recentchange";

    public static void main(String[] args) {

        Object[] functions = new Object[]{};

        ignite(functions);

        retrieveDataAndSetStreams();

    }

    private static void ignite(Object[] functions) {
        endPointService = Service.ignite().port(sgraph_port).threadPool(sgraph_thread);
        webSocketService = Service.ignite().port(stream_port).threadPool(stream_thread);

        endPointService.get(File.separator + sgraph, (req, res) -> {
            return "";
            //TODO
        });

        webSocketService.webSocket(File.separator + recentchanges_stream_name, handler = new WikimediaWebSocketHandler(mapping_export));

        endPointService.init();
        webSocketService.init();

    }

    private static void retrieveDataAndSetStreams() {
        WebTarget target = client.target(wikipedia_stream_address);
        SseEventSource source = SseEventSource.target(target).build();
        source.register(payload -> handler.bindInputStream(stream_name, new ByteArrayInputStream(payload.readData().getBytes())),   // Consumer<InboundSseEvent>
                Throwable::printStackTrace,         // Consumer<Throwable>
                () -> System.out.println("INFO: No more events"));
        source.open();
    }

}


