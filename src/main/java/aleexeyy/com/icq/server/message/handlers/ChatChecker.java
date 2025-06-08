package aleexeyy.com.icq.server.message.handlers;

import aleexeyy.com.icq.server.db.DatabaseInitializer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class ChatChecker {

    public static Long checkExistingConversation(String senderUsername, String recipientUsername) {

        Session session = null;
        Transaction transaction = null;
        Long existingConversationId = null;

        try {
            session = DatabaseInitializer.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            //String hql = "SELECT cp1.conversationId FROM ConversationParticipant cp1 JOIN ConversationParticipant cp2 ON cp1.conversationId = cp2.conversationId JOIN User u1 ON cp1.participantId = u1.userId JOIN User u2 ON cp2.participantId = u2.userId WHERE u1.userName = :userName1 AND u2.userName = :userName2 LIMIT 1";
            String hql =
                    "SELECT cp1.id.conversationId " +
                            "FROM ConversationParticipant cp1, ConversationParticipant cp2 " +
                            "WHERE cp1.id.conversationId = cp2.id.conversationId " +
                            "  AND cp1.user.userName = :userName1 " +
                            "  AND cp2.user.userName = :userName2";

            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("userName1", senderUsername);
            query.setParameter("userName2", recipientUsername);
            query.setMaxResults(1);

            existingConversationId = query.uniqueResult();
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return existingConversationId;

    }
}