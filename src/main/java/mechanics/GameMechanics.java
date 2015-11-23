package mechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.UserProfile;
import org.json.simple.JSONObject;
import resource.Configuration;
import utils.Repairer;
import utils.TimeHelper;

/**
 * Created by uschsh on 26.10.15.
 */
public class GameMechanics {
    private static final int STEP_TIME = Configuration.getInstance().getStepTime();
    private static final int MIN_PLAYERS_FOR_START = Configuration.getInstance().getPlayerToStart();
    private Map<UserProfile, GameMap> usersMaps = new HashMap<>();
    private ArrayList<UserProfile> userQueue = new ArrayList<>();
    private ArrayList<GameMap> gameMaps = new ArrayList<>();
    private boolean isGame = false;

    public boolean isInGame(){
        return isGame;
    }

    public int getMinPlayersForStart(){
        return MIN_PLAYERS_FOR_START;
    }

    public int getAmountMap(){
        return gameMaps.size();
    }

    public int getAmountPlayerInGame(){
        return usersMaps.size();
    }

    public int getAmountPlayerInQueue(){
        return userQueue.size();
    }

    public void addUser(UserProfile userProfile) {
        if(usersMaps.containsKey(userProfile)) {
            return;
        }

        for(GameMap map : gameMaps){
            if(map.isPlace()){
                map.addUser(userProfile);
                usersMaps.put(userProfile, map);
                return;
            }
        }

        if(userQueue.contains(userProfile)){
            return;
        }
        userQueue.add(userProfile);
        userProfile.sendMessage("{\"type\": \"Wait_start\"}");
        if(userQueue.size() == MIN_PLAYERS_FOR_START) {
            GameMap gameMap = new GameMap();
            gameMaps.add(gameMap);
            for (UserProfile anUserQueue : userQueue) {
                usersMaps.put(anUserQueue, gameMap);
                gameMap.addUser(anUserQueue);
            }
            userQueue.clear();
        }
    }

    public void removeUser(UserProfile userProfile){
        try {
            GameMap mapWithUser = usersMaps.get(userProfile);
            if(mapWithUser == null) return;
            mapWithUser.removeUser(userProfile);
            usersMaps.remove(userProfile);
        } catch(RuntimeException e){
            e.printStackTrace();
        }
    }

    public void start(){
        isGame = true;
        this.run();
    }

    public void stop(){

        isGame = false;
    }

    public void run() {
        while (isGame) {
            stepping();
            TimeHelper.sleep(STEP_TIME);
        }
    }

    @SuppressWarnings("unchecked")
    private void sendResultMap(GameMap map){
        String result = map.getResult();
        usersMaps.entrySet().stream().filter(entry -> map.equals(entry.getValue())).forEach(entry -> {
            JSONObject request = new JSONObject();
            request.put("type", "gameResult");
            request.put("gameResult", result);
            request.put("playerCommand", map.getUserCommand(entry.getKey()));
            entry.getKey().sendMessage(request.toString());
        });
        removeMap(map);
    }

    public void removeMap(GameMap map){
        ArrayList<UserProfile> usersForRemove = new ArrayList<>();
        for (Map.Entry<UserProfile, GameMap> entry : usersMaps.entrySet())
        {
            if(map.equals(entry.getValue())){
                usersForRemove.add(entry.getKey());
            }
        }
        usersForRemove.forEach(usersMaps::remove);
        usersForRemove.clear();
        gameMaps.remove(map);
    }

    private void stepping(){
        ArrayList<GameMap> mapsForClose = new ArrayList<>();
        for(GameMap map : gameMaps){
            map.stepping();
            if(map.getEnd()){
                mapsForClose.add(map);
            }
        }

        mapsForClose.forEach(this::sendResultMap);
        mapsForClose.clear();
    }

    public void movePlayer(UserProfile userProfile, String params){
        try {
            usersMaps.get(userProfile).entityMove(userProfile, params);
        } catch (RuntimeException e){
            Repairer.getInstance().repaireUser(userProfile);
        }
    }

    public void startFlagCapture(UserProfile userProfile){
        try {
            usersMaps.get(userProfile).startFlagCapture(userProfile);
        } catch (RuntimeException e){
            Repairer.getInstance().repaireUser(userProfile);
        }
    }

    public void setPlayerTarget(UserProfile userProfile, int x, int y){
        try {
            usersMaps.get(userProfile).setPlayerTarget(userProfile, x, y);
        } catch (RuntimeException e){
            Repairer.getInstance().repaireUser(userProfile);
        }
    }

    public void useAbility(UserProfile userProfile, String abilityName){
        try {
            usersMaps.get(userProfile).useAbility(userProfile, abilityName);
        } catch (RuntimeException e){
            Repairer.getInstance().repaireUser(userProfile);
        }
    }
}
