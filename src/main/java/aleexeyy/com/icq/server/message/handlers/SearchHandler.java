package aleexeyy.com.icq.server.message.handlers;

import aleexeyy.com.icq.server.db.DatabaseInitializer;
import aleexeyy.com.icq.server.db.entities.User;
import aleexeyy.com.icq.shared.messages.SearchUserMessage;
import aleexeyy.com.icq.shared.messages.ServerResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class SearchHandler {

    public static ServerResponse searchUser(SearchUserMessage searchMessage) {

        Session session = DatabaseInitializer.getSessionFactory().openSession();
        Transaction transaction = session.getTransaction();
        ServerResponse response = new ServerResponse();
        response.setCode(400);
        response.setMessage("No Users Found");
        response.setType("search");
        List<User> users;
        try {
            transaction.begin();
            String hql = "FROM User u WHERE lower(u.userName) LIKE :pattern";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("pattern", (searchMessage.getNicknameSearch().toLowerCase() + "%"));

            users = query.list();

            transaction.commit();
            StringBuilder userNames = new StringBuilder();
            for (User user : users) {
                userNames.append(user.getUserName()).append(",");
            }
            response.setMessage(userNames.toString());
            response.setCode(200);

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return response;
    }
}
