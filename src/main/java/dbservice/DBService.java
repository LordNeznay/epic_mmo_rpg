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
    private TExecutor executor;


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

        executor = new TExecutor(sessionFactory);
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

        executor = new TExecutor(sessionFactory);
    }

    public UserDataSet getByName(String username) {
        return executor.execQuery((session, param) -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.getUserByName(param);
            }, username);
    }


    public boolean deleteByName(String username) {
        return executor.execQuery((session, param) -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.deleteByName(param);
        }, username);
    }


    public long getRegCount() {
        return executor.execQuery((session, param) -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.getRegCount();
        }, 0);
    }

    public void saveUser(UserDataSet dataSet) {
        executor.execUpdate((session, param) -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            dao.save(param);
        }, dataSet);
    }

    public boolean isAvailable(String username) {
        return executor.execQuery((session, param) -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.isAvailable(param);
        }, username);
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
