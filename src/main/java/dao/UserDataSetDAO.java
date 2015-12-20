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


    public boolean deleteByName(String username) {
        Query query = session.getNamedQuery("deleteByName");
        query.setString("username", username);
        query.executeUpdate();
        return true;
    }


    public boolean isAvailable(String username) {
        Query query = session.getNamedQuery("userByName");
        query.setString("username", username);
        UserDataSet dataSet = (UserDataSet)query.uniqueResult();
        return dataSet == null;
    }

    public long getRegCount() {
        Query query = session.createQuery("select count(*) from user");
        return (long)query.uniqueResult();
    }

    public void save(UserDataSet dataSet) {
        session.save(dataSet);
    }


}
