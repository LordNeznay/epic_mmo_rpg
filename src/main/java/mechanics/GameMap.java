package mechanics;

import main.UserProfile;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by uschsh on 26.10.15.
 */
public class GameMap {
    public static final int MAX_PLAYERS_IN_COMMAND = 1;

    private Map<UserProfile, Entity> entities = new HashMap<UserProfile, Entity>();
    private int amountRedPlayers = 0;
    private int amountBluePlayers = 0;

    private PhysMapJson physMap = new PhysMapJson();

    public GameMap(){
        System.out.println("Создана новая карта");
    }

    public boolean addUser(UserProfile userProfile) {
        if(amountRedPlayers == MAX_PLAYERS_IN_COMMAND && amountBluePlayers == MAX_PLAYERS_IN_COMMAND){
            return false;
        }
        Entity userEntity = new Entity(this);
        if(amountRedPlayers > amountBluePlayers){
            ++amountBluePlayers;
            userEntity.setCommand("CommandRed");
        } else {
            ++amountBluePlayers;
            userEntity.setCommand("CommandBlue");
        }
        String temp = physMap.getArea(userEntity.getCoord());
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("type", "startMap");
        jsonStart.put("map", temp);

        userProfile.getUserSocket().sendMessage(jsonStart.toString());
        return true;
    }

    public String getArea(UserProfile userProfile){
        Entity entity = entities.get(userProfile);
        return physMap.getArea(entity.getCoord());
    }

    public boolean isPlace(){
        if(amountRedPlayers < MAX_PLAYERS_IN_COMMAND || amountBluePlayers < MAX_PLAYERS_IN_COMMAND){
            return true;
        } else {
            return false;
        }
    }
}
