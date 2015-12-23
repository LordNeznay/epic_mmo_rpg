package frontend;

import main.UserProfile;
import mechanics.messages.*;
import messageSystem.Message;
import org.eclipse.jetty.websocket.api.Session;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.ResponseConstructor;
import utils.ResponseHeaders;

import java.io.IOException;

/**
 * Created by uschsh on 25.10.15.
 */
@WebSocket
public class GameWebSocket {
    private UserProfile userProfile;
    @NotNull
    private Frontend frontend;
    private Session session;

    public GameWebSocket(UserProfile userProfile, @NotNull Frontend frontend) {
        this.userProfile = userProfile;
        this.frontend = frontend;
    }

    public UserProfile getUser() {
        return userProfile;
    }

    private void addUserInGame(){
        Message messageAddUserInQueue = new MessageAddUserInQueue(frontend.getAddress(), frontend.getMessageSystem().getAddressService().getGameMechanicsAddress(), userProfile);
        frontend.getMessageSystem().sendMessage(messageAddUserInQueue);
    }

    private void removeUserFromGame(){
        Message messageRemoveUserFromGame = new MessageRemoveUserFromGame(frontend.getAddress(), frontend.getMessageSystem().getAddressService().getGameMechanicsAddress(),userProfile);
        frontend.getMessageSystem().sendMessage(messageRemoveUserFromGame);
    }

    private void movePlayer(String direction){
        Message messageMovePlayer = new MessageMovePlayer(frontend.getAddress(), frontend.getMessageSystem().getAddressService().getGameMechanicsAddress(), userProfile, direction);
        frontend.getMessageSystem().sendMessage(messageMovePlayer);
    }

    private void startFlagCapture(){
        Message messageStartFlagCapture = new MessageStartFlagCapture(frontend.getAddress(), frontend.getMessageSystem().getAddressService().getGameMechanicsAddress(), userProfile);
        frontend.getMessageSystem().sendMessage(messageStartFlagCapture);
    }

    private void setTarget(int x, int y){
        Message messageSetPlayerTarget = new MessageSetPlayerTarget(frontend.getAddress(), frontend.getMessageSystem().getAddressService().getGameMechanicsAddress(), userProfile, x, y);
        frontend.getMessageSystem().sendMessage(messageSetPlayerTarget);
    }

    private void useAbility(String abilityName){
        Message messageUseAbility = new MessageUseAbility(frontend.getAddress(), frontend.getMessageSystem().getAddressService().getGameMechanicsAddress(), userProfile, abilityName);
        frontend.getMessageSystem().sendMessage(messageUseAbility);
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
                    addUserInGame();
                    break;
                case "leave_game":
                    removeUserFromGame();
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
                movePlayer(query.get("direction").toString());
                userProfile.addMessageForSending(ResponseConstructor.getConfirmRequest(ResponseHeaders.CONFIRMED_PLAYER_MOVE));
                break;
            case "flagCapture":
                startFlagCapture();
                userProfile.addMessageForSending(ResponseConstructor.getConfirmRequest(ResponseHeaders.CONFIRMED_START_FLAG_CAPTURE));
                break;
            case "setTarget":
                userProfile.addMessageForSending(ResponseConstructor.getConfirmRequest(ResponseHeaders.CONFIRMED_SET_TARGET));
                try {
                    int x = Integer.valueOf(query.get("x").toString());
                    int y = Integer.valueOf(query.get("y").toString());
                    setTarget(x, y);
                } catch (NumberFormatException e) {
                    System.err.println("Cannot parse game map!");
                }
                break;
            case "useAbility":
                userProfile.addMessageForSending(ResponseConstructor.getConfirmRequest(ResponseHeaders.CONFIRMED_USE_ABILITY));
                useAbility(query.get("abilityName").toString());
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
