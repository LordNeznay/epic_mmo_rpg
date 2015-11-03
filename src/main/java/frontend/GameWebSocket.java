package frontend;

import mechanics.GameMechanics;
import main.UserProfile;
import org.eclipse.jetty.websocket.api.Session;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Created by uschsh on 25.10.15.
 */
@WebSocket
public class GameWebSocket {
    private UserProfile userProfile;
    private GameMechanics gameMechanics;
    private Session session;

    public GameWebSocket(UserProfile userProfile, GameMechanics gameMechanics) {
        this.userProfile = userProfile;
        this.gameMechanics = gameMechanics;
    }

    public UserProfile getUser() {
        return userProfile;
    }



    @OnWebSocketMessage
    public void onMessage(String data) {
        JSONObject query = null;
        JSONParser jsonPaser = new JSONParser();
        try {
            Object obj = jsonPaser.parse(data);
            query = (JSONObject) obj;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (query != null) {
            switch (query.get("command").toString()) {
                case "join_game":
                    gameMechanics.addUser(userProfile);
                    break;
                case "leave_game":
                    gameMechanics.removeUser(userProfile);
                    break;
                case "action":
                    onGetAction(query);
                    break;
                default: break;
            }
        }
    }

    private void onGetAction(JSONObject query){
        switch (query.get("action").toString()) {
            case "move":
                gameMechanics.movePlayer(userProfile, query.get("direction").toString());
                break;
            case "flagCapture":
                gameMechanics.startFlagCapture(userProfile);
                break;
            case "setTarget":
                try {
                    int x = Integer.valueOf(query.get("x").toString());
                    int y = Integer.valueOf(query.get("y").toString());
                    gameMechanics.setPlayerTarget(userProfile, x, y);
                } catch (NumberFormatException e) {
                    System.err.println("Cannot parse game map!");
                }
                break;
            case "useAbility":
                gameMechanics.useAbility(userProfile, query.get("abilityName").toString());
                break;
            default: break;
        }
    }

    public void sendMessage(String message){
        try {
            session.getRemote().sendString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session _session) {
        this.session = _session;

    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {

    }
}
