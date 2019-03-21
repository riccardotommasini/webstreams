package it.polimi.deib.rsp.webstreams.wikimedia;

import it.polimi.deib.rsp.webstreams.geldt.AbstractWave;
import lombok.extern.log4j.Log4j;
import spark.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;
import java.io.ByteArrayInputStream;
import java.io.File;

@Log4j
public class WikiWave extends AbstractWave {

    private static final String spec = "http://data.gdeltproject.org/gdeltv2/lastupdate.txt";
    private static final String header_export = "GLOBALEVENTID\tSQLDATE\tMonthYear\tYear\tFractionDate\tActor1Code\tActor1Name\tActor1CountryCode\tActor1KnownGroupCode\tActor1EthnicCode\tActor1Religion1Code\tActor1Religion2Code\tActor1Type1Code\tActor1Type2Code\tActor1Type3Code\tActor2Code\tActor2Name\tActor2CountryCode\tActor2KnownGroupCode\tActor2EthnicCode\tActor2Religion1Code\tActor2Religion2Code\tActor2Type1Code\tActor2Type2Code\tActor2Type3Code\tIsRootEvent\tEventCode\tEventBaseCode\tEventRootCode\tQuadClass\tGoldsteinScale\tNumMentions\tNumSources\tNumArticles\tAvgTone\tActor1Geo_Type\tActor1Geo_FullName\tActor1Geo_CountryCode\tActor1Geo_ADM1Code\tActor1Geo_ADM2Code\tActor1Geo_Lat\tActor1Geo_Long\tActor1Geo_FeatureID\tActor2Geo_Type\tActor2Geo_FullName\tActor2Geo_CountryCode\tActor2Geo_ADM1Code\tActor2Geo_ADM2Code\tActor2Geo_Lat\tActor2Geo_Long\tActor2Geo_FeatureID\tActionGeo_Type\tActionGeo_FullName\tActionGeo_CountryCode\tActionGeo_ADM1Code\tActionGeo_ADM2Code\tActionGeo_Lat\tActionGeo_Long\tActionGeo_FeatureID\tDATEADDED\tSOURCEURL";
    private static final String mapping_export = "recentchanges.ttl";
    private static final String export = "recentchanges";
    private static final String sgraph = "sgraph";

    private static WikimediaWebSocketHandler handler;


    private static final int sgraph_port = 80;
    private static final int sgraph_thread = 10;
    private static final int stream_port = 8080;
    private static final int stream_thread = 20;

    private static final String apimethod = "GET";
    public static final String stream_name = "WikimediaChanges";
    public static final String prefix = "http://wikimedia.streams.org/";
    public static final String semicolon = ";";
    private static Service service1;
    private static Service service2;


    private static Client client = ClientBuilder.newClient();
    private static String wikipedia = "https://stream.wikimedia.org/v2/stream/recentchange";

    public static void main(String[] args) {

        Object[] functions = new Object[]{};

        ignite(functions);

        wikimedia();

    }

    private static void ignite(Object[] functions) {
        service1 = Service.ignite().port(sgraph_port).threadPool(sgraph_thread);
        service2 = Service.ignite().port(stream_port).threadPool(stream_thread);

        service1.get(File.separator + sgraph, (req, res) -> {
            return "";
            //TODO
        });

        service2.webSocket(File.separator + export, handler = new WikimediaWebSocketHandler(mapping_export));

        service1.init();
        service2.init();

    }

    private static void wikimedia() {
        WebTarget target = client.target(wikipedia);
        SseEventSource source = SseEventSource.target(target).build();
        source.register(payload -> handler.bindInputStream(stream_name, new ByteArrayInputStream(payload.readData().getBytes())),   // Consumer<InboundSseEvent>
                Throwable::printStackTrace,         // Consumer<Throwable>
                () -> System.out.println("no more events"));
        source.open();
    }

}


