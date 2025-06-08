package aleexeyy.com.icq.client.backend;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class WebSocketManager {
    private static ClientWebSocket client;

    public static synchronized void init (URI uri) throws InterruptedException {
        if (client == null) {
            client = new ClientWebSocket(uri);
            client.connectBlocking(5, TimeUnit.SECONDS);
        }
    }

    public static ClientWebSocket getClient() {
        if (client == null) {
            throw new IllegalStateException("WebSocketManager.init() not called");
        }
        return client;
    }
}
