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

    public DBService(String state) {
        String hbm2ddl_auto;
        String url;
        if (state.equals("test")) {
            hbm2ddl_auto = ServerConfiguration.getInstance().getHbm2ddAutoTest();
            url = ServerConfiguration.getInstance().getConnectionUrlTest();
        }
        else
        {
            hbm2ddl_auto = ServerConfiguration.getInstance().getHbm2ddAuto();
            url = ServerConfiguration.getInstance().getConnectionUrl();
        }

        Configuration configuration = new Configuration()
        .addAnnotatedClass(UserDataSet.class)
        .setProperty("hibernate.dialect", ServerConfiguration.getInstance().getDialect())
        .setProperty("hibernate.connection.driver_class", ServerConfiguration.getInstance().getDriverClass())
        .setProperty("hibernate.connection.url", url)
        .setProperty("hibernate.connection.username", ServerConfiguration.getInstance().getConnectionUsername())
        .setProperty("hibernate.connection.password", ServerConfiguration.getInstance().getConnectionPassword())
        .setProperty("hibernate.show_sql", ServerConfiguration.getInstance().getShowSql())
        .setProperty("hibernate.hbm2ddl.auto", hbm2ddl_auto);

        sessionFactory = createSessionFactory(configuration);
    }
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
        UserDataSet dataSet = dao.getUserByName(username);
        session.close();
        return dataSet;
    }


    public boolean deleteByName(String username) {
        Session session = sessionFactory.openSession();
        UserDataSetDAO dao = new UserDataSetDAO(session);
        boolean status = dao.deleteByName(username);
        session.close();
        return status;
    }


    public long getRegCount() {
        Session session = sessionFactory.openSession();
        UserDataSetDAO dao = new UserDataSetDAO(session);
        long count = dao.getRegCount();
        session.close();
        return count;
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
        boolean status =dao.isAvailable(username);
        session.close();
        return status;
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
