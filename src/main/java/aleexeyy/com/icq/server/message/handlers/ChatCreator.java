package aleexeyy.com.icq.server.message.handlers;

import aleexeyy.com.icq.server.db.DatabaseInitializer;
import aleexeyy.com.icq.server.db.entities.Conversation;
import aleexeyy.com.icq.server.ui.ServerController;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

import java.time.LocalDateTime;

public class ChatCreator {

    public static Long createChat(String username1, String username2) {

        Session session = null;
        Transaction transaction = null;
        Long conversationId = null;

        try {
            session = DatabaseInitializer.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Conversation conversation = new Conversation();
            conversation.setCreatedAt(LocalDateTime.now());

            session.persist(conversation);

            conversationId = conversation.getConversationId();

            String hqlInsert =
                    "insert into ConversationParticipant " +
                            "  (conversationId, participantId, joinedAt) " +
                            "select " +
                            "  :convId,    u.userId,  :joinedAt " +
                            "from User u " +
                            "where u.userName in (:nameA, :nameB)";

            MutationQuery insertQuery = session.createMutationQuery(hqlInsert);
            insertQuery.setParameter("convId", conversationId);
            insertQuery.setParameter("joinedAt", LocalDateTime.now());
            insertQuery.setParameter("nameA", username1);
            insertQuery.setParameter("nameB", username2);

            int rowsAffected = insertQuery.executeUpdate();
            if (rowsAffected != 2) {
                // In a real app, handle the case where one or both usernames didn’t match any User.
                throw new IllegalStateException("Expected to insert 2 participants, but inserted " + rowsAffected);
            }
            transaction.commit();

            ServerController.refreshConversationList();

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
        return conversationId;
    }

}
