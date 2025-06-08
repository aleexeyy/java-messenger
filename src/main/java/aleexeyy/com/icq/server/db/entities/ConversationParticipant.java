package aleexeyy.com.icq.server.db.entities;

import aleexeyy.com.icq.server.db.entities.keys.ConversationParticipantId;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@IdClass(ConversationParticipantId.class)
@Table(name="conversation_participants")
public class ConversationParticipant {


    @Id
    @Column(name = "participant_id")
    private Long participantId;

    @Id
    @Column(name = "conversation_id")
    private Long conversationId;

    @MapsId("participantId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participant_id", nullable = false)
    private User user;

    @MapsId("conversationId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;


    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    public ConversationParticipant() { }

    public ConversationParticipant(User user, Conversation conversation, LocalDateTime joinedAt) {
        this.user = user;
        this.conversation = conversation;
        this.participantId = user.getUserId();
        this.conversationId = conversation.getConversationId();
        this.joinedAt = joinedAt;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.participantId = user != null ? user.getUserId() : null;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
        this.conversationId = conversation != null ? conversation.getConversationId() : null;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}
