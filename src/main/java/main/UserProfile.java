package main;

import frontend.GameWebSocket;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;


/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class UserProfile {
    @NotNull private String login;
    @NotNull private String password;
    @NotNull private String email;

    private GameWebSocket userSocket;
    private ArrayList<String> forSending = new ArrayList<>();

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

    public void addMessageForSending(String body){
        forSending.add(body);
    }

    public void sendMessage(){
        StringBuilder response = new StringBuilder();
        response.append('[');

        boolean isOne = true;
        for (String aForSending : forSending) {
            if (!isOne) response.append(',');
            response.append(aForSending);
            isOne = false;
        }

        response.append(']');
        try{
            userSocket.sendMessage(response.toString());
        } catch (RuntimeException e){
            userSocket = null;
        }
        forSending.clear();
    }
}
