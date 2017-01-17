package dbservice;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Created by uschsh on 14.12.15.
 */
public class TExecutor {
    private SessionFactory sessionFactory;

    public TExecutor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T> T execQuery(ExecQuery<T> handler) {
        T result;
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            result = handler.execQuery(session);

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return result;
    }

    public void execUpdate(ExecUpdate handler) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            handler.execUpdate(session);

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
        }
    }
}
