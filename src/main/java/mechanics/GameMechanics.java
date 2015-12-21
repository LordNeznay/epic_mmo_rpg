package mechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.UserProfile;
import mechanics.gameMap.GameMap;
import mechanics.gameMap.messages.*;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import messageSystem.MessageSystem;
import resource.ServerConfiguration;
import utils.ResponseConstructor;
import utils.ResponseHeaders;

/**
 * Created by uschsh on 26.10.15.
 */
public class GameMechanics implements Abonent, Runnable {
    private final Address address = new Address();
    private final MessageSystem messageSystem;
    private static final int STEP_TIME = ServerConfiguration.getInstance().getStepTime();
    private static final int MIN_PLAYERS_FOR_START = ServerConfiguration.getInstance().getPlayerToStart();
    private static final int MAX_PLAYER_IN_GAME = ServerConfiguration.getInstance().getAmountPlayerInCommand()*2;
    private Map<UserProfile, Address> usersMaps = new HashMap<>();
    private Map<Address, Integer> amountPlayerInMaps = new HashMap<>();
    private ArrayList<UserProfile> userQueue = new ArrayList<>();

    private volatile boolean isWorked = false;

    public GameMechanics(MessageSystem messageSystem){
        System.out.print("GameMechanics-server was started\n");
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

    public void addUser(UserProfile userProfile) {
        if(usersMaps.containsKey(userProfile)) {
            return;
        }

        for (Map.Entry<Address, Integer> entry : amountPlayerInMaps.entrySet()){
            if(entry.getValue() < MAX_PLAYER_IN_GAME){
                Message messageAddUserInGameMap = new MessageAddUserInGameMap(address, entry.getKey(), userProfile);
                messageSystem.sendMessage(messageAddUserInGameMap);
                usersMaps.put(userProfile, entry.getKey());
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
            GameMap gameMap = new GameMap(messageSystem);
            Address addressMap = gameMap.getAddress();
            Thread gameMapThread = new Thread(gameMap);
            gameMapThread.setDaemon(true);
            gameMapThread.start();

            amountPlayerInMaps.put(addressMap, userQueue.size());
            for (UserProfile anUserQueue : userQueue) {
                Message messageAddUserInGameMap = new MessageAddUserInGameMap(address, addressMap, anUserQueue);
                messageSystem.sendMessage(messageAddUserInGameMap);
                usersMaps.put(anUserQueue, addressMap);
            }
            userQueue.clear();
        }
    }

//    @TestOnly
//    public GameMap getMapWithUser(UserProfile userProfile){
//        return usersMaps.get(userProfile);
//    }

//    @TestOnly
//    public void replaseMap(GameMap lastMap, GameMap newMap){
//        gameMaps.remove(lastMap);
//        gameMaps.add(newMap);
//        for(Map.Entry<UserProfile, GameMap> entry : usersMaps.entrySet()){
//            if(entry.getValue().equals(lastMap)){
//                entry.setValue(newMap);
//            }
//        }
//    }

    public boolean isInGame(){
        return isWorked;
    }

    public int getMinPlayersForStart(){
        return MIN_PLAYERS_FOR_START;
    }

    public int getAmountMap(){
        return amountPlayerInMaps.size();
    }

    public int getAmountPlayerInGame(){
        return usersMaps.size();
    }

    public int getAmountPlayerInQueue(){
        return userQueue.size();
    }

    public void removeUser(UserProfile userProfile){
        if(userQueue.contains(userProfile)){
            userQueue.remove(userProfile);
            return;
        }

        Address mapWithUser = usersMaps.get(userProfile);
        if(mapWithUser == null)return;
        Message message = new MessageRemoveUserOnGameMap(address, mapWithUser, userProfile);
        messageSystem.sendMessage(message);
        usersMaps.remove(userProfile);
    }

    public void shutdown(){
        stop();
    }

    public void stop(){
        isWorked = false;
        for (Map.Entry<Address, Integer> entry : amountPlayerInMaps.entrySet()){
            Message message = new MessageDeleteGameMap(address, entry.getKey());
            messageSystem.sendMessage(message);
        }
    }

    @Override
    public void run() {
        isWorked = true;
        while (isWorked) {
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(STEP_TIME);
            } catch (InterruptedException e) {
                System.out.print("GameMechanics-server was shutdown\n");
                return;
            }
        }
        System.out.print("GameMechanics-server was shutdown\n");
    }

    public void removeMap(Address mapAddress){
        ArrayList<UserProfile> usersForRemove = new ArrayList<>();
        for(Map.Entry<UserProfile, Address> entry: usersMaps.entrySet()){
            if(mapAddress.equals(entry.getValue())){
                usersForRemove.add(entry.getKey());
            }
        }
        usersForRemove.forEach(usersMaps::remove);
        amountPlayerInMaps.remove(mapAddress);
        Message message = new MessageDeleteGameMap(address, mapAddress);
        messageSystem.sendMessage(message);
    }

    public void movePlayer(UserProfile userProfile, String params){
        Message message = new MessageMovePlayerOnGameMap(address, usersMaps.get(userProfile), userProfile, params);
        messageSystem.sendMessage(message);
    }

    public void startFlagCapture(UserProfile userProfile){
        Message message = new MessageStartFlagCaptureOnGameMap(address, usersMaps.get(userProfile), userProfile);
        messageSystem.sendMessage(message);
    }

    public void setPlayerTarget(UserProfile userProfile, int x, int y){
        Message message = new MessageSetPlayerTargetOnGameMap(address, usersMaps.get(userProfile), userProfile, x, y);
        messageSystem.sendMessage(message);
    }

    public void useAbility(UserProfile userProfile, String abilityName){
        Message message = new MessageUseAbilityOnGameMap(address, usersMaps.get(userProfile), userProfile, abilityName);
        messageSystem.sendMessage(message);
    }
}
