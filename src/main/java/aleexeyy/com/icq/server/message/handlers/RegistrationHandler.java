package aleexeyy.com.icq.server.message.handlers;

import aleexeyy.com.icq.server.db.DatabaseInitializer;
import aleexeyy.com.icq.server.db.entities.User;
import aleexeyy.com.icq.shared.messages.RegistrationMessage;
import aleexeyy.com.icq.shared.messages.ServerResponse;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;

public class RegistrationHandler {

    public static ServerResponse handleRegistration(RegistrationMessage registrationMessage) {

        System.out.println("Received nickname: " + registrationMessage.getNickname());
        System.out.println("Received password: " + registrationMessage.getPassword());
        System.out.println("Received from: " + registrationMessage.getFrom());

        User newUser = new User();
        newUser.setUserName(registrationMessage.getNickname());
        newUser.setPasswordHash(registrationMessage.getPassword());
        newUser.setCreatedAt(LocalDateTime.now());
        Session session = null;
        Transaction transaction = null;
        ServerResponse response = new ServerResponse();

        try {
            session = DatabaseInitializer.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(newUser);
            transaction.commit();
            System.out.println("User inserted successfully.");
            response.setCode(200);
            response.setType("registration");
            response.setMessage("Registration successful");

        } catch(HibernateException e) {

            System.out.println("User already exists.");
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            Session lookupSession = null;
            Transaction lookupTx = null;
            try {
                lookupSession = DatabaseInitializer.getSessionFactory().openSession();
                lookupTx = lookupSession.beginTransaction();
                String hql = "SELECT u.passwordHash FROM User u WHERE u.userName = :uname";
                Query<String> query = lookupSession.createQuery(hql, String.class);
                query.setParameter("uname", registrationMessage.getNickname());
                String storedPassword = query.uniqueResult();

                lookupTx.commit();

                if (storedPassword != null && storedPassword.equals(registrationMessage.getPassword())) {
                    response.setCode(200);
                    response.setType("registration");
                    response.setMessage("Login successful");
                } else {
                    response.setCode(400);
                    response.setType("registration");
                    response.setMessage("User already exists, access denied");
                }
            } catch (HibernateException ex) {
                if (lookupTx != null && lookupTx.isActive()) {
                    lookupTx.rollback();
                }
                ex.printStackTrace();
            } finally {
                if (lookupSession != null && lookupSession.isOpen()) {
                    lookupSession.close();
                }
            }


        } catch(Exception e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            response.setCode(500);
            response.setType("registration");
            response.setMessage("Error occurred during registration");

        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        return response;

    }
}
