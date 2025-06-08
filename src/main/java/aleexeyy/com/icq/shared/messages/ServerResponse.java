package aleexeyy.com.icq.shared.messages;

public class ServerResponse {
    private int code;
    private String message;
    private String type;
    private String data;

    public ServerResponse() {
        this.data = null;
    }
    public ServerResponse(int code, String type, String message) {
        this.type = type;
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        if (this.data == null) {
            return "{"
                    + "\"code\":\"" + code + "\", "
                    + "\"type\":\"" + type + "\", "
                    + "\"message\":\"" + message + "\""
                    + "}";
        } else {
            return "{"
                    + "\"code\":\"" + code + "\", "
                    + "\"type\":\"" + type + "\", "
                    + "\"message\":\"" + message + "\", "
                    + "\"data\":\"" + data + "\""
                    + "}";
        }
    }
}
