package aleexeyy.com.icq.server.db.entities;

import aleexeyy.com.icq.server.db.entities.keys.MessageStatusId;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@IdClass(MessageStatusId.class)
@Table(name="message_statuses")
public class MessageStatus {

    @Id
    @Column(name = "message_id")
    private Long messageId;

    @Id
    @Column(name = "recipient_id")
    private Long recipientId;

    @MapsId("messageId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @MapsId("recipientId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;


    @Column(name = "delivered_at", nullable = true, updatable = false)
    private LocalDateTime deliveredAt;

    @Column(name = "read_at", nullable = true, updatable = false)
    private LocalDateTime readAt;

    public MessageStatus() {}

    public MessageStatus(Message message, User recipient) {
        this.message = message;
        this.messageId = message.getMessageId();
        this.recipient = recipient;
        this.recipientId = recipient.getUserId();
        this.deliveredAt = null;
        this.readAt = null;
    }

    public MessageStatus(Message message, User recipient, LocalDateTime deliveredAt, LocalDateTime readAt) {
        this.message = message;
        this.messageId = message.getMessageId();
        this.recipient = recipient;
        this.recipientId = recipient.getUserId();
        this.deliveredAt = deliveredAt;
        this.readAt = readAt;
    }


    public Long getMessageId() {
        return messageId;
    }
    public Long getRecipientId() {
        return recipientId;
    }
    public Message getMessage() {
        return message;
    }
    public void setMessage(Message message) {
        this.message = message;
        this.messageId = message.getMessageId();
    }
    public User getRecipient() {
        return recipient;
    }
    public void setRecipient(User recipient) {
        this.recipient = recipient;
        this.recipientId = recipient.getUserId();
    }
    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }
    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }
    public LocalDateTime getReadAt() {
        return readAt;
    }
    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
}
