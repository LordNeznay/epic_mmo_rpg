package dbservice;

import org.hibernate.Session;

import java.sql.SQLException;

/**
 * Created by uschsh on 14.12.15.
 */
public interface ExecUpdate<P> {
    void execUpdate(Session session, P param);
}
