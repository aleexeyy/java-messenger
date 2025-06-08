package aleexeyy.com.icq.client.backend.response.handlers;
import aleexeyy.com.icq.server.db.DatabaseInitializer;
import aleexeyy.com.icq.server.db.entities.Message;
import aleexeyy.com.icq.server.db.entities.MessageStatus;
import aleexeyy.com.icq.server.db.entities.User;
import aleexeyy.com.icq.shared.messages.ServerResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class MessageStatusUpdater {

    public static void updateMessageStatus(ServerResponse response) {

        String[] dataString = response.getData().split(",");
        List<String> data = Arrays.stream(dataString)
                .map(String::trim)
                .filter(s -> !s.isEmpty()) // filter out empty entries
                .toList();

        String recipientUsername = data.get(0);
        Long messageId = Long.parseLong(data.get(1));
        Session session = null;
        Transaction transaction = null;

        try {
            session = DatabaseInitializer.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            User recipient = session.createQuery(
                            "from User where userName = :username", User.class)
                    .setParameter("username", recipientUsername)
                    .getSingleResult();

            Message message = session.find(Message.class, messageId);

            LocalDateTime now = LocalDateTime.now();

            MessageStatus status = new MessageStatus();
            status.setRecipient(recipient);
            status.setMessage(message);
            status.setDeliveredAt(now);
            status.setReadAt(null);

            session.persist(status);

            transaction.commit();
        } catch(Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
