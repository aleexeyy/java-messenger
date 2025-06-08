package aleexeyy.com.icq.shared.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RegistrationMessage.class, name = "registration"),
        @JsonSubTypes.Type(value = PrivateMessage.class, name = "private_message"),
        @JsonSubTypes.Type(value = SearchUserMessage.class, name = "search"),
        @JsonSubTypes.Type(value = LoadMessage.class, name = "load")
})
public abstract class Message {
    private String type;
    private String from;

    public Message() {}

    public Message(String type, String from) {
        this.type = type;
        this.from = from;
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

}
