package aleexeyy.com.icq.shared.messages;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("load")
public class LoadMessage extends Message {
    private String type;
    private String from;
    private String senderUsername;
    private String participantUsername;

    public LoadMessage() {
        this.type = "load";
    }
    public LoadMessage(String from, String senderUsername, String participantUsername) {
        this.type = "load";
        this.senderUsername = senderUsername;
        this.participantUsername = participantUsername;
        this.from = from;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSenderUsername() {
        return senderUsername;
    }
    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
    public String getParticipantUsername() {
        return participantUsername;
    }
    public void setParticipantUsername(String participantUsername) {
        this.participantUsername = participantUsername;
    }

    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    @Override
    public String toString() {
        return "{"
                + "\"type\":\"" + type + "\", "
                + "\"from\":\"" + from + "\", "
                + "\"senderUsername\":\"" + senderUsername + "\", "
                + "\"participantUsername\":\"" + participantUsername + "\""
                + "}";
    }

}
