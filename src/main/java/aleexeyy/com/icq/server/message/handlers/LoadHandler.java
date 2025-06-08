package aleexeyy.com.icq.server.message.handlers;

import aleexeyy.com.icq.server.db.DatabaseInitializer;
import aleexeyy.com.icq.server.db.entities.Conversation;
import aleexeyy.com.icq.shared.messages.LoadMessage;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;

import java.util.List;

public class LoadHandler {
    public static List<MessageDTO> loadMessages(LoadMessage message) {

        Session session = null;
        List<MessageDTO> messages = null;
        try {
            session = DatabaseInitializer.getSessionFactory().openSession();

            String hql =
                    "select cp.conversation " +
                            "from   ConversationParticipant cp " +
                            "join   cp.user u " +
                            "where  u.userName in (:userA, :userB) " +
                            "group  by cp.conversation " +
                            "having count(cp) = 2";
            TypedQuery<Conversation> query = session.createQuery(hql, Conversation.class);
            query.setParameter("userA", message.getSenderUsername());
            query.setParameter("userB", message.getParticipantUsername());

            List<Conversation> convs = query.getResultList();
            Conversation conv = convs.isEmpty() ? null : convs.get(0);
            String messageHql = """
                SELECT new aleexeyy.com.icq.server.message.handlers.MessageDTO(
                    m.content,
                    m.sentAt,
                    m.sender.userName
                )
                FROM Message m
                WHERE m.conversation = :conversation
                ORDER BY m.sentAt ASC
            """;
            TypedQuery<MessageDTO> messageQuery = session.createQuery(messageHql, MessageDTO.class);
            messageQuery.setParameter("conversation", conv);

            messages = messageQuery.getResultList();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return messages;
    }



    public static List<MessageDTO> loadMessages(Long conversationId) {
        Session session = null;
        List<MessageDTO> messages = null;

        try {
            session = DatabaseInitializer.getSessionFactory().openSession();

            String hql = """
                SELECT new aleexeyy.com.icq.server.message.handlers.MessageDTO(
                    m.content,
                    m.sentAt,
                    m.sender.userName
                )
                FROM Message m
                WHERE m.conversation.conversationId = :convId
                ORDER BY m.sentAt ASC
            """;
            messages = session.createQuery(hql, MessageDTO.class)
                    .setParameter("convId", conversationId)
                    .getResultList();

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return messages;

    }
}
