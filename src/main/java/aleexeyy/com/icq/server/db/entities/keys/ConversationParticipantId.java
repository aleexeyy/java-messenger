package aleexeyy.com.icq.server.db.entities.keys;


import java.io.Serializable;
import java.util.Objects;

public class ConversationParticipantId implements Serializable {
    private Long conversationId;
    private Long participantId;

    public ConversationParticipantId() {}
    public ConversationParticipantId(Long conversationId, Long participantId) {
        this.conversationId = conversationId;
        this.participantId = participantId;
    }
    public Long getConversationId() {
        return this.conversationId;
    }
    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
    public Long getParticipantId() {
        return this.participantId;
    }
    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ConversationParticipantId that = (ConversationParticipantId) o;

        return this.getConversationId().equals(that.getConversationId()) && this.getParticipantId().equals(that.getParticipantId());
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.getConversationId(), this.getParticipantId());
    }
}