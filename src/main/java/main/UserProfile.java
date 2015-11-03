package main;

import frontend.GameWebSocket;
import org.jetbrains.annotations.NotNull;
import utils.Repairer;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class UserProfile {
    @NotNull private String login;
    @NotNull private String password;
    @NotNull private String email;

    private GameWebSocket userSocket;

    public UserProfile(@NotNull String login, @NotNull String password, @NotNull String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    @NotNull public String getLogin() {
        return login;
    }

    @NotNull public String getPassword() {
        return password;
    }

    @NotNull public String getEmail() {
        return email;
    }

    public void addSocket(@NotNull GameWebSocket socket){
        userSocket = socket;
    }

    public void sendMessage(String message){
        try{
            userSocket.sendMessage(message);
        } catch (RuntimeException e){
            Repairer.getInstance().repaireUser(this);
            userSocket = null;
        }
    }
}
