package frontend;

import base.GameUser;
import mechanics.GameMechanics;
import main.UserProfile;
import org.eclipse.jetty.websocket.api.Session;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONObject;
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

    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        this.session = session;

        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "wait" );
        jsonStart.put("name", "name");
        jsonStart.put("score", "38");
        try {
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
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
