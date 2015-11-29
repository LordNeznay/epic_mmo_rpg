package dbservice;


import dao.UserDataSetDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import resource.ServerConfiguration;

import java.util.List;

/**
 * Created by uschsh on 29.11.15.
 */
public class DBService {
    private SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = new Configuration()
        .addAnnotatedClass(UserDataSet.class)
        .setProperty("hibernate.dialect", ServerConfiguration.getInstance().getDialect())
        .setProperty("hibernate.connection.driver_class", ServerConfiguration.getInstance().getDriverClass())
        .setProperty("hibernate.connection.url", ServerConfiguration.getInstance().getConnectionUrl())
        .setProperty("hibernate.connection.username", ServerConfiguration.getInstance().getConnectionUsername())
        .setProperty("hibernate.connection.password", ServerConfiguration.getInstance().getConnectionPassword())
        .setProperty("hibernate.show_sql", ServerConfiguration.getInstance().getShowSql())
        .setProperty("hibernate.hbm2ddl.auto", ServerConfiguration.getInstance().getHbm2ddAuto());

        sessionFactory = createSessionFactory(configuration);
    }

    public UserDataSet getByName(String username) {
        Session session = sessionFactory.openSession();
        UserDataSetDAO dao = new UserDataSetDAO(session);
        return dao.getUserByName(username);
    }

    public UserDataSet getBySession(String user_session) {
        Session session = sessionFactory.openSession();
        UserDataSetDAO dao = new UserDataSetDAO(session);
        return dao.getBySession(user_session);
    }

    public long getRegCount() {
        Session session = sessionFactory.openSession();
        UserDataSetDAO dao = new UserDataSetDAO(session);
        return dao.getRegCount();
    }

    public void saveUser(UserDataSet dataSet) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        UserDataSetDAO dao = new UserDataSetDAO(session);
        dao.save(dataSet);
        session.getTransaction().commit();
        session.close();
    }

    public boolean isAvailable(String username) {
        Session session = sessionFactory.openSession();
        UserDataSetDAO dao = new UserDataSetDAO(session);
        return dao.isAvailable(username);
    }

    public void setSession(String username, String user_session) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        UserDataSetDAO dao = new UserDataSetDAO(session);
        dao.setUserSession(username, user_session);
        session.getTransaction().commit();
        session.close();
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public void shutdown(){
        sessionFactory.close();
    }
}
