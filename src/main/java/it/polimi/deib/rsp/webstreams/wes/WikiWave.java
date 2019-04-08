package it.polimi.deib.rsp.webstreams.wes;

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

    private static String mapping_file_path;
    private static final String recentchanges_stream_name = "recentchanges";
    private static final String sgraph = "sgraph";

    private static WikimediaWebSocketHandler handler;

    private static int sgraph_port;
    private static final int sgraph_thread = 10;
    private static int stream_port;
    private static final int stream_thread = 20;

    public static final String stream_name = "WikimediaChanges";
    public static final String prefix = "http://wikimedia.streams.org/";

    private static Service sGRAPHService;
    private static Service webSocketService;


    private static Client client = ClientBuilder.newClient();
    private static String wikipedia_stream_address;

    public static void startWikimedia(int sgraphport, int streamport, String wikipediastreamaddress, String mappingFilePath) {

        sgraph_port = sgraphport;
        stream_port = streamport;
        wikipedia_stream_address = wikipediastreamaddress;
        mapping_file_path = mappingFilePath;

        Object[] functions = new Object[]{};

        ignite(functions);

        retrieveDataAndSetStreams();

    }

    private static void ignite(Object[] functions) {

        /*
         * Steps (5) and (6): WebSockets for RDF streams and access point for
         * SGRAPHS are here set up.
         *
         */

        sGRAPHService = Service.ignite().port(sgraph_port).threadPool(sgraph_thread);
        webSocketService = Service.ignite().port(stream_port).threadPool(stream_thread);

        sGRAPHService.get(File.separator + sgraph, (req, res) -> {
            return "";
            //TODO
        });

        webSocketService.webSocket(File.separator + recentchanges_stream_name, handler = new WikimediaWebSocketHandler(mapping_file_path));

        sGRAPHService.init();
        webSocketService.init();

    }

    private static void retrieveDataAndSetStreams() {

        /*
         * Step (1): resources have been chosen for this stream.
         * In this case, a stream is already published on the web.
         * We have to gather this and convert it into RDF, which will be pushed.
         *
         */

        WebTarget target = client.target(wikipedia_stream_address);
        SseEventSource source = SseEventSource.target(target).build();
        source.register(payload -> handler.bindInputStream(stream_name, new ByteArrayInputStream(payload.readData().getBytes())),   // Consumer<InboundSseEvent>
                Throwable::printStackTrace,         // Consumer<Throwable>
                () -> System.out.println("INFO: No more queries"));
        source.open();
    }

}


