package dao;

import org.hibernate.Session;
import dbservice.UserDataSet;
import org.hibernate.Query;
/**
 * Created by uschsh on 29.11.15.
 */
public class UserDataSetDAO {
    private Session session;

    public UserDataSetDAO(Session session) {
        this.session    =   session;
    }

    public UserDataSet getUserByName(String username) {
        Query query = session.getNamedQuery("userByName");
        query.setString("username", username);
        return (UserDataSet) query.uniqueResult();
    }

    public UserDataSet getBySession(String user_session) {
        Query query = session.getNamedQuery("userBySession");
        query.setString("session", user_session);
        return (UserDataSet) query.uniqueResult();
    }

    public boolean deleteBySession(String user_session) {
        Query query = session.getNamedQuery("deleteBySession");
        query.setString("session", user_session);
        query.executeUpdate();
        return true;
    }

    public boolean deleteByName(String username) {
        Query query = session.getNamedQuery("deleteByName");
        query.setString("username", username);
        query.executeUpdate();
        return true;
    }

    public long getAuthUser() {
        Query query = session.getNamedQuery("getAuthUser");
        return (long)query.uniqueResult();
    }

    public void setUserSession(String username, String user_session) {
        UserDataSet dataSet = getUserByName(username);
        dataSet.setSession(user_session);
        session.update(dataSet);
    }

    public void setNullUserSession(String user_session) {
        UserDataSet dataSet = getBySession(user_session);
        dataSet.setSession(null);
        session.update(dataSet);
    }

    public boolean isAvailable(String username) {
        Query query = session.getNamedQuery("userByName");
        query.setString("username", username);
        UserDataSet dataSet = (UserDataSet)query.uniqueResult();
        return dataSet != null;
    }

    public long getRegCount() {
        Query query = session.createQuery("select count(*) from user");
        return (long)query.uniqueResult();
    }

    public void save(UserDataSet dataSet) {
        session.save(dataSet);
    }


}
