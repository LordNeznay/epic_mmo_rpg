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
import org.jetbrains.annotations.NotNull;
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

    public UserDataSet getByName(@NotNull String username) {
        return executor.execQuery((session) -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.getUserByName(username);
        });
    }


    public boolean deleteByName(@NotNull String username) {
        return executor.execQuery((session) -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.deleteByName(username);
        });
    }


    public long getRegCount() {
        return executor.execQuery((session) -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.getRegCount();
        });
    }

    public void saveUser(UserDataSet dataSet) {
        executor.execUpdate((session) -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            dao.save(dataSet);
        });
    }

    public boolean isAvailable(@NotNull String username) {
        return executor.execQuery((session) -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.isAvailable(username);
        });
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
