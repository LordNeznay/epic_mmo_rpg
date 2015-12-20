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
    @NotNull private Map<String, UserProfile> sessions = new HashMap<>();
    private DBService dbservice;

    public AccountService(DBService dbservice) {
        this.dbservice = dbservice;
    }



    public boolean addUser(String userName, UserProfile userProfile) {
        if (!dbservice.isAvailable(userName))
            return false;
        dbservice.saveUser(new UserDataSet(userProfile.getLogin(), userProfile.getEmail(), userProfile.getPassword(), 0));
        return true;
    }

    public boolean addSession(String sessionId, UserProfile userProfile) {
        UserDataSet user = dbservice.getByName(userProfile.getLogin());

        if(user != null) {
            sessions.put(sessionId, userProfile);
            return true;
        } else
            return false;

    }

    public UserProfile authenticate(String login, String password) {
        UserDataSet dataSet = dbservice.getByName(login);

        if((dataSet != null) && (dataSet.getPassword().equals(password))) {
            UserProfile userProfile = new UserProfile(dataSet.getName(), dataSet.getPassword(), dataSet.getEmail());
            sessions.put(login, userProfile);
            return userProfile;
            }
        return null;
    }

    public boolean isExistUser(String login) {
        return !dbservice.isAvailable(login);
    }

    public void registerUser(String login, String password) {
        if (dbservice.isAvailable(login)) {
            dbservice.saveUser(new UserDataSet(login, "useremail", password, 0));
        }
    }

    public void shutdown()  {
        dbservice.shutdown();
    }

    @Nullable
    public UserProfile getUserByName(String userName) {
        UserDataSet dataSet = dbservice.getByName(userName);
        if(dataSet == null)
            return null;
        return new UserProfile(dataSet.getName(), dataSet.getPassword(), dataSet.getEmail());
    }

    @Nullable
    public UserProfile getUserBySession(String sessionId) {
        return sessions.get(sessionId);
    }


    public void deleteUserByName(String userName) { dbservice.deleteByName(userName); }


    public long getRegUsersNumber() { return dbservice.getRegCount(); }
}
