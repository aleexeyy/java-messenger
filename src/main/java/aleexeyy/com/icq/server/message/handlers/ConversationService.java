package aleexeyy.com.icq.server.message.handlers;

import aleexeyy.com.icq.server.db.DatabaseInitializer;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConversationService {
    public static List<Long> getAllConversationIds() {
        Session session = null;
        List<Long> conversationIds = null;
        try {
            session = DatabaseInitializer.getSessionFactory().openSession();
            String hql = "SELECT c.conversationId FROM Conversation c";

            Query<Long> query = session.createQuery(hql, Long.class);
            conversationIds = query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return conversationIds;
    }

    public static List<String> getAllConversationIds(String username) {
        Session session = null;
        List<String> conversationIds = null;
        try {
            session = DatabaseInitializer.getSessionFactory().openSession();
            String hql =
                    "select distinct cp2.user.userName " +
                            "from   ConversationParticipant cp1, ConversationParticipant cp2 " +
                            "where  cp1.conversation = cp2.conversation    " +
                            "  and  cp1.user.userName   = :username       " +
                            "  and  cp2.user.userName  <> :username       ";

            conversationIds = session.createQuery(hql, String.class).setParameter("username", username).getResultList();;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return conversationIds;
    }



    public static Map<Long, List<String>> getAllConversationParticipants(List<Long> conversationIds) {
        Session session = null;
        Map<Long, List<String>> participantsByConversation = null;

        try {
            session = DatabaseInitializer.getSessionFactory().openSession();
            String hql =
                    "select cp.conversation.conversationId, u.userName " +
                            "from   ConversationParticipant cp " +
                            "join   cp.user u " +
                            "where  cp.conversation.conversationId in :ids";

            List<Object[]> rows = session.createQuery(hql, Object[].class)
                    .setParameter("ids", conversationIds)
                    .getResultList();

            participantsByConversation = rows.stream()
                    .collect(Collectors.groupingBy(
                            row -> (Long) row[0],
                            Collectors.mapping(row -> (String) row[1], Collectors.toList())
                    ));


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return participantsByConversation;
    }
}
