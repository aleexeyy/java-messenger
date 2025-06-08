package aleexeyy.com.icq.shared.messages;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("search")
public class SearchUserMessage extends Message {
    private String type;
    private String from;
    private String nicknameSearch;

    public SearchUserMessage() {
        super();
        type = "search";
    }

    public SearchUserMessage(String from, String nicknameSearch) {
        super();
        this.type = "search";
        this.from = from;
        this.nicknameSearch = nicknameSearch;
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

    public String getNicknameSearch() {
        return nicknameSearch;
    }
    public void setNicknameSearch(String nicknameSearch) {
        this.nicknameSearch = nicknameSearch;
    }

    @Override
    public String toString() {
        return "{"
                + "\"type\":\"" + type + "\", "
                + "\"from\":\"" + from + "\", "
                + "\"nicknameSearch\":\"" + nicknameSearch + "\""
                + "}";
    }

}
