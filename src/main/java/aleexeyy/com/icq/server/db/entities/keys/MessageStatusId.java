package aleexeyy.com.icq.server.db.entities.keys;

import java.io.Serializable;
import java.util.Objects;

public class MessageStatusId implements Serializable {

    private Long messageId;
    private Long recipientId;

    public MessageStatusId() {}

    public MessageStatusId(Long messageId, Long recipientId) {
        this.messageId = messageId;
        this.recipientId = recipientId;
    }

    public Long getMessageId() {
        return messageId;
    }
    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
    public Long getRecipientId() {
        return recipientId;
    }
    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageStatusId that = (MessageStatusId) o;

        return this.messageId.equals(that.messageId) && this.recipientId.equals(that.recipientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.messageId, this.recipientId);
    }
}
