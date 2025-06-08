package aleexeyy.com.icq.server;

import aleexeyy.com.icq.server.message.handlers.*;
import aleexeyy.com.icq.shared.messages.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ServerApp extends WebSocketServer {

    private Map<String, WebSocket> connectionsMap = new ConcurrentHashMap<>();


    public ServerApp(int port) {
        super(new InetSocketAddress(port));
    }
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection from: " + conn.getRemoteSocketAddress().getHostName() + "/[" + conn.getRemoteSocketAddress().getAddress().getHostAddress() + "]:" + conn.getRemoteSocketAddress().getPort());
        System.out.println("Number of Connections: " + getConnections().size());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (message == null || message.trim().isEmpty()) {
            System.err.println("Received empty JSON message.");
            return;
        }
        System.out.println("Received raw message:" + message);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Message msg = objectMapper.readValue(message, Message.class);
            if (msg instanceof RegistrationMessage registrationMessage) {
                ServerResponse response = RegistrationHandler.handleRegistration(registrationMessage);
                if (response.getCode() == 200) {
                    connectionsMap.put(registrationMessage.getNickname(), conn);
                    conn.setAttachment(registrationMessage.getNickname());
                }
                conn.send(String.valueOf(response));
            } else if (msg instanceof PrivateMessage privateMessage) {
                System.out.println("Received a Private Message Content: " + privateMessage.getContent());
                if (!connectionsMap.containsKey(privateMessage.getTo())) return;
                ServerResponse response = PrivateMessageHandler.sendPrivateMessage(privateMessage, (String) conn.getAttachment());
                if (response.getCode() == 200) {
                    connectionsMap.get(privateMessage.getTo()).send(String.valueOf(response));
                    response.setType("successful_delivery");
                    conn.send(String.valueOf(response));
                }
            } else if (msg instanceof SearchUserMessage searchMessage) {
                ServerResponse response = SearchHandler.searchUser(searchMessage);
                conn.send(String.valueOf(response));
            } else if (msg instanceof  LoadMessage loadMessage) {

                loadMessage.setSenderUsername((String) conn.getAttachment());
                List<MessageDTO> messages =  LoadHandler.loadMessages(loadMessage);
                if (messages == null || messages.isEmpty()) return;

                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                ServerResponse response = new ServerResponse(200, "load", null);
                response.setMessage(mapper.writeValueAsString(messages));
                String jsonToSend = mapper.writeValueAsString(response);
                conn.send(jsonToSend);
            }
        } catch (JsonProcessingException e) {
            System.err.println("Invalid JSON received from client: " + e.getMessage());
        }
    }


    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
//        System.out.println("Connection closed by " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        String username = (String) conn.getAttachment();
        if (username != null && connectionsMap.containsKey(username)) {
            connectionsMap.remove(username);
            System.out.println("Connection closed for user: " + username);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Error occurred on connection to: " + conn.getRemoteSocketAddress().getAddress().getHostAddress() + ": " + ex);
    }

    @Override
    public void onStart() {
       System.out.println("Server started on port " + getPort());
    }


}
