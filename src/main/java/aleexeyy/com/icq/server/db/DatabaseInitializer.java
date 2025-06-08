package aleexeyy.com.icq.server.db;

import aleexeyy.com.icq.server.db.entities.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class DatabaseInitializer {
    private static SessionFactory sessionFactory;

    static {
        try {
            String dbUser = System.getenv("DB_USERNAME");
            String dbPass = System.getenv("DB_PASSWORD");
            String dbUrl = System.getenv("DB_URL");

            System.out.println("dbUser = " + dbUser);
            System.out.println("dbPass = " + dbPass);
            System.out.println("dbUrl = " + dbUrl);

            if (dbUser == null || dbPass == null || dbUrl == null) {
                throw new IllegalStateException("DATABASE Environmental Variables must be set!");
            }

            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            if (dbUser != null && !dbUser.isEmpty()) {
                configuration.setProperty("hibernate.connection.username", dbUser);
            }
            if (dbPass != null && !dbPass.isEmpty()) {
                configuration.setProperty("hibernate.connection.password", dbPass);
            }

            if (dbUrl != null && !dbUrl.isEmpty()) {
                configuration.setProperty("hibernate.connection.url", dbUrl);
            }

            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Conversation.class);
            configuration.addAnnotatedClass(ConversationParticipant.class);
            configuration.addAnnotatedClass(Message.class);
            configuration.addAnnotatedClass(MessageStatus.class);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
