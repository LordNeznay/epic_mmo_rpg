package mechanics;

import com.sun.javafx.geom.Vec2d;
import main.UserProfile;
import org.jetbrains.annotations.TestOnly;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import resource.Configuration;
import resource.ResourceFactory;
import utils.Repairer;
import utils.ResponseConstructor;
import utils.ResponseHeaders;

import javax.jws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by uschsh on 26.10.15.
 */
public class GameMap {
    private static final int MAX_PLAYERS_IN_COMMAND = Configuration.getInstance().getAmountPlayerInCommand();
    private static final int VIEW_WIDTH_2 = 8;
    private static final int VIEW_HEIGHT_2 = 5;
    private static final int POINTS_TO_WIN = Configuration.getInstance().getPointsToWin();
    private boolean isEnd = false;
    private int mapWidth;
    private int mapHeight;

    private Map<UserProfile, Entity> entities = new HashMap<>();
    private Flag flag = new Flag();
    private Entity[][] entityLocation;
    private int amountRedPlayers = 0;
    private int amountBluePlayers = 0;
    private PhysMapJson physMap = (PhysMapJson)ResourceFactory.getInstance().getPhysMap("public_html/res/tilemap.json");

    public boolean getEnd(){
        return isEnd;
    }

    public static int getMaxPlayersInCommand(){
        return MAX_PLAYERS_IN_COMMAND;
    }

    public int getAmountRedPlayers(){
        return amountRedPlayers;
    }

    public int getAmountBluePlayers(){
        return amountBluePlayers;
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

    public String getResult(){
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

        sendConfirmation(userProfile);
        return true;
    }

    private void sendConfirmation(UserProfile userProfile){
        String response = ResponseConstructor.getResponse(ResponseHeaders.USER_WAS_JOINED, "{}");
        userProfile.addMessageForSending(response);
    }

    public void sendPlayersPosition(UserProfile userProfile){
        Vec2d pos;
        try {
            if(!entities.get(userProfile).isCoordChange()) {
                return;
            }
            pos = entities.get(userProfile).getCoord();
        } catch (RuntimeException e){
            Repairer.getInstance().repaireUser(userProfile);
            return;
        }

        String response = ResponseConstructor.getCoordJson(pos);
        response = ResponseConstructor.getResponse(ResponseHeaders.PLAYER_POSITION, response);
        userProfile.addMessageForSending(response);
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
                        result.append(ResponseConstructor.getFlagJson(new Vec2d(x, y), flag.getOwner()));
                        ++amountEntity;
                    }

                    if(entityLocation[i][j] == null) continue;
                    if(entity.getTarget() != null) {
                        if (entity.getTarget().equals(entityLocation[i][j])) {
                            if (amountEntity != 0) {
                                result.append(", ");
                            }
                            result.append(ResponseConstructor.getTargetJson(new Vec2d(x, y)));
                            ++amountEntity;
                        }
                    }

                    if(entityLocation[i][j].equals(entity)) continue;
                    if(amountEntity!=0) {
                        result.append(", ");
                    }
                    result.append(ResponseConstructor.getEntityJson(new Vec2d(x, y), entityLocation[i][j].getCommand()));
                    ++amountEntity;
                }
            }
        }

        return result.toString();
    }

    public void sendEntityInViewArea(UserProfile userProfile){
        Entity playerEntity = entities.get(userProfile);

        String response = ResponseConstructor.entitiesInViewArea(new Vec2d(VIEW_WIDTH_2, VIEW_HEIGHT_2), getAllEntityInViewAreaJson(playerEntity));
        response = ResponseConstructor.getResponse(ResponseHeaders.ENTITIES_IN_VIEW_AREA, response);
        userProfile.addMessageForSending(response);
    }

    private boolean isPositionCorrect(int j, int i) {
        return i >= 0 && i < mapWidth && j >= 0 && j < mapHeight;
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
        playerEntity.move(params);
    }

    public boolean isPassability(Vec2d cell){
        boolean result = false;
        if(isPositionCorrect((int)cell.x, (int)cell.y)) {
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

        String response = ResponseConstructor.getResponse(ResponseHeaders.AVAILABLE_ACTIONS, availableActions.toString());
        userProfile.addMessageForSending(response);
    }

    public void stepping(){
        String responseStatusFlag = ResponseConstructor.getResponse(ResponseHeaders.FLAG_STATUS, flag.getStatus());
        for (Map.Entry<UserProfile, Entity> entry : entities.entrySet())
        {
            sendPlayersPosition(entry.getKey());
            sendEntityInViewArea(entry.getKey());
            sendAvailableActions(entry.getKey());
            sendEntityStatus(entry.getKey());
            entry.getValue().stepping(entry.getKey());
            entry.getKey().addMessageForSending(responseStatusFlag);
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
            Repairer.getInstance().repaireGameMap(this);
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
            Repairer.getInstance().repaireGameMap(this);
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

    public void setPlayerTarget(UserProfile userProfile, int relative_x, int relative_y){
        Entity playerEntity = entities.get(userProfile);
        relative_x = (int)playerEntity.getCoord().x - VIEW_WIDTH_2 + relative_x;
        relative_y = (int)playerEntity.getCoord().y - VIEW_HEIGHT_2 + relative_y;
        if(isPositionCorrect(relative_y, relative_x)){
            if(entityLocation[relative_x][relative_y] != null){
                playerEntity.setTarget(entityLocation[relative_x][relative_y]);
            }
        }
    }

    public void useAbility(UserProfile userProfile, String abilityName){
        Entity playerEntity = entities.get(userProfile);
        playerEntity.useAbility(abilityName);
    }

    private void sendEntityStatus(UserProfile userProfile){
        Entity playerEntity = entities.get(userProfile);

        String response;
        if(playerEntity.isHaveTarget()){
            response = ResponseConstructor.entityStatus(playerEntity.getHitPoints(), true, playerEntity.getTargetsHitPoints());
        } else {
            response = ResponseConstructor.entityStatus(playerEntity.getHitPoints(), false,0);
        }
        response = ResponseConstructor.getResponse(ResponseHeaders.ENTITY_STATUS, response);
        userProfile.addMessageForSending(response);
    }
}
