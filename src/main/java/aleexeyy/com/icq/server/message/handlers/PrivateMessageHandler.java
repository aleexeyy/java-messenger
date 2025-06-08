package aleexeyy.com.icq.server.message.handlers;

import aleexeyy.com.icq.shared.messages.PrivateMessage;
import aleexeyy.com.icq.shared.messages.ServerResponse;

public class PrivateMessageHandler {
    public static ServerResponse sendPrivateMessage(PrivateMessage privateMessage, String senderUsername) {
        Long existingConversationId = ChatChecker.checkExistingConversation(senderUsername, privateMessage.getTo());
        ServerResponse serverResponse = new ServerResponse(400, "private_message", "Couldn't send the message");
        if (existingConversationId == null) {

            System.out.println("No existing conversation between " + senderUsername + " and " + privateMessage.getTo());
            existingConversationId = ChatCreator.createChat(privateMessage.getTo(), senderUsername);
        } else {
            System.out.println("Found existing conversation: " + existingConversationId);
        }
        if (existingConversationId != null) {
            Long messageId = MessageUtils.addMessageToDB(senderUsername, existingConversationId, privateMessage.getContent());
            serverResponse.setCode(200);
            serverResponse.setMessage(senderUsername);
            serverResponse.setData(privateMessage.getTo() + "," + messageId);
        }

        return serverResponse;
    }
}
