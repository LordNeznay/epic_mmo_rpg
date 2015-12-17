package dbservice;

import java.sql.SQLException;

/**
 * Created by uschsh on 14.12.15.
 */
public interface TUserResultHandler<T> {
    T handle(UserDataSet result) throws SQLException;
}
