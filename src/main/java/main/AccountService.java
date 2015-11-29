package main;

import dbservice.DBService;
import dbservice.UserDataSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class AccountService {
   // @NotNull private Map<String, UserProfile> users = new HashMap<>();
   // @NotNull private Map<String, UserProfile> sessions = new HashMap<>();
    private DBService dbservice;

    public AccountService(DBService dbservice) {
        this.dbservice = dbservice;
    }

    public boolean addUser(String userName, UserProfile userProfile) {
        if (dbservice.isAvailable(userName))
            return false;
        dbservice.saveUser(new UserDataSet(userProfile.getLogin(), userProfile.getEmail(), userProfile.getPassword(), 0, ""));
        return true;
    }

    public boolean addSession(String sessionId, UserProfile userProfile) {
        UserDataSet user = dbservice.getByName(userProfile.getLogin());

        if(user != null) {
            dbservice.setSession(userProfile.getLogin(), sessionId);
            return true;
        } else
            return false;

    }

    @Nullable
    public UserProfile getUserByName(String userName) {
        UserDataSet dataSet = dbservice.getByName(userName);
        return new UserProfile(dataSet.getName(), dataSet.getPassword(), dataSet.getEmail());
    }

    @Nullable
    public UserProfile getUserBySession(String sessionId) {
        UserDataSet dataSet = dbservice.getBySession(sessionId);
        if (dataSet != null)
            return new UserProfile(dataSet.getName(), dataSet.getPassword(), dataSet.getEmail());
        else
            return null;
    }

    public void removeUser(String sessionId) { /*sessions.remove(sessionId);*/ }

    public long getAuthUsersNumber() { return dbservice.getRegCount(); }

    public long getRegUsersNumber() { return dbservice.getRegCount(); }
}
