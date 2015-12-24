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

    public DBService(Configuration configuration) {

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
