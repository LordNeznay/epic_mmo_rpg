package main;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class AccountService {
    @NotNull private Map<String, UserProfile> users = new HashMap<>();
    @NotNull private Map<String, UserProfile> sessions = new HashMap<>();

    public boolean addUser(String userName, UserProfile userProfile) {
        if (users.containsKey(userName))
            return false;
        users.put(userName, userProfile);
        return true;
    }

    public void addSessions(String sessionId, UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    @Nullable
    public UserProfile getUser(String userName) {
        return users.get(userName);
    }

    @Nullable
    public UserProfile getSessions(String sessionId) {
        return sessions.get(sessionId);
    }

    public void removeUser(String sessionId) { sessions.remove(sessionId); }

    public int getAuthUsersNumber() { return sessions.size(); }

    public int getRegUsersNumber() { return users.size(); }
}
