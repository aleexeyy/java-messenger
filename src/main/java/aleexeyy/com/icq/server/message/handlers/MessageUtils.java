package aleexeyy.com.icq.server.message.handlers;

import aleexeyy.com.icq.server.db.DatabaseInitializer;
import aleexeyy.com.icq.server.db.entities.Conversation;
import aleexeyy.com.icq.server.db.entities.Message;
import aleexeyy.com.icq.server.db.entities.User;
import aleexeyy.com.icq.server.ui.ServerController;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;

public class MessageUtils {

    public static Long addMessageToDB(String senderUsername, Long conversationId, String messageContent) {

        Session session = null;
        Transaction transaction = null;
        Long messageId = null;
        try {
            session = DatabaseInitializer.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            User sender = session
                    .createQuery("FROM User u WHERE u.userName = :uname", User.class)
                    .setParameter("uname", senderUsername)
                    .uniqueResult();
            Conversation conv = session
                    .createQuery("FROM Conversation c WHERE c.conversationId = :cid", Conversation.class)
                    .setParameter("cid", conversationId)
                    .uniqueResult();

            Message msg = new Message(sender, conv, LocalDateTime.now(), messageContent);

            session.persist(msg);

            session.flush();

            messageId = msg.getMessageId();


            transaction.commit();

            ServerController.getInstance().loadMessagesFor(conversationId);

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return messageId;
    }
}
