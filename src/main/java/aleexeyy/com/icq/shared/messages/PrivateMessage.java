package aleexeyy.com.icq.shared.messages;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("private_message")
public class PrivateMessage extends Message {

    private  String type;
    private  String to;
    private  String from;
    private String content;


    public PrivateMessage() {
        this.type = "private_message";
    }
    public PrivateMessage(String to, String from, String content) {
        this.type = "private_message";
        this.to = to;
        this.from = from;
        this.content = content;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }


    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    @Override
    public String toString() {
        return "{"
                + "\"type\":\"" + type + "\", "
                + "\"from\":\"" + from + "\", "
                + "\"to\":\"" + to + "\", "
                + "\"content\":\"" + content + "\""
                + "}";
    }
}


