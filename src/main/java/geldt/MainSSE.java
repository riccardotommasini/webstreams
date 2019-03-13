package geldt;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;

public class MainSSE {
    public static void main(String[] args) throws InterruptedException {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://stream.wikimedia.org/v2/stream/recentchange");

        while (true) {
            SseEventSource source = SseEventSource.target(target).build();
            source.register(inboundSseEvent -> System.out.println(inboundSseEvent.readData()));
            source.open();
            Thread.sleep(   500);
        }
    }
}
