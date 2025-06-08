package aleexeyy.com.icq.server.db.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id")
    private Long conversationId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Conversation() {}

    public Conversation(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Long getConversationId() {
        return this.conversationId;
    }
    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
