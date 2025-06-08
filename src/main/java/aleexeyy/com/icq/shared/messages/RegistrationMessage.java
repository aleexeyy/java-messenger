package aleexeyy.com.icq.shared.messages;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("registration")
public class RegistrationMessage extends Message {

    private String type;
    private String from;
    private String nickname;
    private String password;


    public RegistrationMessage() {
        this.type = "registration";
    }

    public RegistrationMessage(String from, String nickname, String password) {
        this.type = "registration";
        this.from = from;
        this.nickname = nickname;
        this.password = password;
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
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "{"
                + "\"type\":\"" + type + "\", "
                + "\"from\":\"" + from + "\", "
                + "\"nickname\":\"" + nickname + "\", "
                + "\"password\":\"" + password + "\""
                + "}";
    }
}
