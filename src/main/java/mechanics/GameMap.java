package mechanics;

import com.sun.javafx.geom.Vec2d;
import main.UserProfile;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by uschsh on 26.10.15.
 */
public class GameMap {
    public static final int MAX_PLAYERS_IN_COMMAND = 2;
    private static final int VIEW_WIDTH_2 = 8;
    private static final int VIEW_HEIGHT_2 = 5;
    private int mapWidth;
    private int mapHeight;

    private Map<UserProfile, Entity> entities = new HashMap<UserProfile, Entity>();
    private Entity[][] entityLocation;
    private int amountRedPlayers = 0;
    private int amountBluePlayers = 0;

    private PhysMapJson physMap = new PhysMapJson();

    public GameMap(){
        System.out.println("Создана новая карта");
        Vec2d sizeMap = physMap.getSize();
        mapWidth = (int) sizeMap.x;
        mapHeight = (int) sizeMap.y;
        entityLocation = new Entity[mapWidth][mapHeight];
        for(int i=0; i<mapWidth; ++i){
            for(int j=0; j<mapHeight; ++j){
                entityLocation[i][j] = null;
            }
        }
    }

    public boolean addUser(UserProfile userProfile) {
        if(amountRedPlayers == MAX_PLAYERS_IN_COMMAND && amountBluePlayers == MAX_PLAYERS_IN_COMMAND){
            return false;
        }
        Entity userEntity = new Entity(this);
        entityLocation[(int)userEntity.getCoord().x][(int)userEntity.getCoord().y] = userEntity;
        if(amountRedPlayers < amountBluePlayers){
            ++amountRedPlayers;
            userEntity.setCommand("CommandRed");
        } else {
            ++amountBluePlayers;
            userEntity.setCommand("CommandBlue");
        }

        entities.put(userProfile, userEntity);

        JSONObject jsonStart = new JSONObject();
        jsonStart.put("type", "user_was_joined");
        userProfile.getUserSocket().sendMessage(jsonStart.toString());
        return true;
    }

    public void sendPlayerViewArea(UserProfile userProfile){
        String viewArea = getArea(userProfile);
        JSONObject request = new JSONObject();
        request.put("type", "viewArea");
        request.put("map", viewArea);
        userProfile.getUserSocket().sendMessage( request.toString());
    }

    public void sendEntityInViewArea(UserProfile userProfile){
        Entity playerEntity = entities.get(userProfile);
        Vec2d playerPosition = playerEntity.getCoord();
        StringBuilder entityesInViewArea = new StringBuilder();
        entityesInViewArea.append("{");

        entityesInViewArea.append("\"player\": {\"x\":");
        entityesInViewArea.append(VIEW_WIDTH_2);
        entityesInViewArea.append(",\"y\": ");
        entityesInViewArea.append(VIEW_HEIGHT_2);
        entityesInViewArea.append(",\"image\": \"people.png\"},");

        entityesInViewArea.append("\"entities\": [");
        int amountEntity = 0;

        int y = 0;
        for(int j = (int)playerPosition.y - VIEW_HEIGHT_2; j <= playerPosition.y + VIEW_HEIGHT_2; ++j, ++y){
            int x=0;
            for(int i = (int)playerPosition.x - VIEW_WIDTH_2; i <= playerPosition.x + VIEW_WIDTH_2; ++i, ++x){
                if(i >= 0 && i < mapWidth && j >= 0 && j < mapHeight ) {
                    if(entityLocation[i][j] == null || entityLocation[i][j] == playerEntity) continue;
                    if(amountEntity!=0) {
                        entityesInViewArea.append(", ");
                    }
                    entityesInViewArea.append("{\"x\":");
                    entityesInViewArea.append(x);
                    entityesInViewArea.append(",\"y\":");
                    entityesInViewArea.append(y);
                    entityesInViewArea.append(",\"image\": \"people.png\"}");
                    ++amountEntity;
                }
            }
        }
        entityesInViewArea.append("]}");

        JSONObject request = new JSONObject();
        request.put("type", "entitiesInViewArea");
        request.put("entities", entityesInViewArea.toString());
        userProfile.getUserSocket().sendMessage( request.toString());
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

    public void removeUser(UserProfile userProfile){
        Entity playerEntity = entities.get(userProfile);
        if(playerEntity.getCommand().equals("CommandRed")){
            --amountRedPlayers;
        } else {
            --amountBluePlayers;
        }
    }

    public void gameAction(UserProfile userProfile, String action, String params){
        switch (action){
            case "move":
                entityMove(userProfile, params);
                break;
        }
    }

    public void entityMove(UserProfile userProfile, String params){
        Entity playerEntity = entities.get(userProfile);
        entityLocation[(int)playerEntity.getCoord().x][(int)playerEntity.getCoord().y] = null;
        playerEntity.move(params);
        entityLocation[(int)playerEntity.getCoord().x][(int)playerEntity.getCoord().y] = playerEntity;
    }

    public boolean isPassability(Vec2d cell){
        boolean result = false;
        if(cell.x >= 0 && cell.x < mapWidth && cell.y >= 0 && cell.y < mapHeight ) {
            result = entityLocation[(int) cell.x][(int) cell.y] == null;
        }
        return result && physMap.isPassability(cell);
    }

    public void stepping(){
        for (Map.Entry<UserProfile, Entity> entry : entities.entrySet())
        {
            sendPlayerViewArea(entry.getKey());
            sendEntityInViewArea(entry.getKey());
        }
    }
}
