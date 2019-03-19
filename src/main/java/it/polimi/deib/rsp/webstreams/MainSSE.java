package it.polimi.deib.rsp.webstreams;


import com.google.common.collect.Maps;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Collectors;

public class MainSSE {
    public static void main(String[] args) throws InterruptedException, URISyntaxException {

        Client client = ClientBuilder.newClient();
        String wikipedia = "https://stream.wikimedia.org/v2/stream/recentchange";
        String token = "MzI5NGVjNWYtNzVlOC00OWE5LWJjOWUtNGQzYjI5NmI1OTg0";
        String api ="http://stockmarket.streamdata.io/v2/prices";
        Map<String,String> headers = Maps.newHashMap();

        WebTarget target = client.target(buildStreamdataUrl(api,token,headers));

        while (true) {
            SseEventSource source = SseEventSource.target(target).build();
            source.register(inboundSseEvent -> System.out.println(inboundSseEvent));
            source.open();
        }

    }

    private static String buildStreamdataUrl(String anApiUrl, String aToken, Map<String, String> aHeaders) throws URISyntaxException {
        URI uri = new  URI(anApiUrl);

        String aqueryParamSeparator = (uri.getQuery() == null || uri.getQuery().isEmpty()) ? "?" : "&";

        // proxify and add auth token to API url
        String url = String.format("https://streamdata.motwin.net/%s%sX-Sd-Token=%s", anApiUrl, aqueryParamSeparator, aToken);

        // add headers if any to API url
        String additionalHeaders = aHeaders.keySet().stream().map(key -> "X-Sd-Header" + "=" + key + ":" + aHeaders.get(key)).collect(Collectors.joining("&"));

        return url + ((!additionalHeaders.isEmpty()) ? "&" : "") + additionalHeaders;
    }
}

