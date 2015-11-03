package mechanics;

import com.sun.javafx.geom.Vec2d;
import main.UserProfile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.Repairer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by uschsh on 26.10.15.
 */
public class GameMap {
    public static final int MAX_PLAYERS_IN_COMMAND = 2;
    private static final int VIEW_WIDTH_2 = 8;
    private static final int VIEW_HEIGHT_2 = 5;
    private static final int POINTS_TO_WIN = 10;
    private boolean isEnd = false;
    private int mapWidth;
    private int mapHeight;

    private Map<UserProfile, Entity> entities = new HashMap<>();
    private Flag flag = new Flag();
    private Entity[][] entityLocation;
    private int amountRedPlayers = 0;
    private int amountBluePlayers = 0;
    private PhysMapJson physMap = new PhysMapJson();

    public boolean getEnd(){
        return isEnd;
    }

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

        parseObjectLayer(physMap.getObjectLayer());
    }

    public String  getResult(){
        if(amountBluePlayers == 0) {
            return flag.getResult(true, "CommandRed");
        }
        if(amountRedPlayers == 0) {
            return flag.getResult(true, "CommandBlue");
        }
        return flag.getResult();
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

        JSONObject request = new JSONObject();

        request.put("type", "user_was_joined");

        userProfile.sendMessage(request.toString());
        return true;
    }

    public void sendPlayerViewArea(UserProfile userProfile){
        String viewArea = getArea(userProfile);
        JSONObject request = new JSONObject();

        request.put("type", "viewArea");
        request.put("map", viewArea);
        userProfile.sendMessage(request.toString());
    }

    private String getTargetJson(int viewX, int viewY){
        return "{\"x\":" + viewX + ",\"y\":" + viewY + ",\"image\": \"target.png\"}";
    }

    private String getFlagEntityJson(int viewX, int viewY){
        StringBuilder result = new StringBuilder();
        result.append("{\"x\":");
        result.append(viewX);
        result.append(",\"y\":");
        result.append(viewY);
        result.append(",\"image\": \"");
        switch(flag.getOwner()){
            case "CommandBlue":
                result.append("blue_flag.png");
                break;
            case "CommandRed":
                result.append("red_flag.png");
                break;
            default:
                result.append("flag.png");
                break;
        }
        result.append("\"}");
        return result.toString();
    }

    private String getEntityJson(int viewX, int viewY, int mapX, int mapY){
        StringBuilder result = new StringBuilder();
        result.append("{\"x\":");
        result.append(viewX);
        result.append(",\"y\":");
        result.append(viewY);
        result.append(",\"image\": \"");
        switch(entityLocation[mapX][mapY].getCommand()){
            case "CommandBlue":
                result.append("blue_people.png");
                break;
            case "CommandRed":
                result.append("red_people.png");
                break;
            default:
                result.append("people.png");
                break;
        }
        result.append("\"}");
        return result.toString();
    }

    private String getAllEntityInViewAreaJson(Entity entity){
        StringBuilder result = new StringBuilder();
        int amountEntity = 0;
        int y = 0;
        for(int j = (int)entity.getCoord().y - VIEW_HEIGHT_2; j <= entity.getCoord().y + VIEW_HEIGHT_2; ++j, ++y){
            int x=0;
            for(int i = (int)entity.getCoord().x - VIEW_WIDTH_2; i <= entity.getCoord().x + VIEW_WIDTH_2; ++i, ++x){
                if(isPositionCorrect(j, i)) {
                    if((int)flag.getPosition().x == i && (int)flag.getPosition().y == j){
                        if(amountEntity!=0) {
                            result.append(", ");
                        }
                        result.append(getFlagEntityJson(x, y));
                        ++amountEntity;
                    }

                    if(entityLocation[i][j] == null) continue;
                    if(entity.getTarget() != null) {
                        if (entity.getTarget().equals(entityLocation[i][j])) {
                            if (amountEntity != 0) {
                                result.append(", ");
                            }
                            result.append(getTargetJson(x, y));
                            ++amountEntity;
                        }
                    }

                    if(entityLocation[i][j].equals(entity)) continue;
                    if(amountEntity!=0) {
                        result.append(", ");
                    }
                    result.append(getEntityJson(x, y, i, j));
                    ++amountEntity;
                }
            }
        }

        return result.toString();
    }

    public void sendEntityInViewArea(UserProfile userProfile){
        Entity playerEntity = entities.get(userProfile);

        JSONObject request = new JSONObject();
        request.put("type", "entitiesInViewArea");
        request.put("entities", '{' + "\"player\": {\"x\":" + VIEW_WIDTH_2 + ",\"y\": " + VIEW_HEIGHT_2 + ",\"image\": \"people.png\"}," + "\"entities\": [" + getAllEntityInViewAreaJson(playerEntity) + "]}");
        userProfile.sendMessage(request.toString());
    }

    private boolean isPositionCorrect(int j, int i) {
        return i >= 0 && i < mapWidth && j >= 0 && j < mapHeight;
    }

    public String getArea(UserProfile userProfile){
        Entity entity = entities.get(userProfile);
        return physMap.getArea(entity.getCoord());
    }

    public boolean isPlace(){
        return (amountRedPlayers < MAX_PLAYERS_IN_COMMAND || amountBluePlayers < MAX_PLAYERS_IN_COMMAND);
    }

    public void removeUser(UserProfile userProfile){
        Entity playerEntity = entities.get(userProfile);
        if(playerEntity == null) return;
        if(playerEntity.getCommand().equals("CommandRed")){
            --amountRedPlayers;
        } else {
            --amountBluePlayers;
        }
        entities.remove(userProfile);
        entityLocation[(int)playerEntity.getCoord().x][(int)playerEntity.getCoord().y] = null;
        for (Map.Entry<UserProfile, Entity> entry : entities.entrySet())
        {
            if(entry.getValue().getTarget() == null){
                continue;
            }
            if(entry.getValue().getTarget().equals(playerEntity)){
                entry.getValue().setTarget(null);
            }
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
        if(isPositionCorrect((int)cell.x, (int)cell.y) ) {
            result = entityLocation[(int) cell.x][(int) cell.y] == null;
        }
        return result && physMap.isPassability(cell);
    }

    private void sendAvailableActions(UserProfile userProfile){
        StringBuilder availableActions = new StringBuilder();
        availableActions.append('[');
        if(flag.isMayInteract(entities.get(userProfile))){
            availableActions.append("\"flag\"");
        }
        availableActions.append(']');

        JSONObject request = new JSONObject();
        request.put("type", "availableActions");
        request.put("availableActions", availableActions.toString());
        userProfile.sendMessage(request.toString());
    }

    public void stepping(){
        for (Map.Entry<UserProfile, Entity> entry : entities.entrySet())
        {
            entry.getValue().stepping(entry.getKey());
            sendPlayerViewArea(entry.getKey());
            sendEntityInViewArea(entry.getKey());
            sendAvailableActions(entry.getKey());
            sendEntityStatus(entry.getKey());
            flag.sendStatus(entry.getKey());
        }
        flag.stepping();
        if(flag.getMaxPoints() == POINTS_TO_WIN){
            isEnd = true;
        }
        if(amountBluePlayers == 0 || amountRedPlayers == 0){
            isEnd = true;
        }
    }

    public String getUserCommand(UserProfile userProfile){
        try {
            Entity userEntity = entities.get(userProfile);
            return userEntity.getCommand();
        } catch(RuntimeException e){
            Repairer.getInstance().repaireUser(userProfile);
        }
        return "none";
    }

    private Vec2d getObjectsPosition(JSONObject obj){
        int objX = 0; int objW = 0;
        int objY = 0; int objH = 0;
        try{
            objX = Integer.valueOf(obj.get("x").toString());
            objY = Integer.valueOf(obj.get("y").toString()) - 1;
            objW = Integer.valueOf(obj.get("width").toString());
            objH = Integer.valueOf(obj.get("height").toString());
        }catch (NumberFormatException e) {
            System.err.println("Cannot parse game map!");
        }
        return new Vec2d(objX / objW, objY / objH);
    }

    private void parseObjectLayer(String objects){
        JSONArray mapObjects = null;
        JSONParser jsonPaser = new JSONParser();
        try {
            Object obj = jsonPaser.parse(objects);
            mapObjects = (JSONArray)obj;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert mapObjects != null;
        for(Object object : mapObjects){
            switch (((JSONObject)object).get("name").toString()){
                case "Flag":
                    flag.setPosition(getObjectsPosition((JSONObject)object));
                    break;
                case "CommandsRedSpawnPoint":
                    Entity.setCommandsRedSpawnPoint(getObjectsPosition((JSONObject)object));
                    break;
                case "CommandsBlueSpawnPoint":
                    Entity.setCommandsBlueSpawnPoint(getObjectsPosition((JSONObject)object));
                    break;
                default: break;
            }
        }
    }

    public void updatePositionEntity(Entity entity, int x, int y){
        if(isPositionCorrect(y, x)){
            entityLocation[x][y] = null;
        }
        x = (int)entity.getCoord().x;
        y = (int)entity.getCoord().y;
        if(isPositionCorrect(y, x)){
            entityLocation[x][y] = entity;
        }
    }

    public void startFlagCapture(UserProfile userProfile){
        Entity playerEntity = entities.get(userProfile);
        flag.startCapture(playerEntity);
    }

    public void setPlayerTarget(UserProfile userProfile, int x, int y){
        Entity playerEntity = entities.get(userProfile);
        x = (int)playerEntity.getCoord().x - VIEW_WIDTH_2 + x;
        y = (int)playerEntity.getCoord().y - VIEW_HEIGHT_2 + y;
        if(isPositionCorrect(y, x)){
            if(entityLocation[x][y] != null){
                playerEntity.setTarget(entityLocation[x][y]);
            }
        }
    }

    public void useAbility(UserProfile userProfile, String abilityName){
        Entity playerEntity = entities.get(userProfile);
        playerEntity.useAbility(abilityName);
    }

    private void sendEntityStatus(UserProfile userProfile){
        Entity playerEntity = entities.get(userProfile);
        StringBuilder entityStatus = new StringBuilder();
        entityStatus.append('{');
        entityStatus.append("\"hitPoints\":");
        entityStatus.append(playerEntity.getHitPoints());
        if(playerEntity.isHaveTarget()) {
            entityStatus.append(", \"targetsHitPoints\":");
            entityStatus.append(playerEntity.getTargetsHitPoints());
        }
        entityStatus.append('}');

        JSONObject request = new JSONObject();
        request.put("type", "entityStatus");
        request.put("entityStatus", entityStatus.toString());
        userProfile.sendMessage(request.toString());
    }
}
