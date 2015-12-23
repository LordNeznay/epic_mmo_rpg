package dbservice;

import org.hibernate.Session;

/**
 * Created by uschsh on 23.12.15.
 */
public interface ExecQuery<T, P> {
    T execQuery(Session session, P param);
}
