package mechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import main.UserProfile;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import org.jetbrains.annotations.TestOnly;
import org.json.simple.JSONObject;
import resource.ServerConfiguration;
import utils.Repairer;
import utils.ResponseConstructor;
import utils.ResponseHeaders;
import utils.TimeHelper;

/**
 * Created by uschsh on 26.10.15.
 */
public class GameMechanics implements Abonent, Runnable {
    private final Address address = new Address();
    private final MessageSystem messageSystem;
    private static final int STEP_TIME = ServerConfiguration.getInstance().getStepTime();
    private static final int MIN_PLAYERS_FOR_START = ServerConfiguration.getInstance().getPlayerToStart();
    private Map<UserProfile, GameMap> usersMaps = new HashMap<>();
    private ArrayList<UserProfile> userQueue = new ArrayList<>();
    private ArrayList<GameMap> gameMaps = new ArrayList<>();
    private boolean isWorked = false;

    public GameMechanics(MessageSystem messageSystem){
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().registerGameMechanics(this);
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public Address getAddress(){
        return address;
    }

    @TestOnly
    public GameMap getMapWithUser(UserProfile userProfile){
        return usersMaps.get(userProfile);
    }

    @TestOnly
    public void replaseMap(GameMap lastMap, GameMap newMap){
        gameMaps.remove(lastMap);
        gameMaps.add(newMap);
        for(Map.Entry<UserProfile, GameMap> entry : usersMaps.entrySet()){
            if(entry.getValue().equals(lastMap)){
                entry.setValue(newMap);
            }
        }
    }

    public boolean isInGame(){
        return isWorked;
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

        String response = ResponseConstructor.getResponse(ResponseHeaders.WAIT_START, "{}");
        userProfile.addMessageForSending(response);

        if(userQueue.size() == MIN_PLAYERS_FOR_START) {
//            GameMap gameMap = new GameMap(messageSystem);
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
        if(userQueue.contains(userProfile)){
            userQueue.remove(userProfile);
            return;
        }

        GameMap mapWithUser = usersMaps.get(userProfile);
        if(mapWithUser == null)return;
        mapWithUser.removeUser(userProfile);
        usersMaps.remove(userProfile);
        userProfile.sendMessage();
    }

    public void stop(){
        isWorked = false;
    }

    @Override
    public void run() {
        isWorked = true;
        while (isWorked) {
            messageSystem.execForAbonent(this);
            stepping();
            TimeHelper.sleep(STEP_TIME);
        }
    }

    @SuppressWarnings("unchecked")
    private void sendResultMap(GameMap map){
        String result = map.getResult();
        usersMaps.entrySet().stream().filter(entry -> map.equals(entry.getValue())).forEach(entry -> {
            JSONObject body = new JSONObject();
            body.put("gameResult", result);
            body.put("playerCommand", map.getUserCommand(entry.getKey()));
            String response = ResponseConstructor.getResponse(ResponseHeaders.GAME_RESULT, body.toString());
            entry.getKey().addMessageForSending(response);
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
        usersForRemove.forEach(this::removeUser);
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

        for(Map.Entry<UserProfile, GameMap> entry: usersMaps.entrySet()){
            entry.getKey().sendMessage();
        }
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
