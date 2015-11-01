package mechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.AccountService;
import main.UserProfile;
import utils.TimeHelper;

/**
 * Created by uschsh on 26.10.15.
 */
public class GameMechanics {
    private static final int STEP_TIME = 100;
    private static final int MIN_PLAYERS_FOR_START = 1;
    private AccountService accountService;
    private Map<UserProfile, GameMap> usersMaps = new HashMap<UserProfile, GameMap>();
    private ArrayList<UserProfile> userQueue = new ArrayList<UserProfile>();
    private ArrayList<GameMap> gameMaps = new ArrayList<GameMap>();

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

        userQueue.add(userProfile);
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
            mapWithUser.removeUser(userProfile);
        } catch(RuntimeException e){
            e.printStackTrace();
        }
        usersMaps.remove(userProfile);
    }

    public void gameAction(UserProfile userProfile, String action, String params){
        try {
            GameMap mapWithUser = usersMaps.get(userProfile);
            mapWithUser.gameAction(userProfile, action, params);
        } catch(RuntimeException e){
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            stepping();
            TimeHelper.sleep(STEP_TIME);
        }
    }

    private void stepping(){
        gameMaps.forEach(GameMap::stepping);
    }

    public void movePlayer(UserProfile userProfile, String params){
        usersMaps.get(userProfile).entityMove(userProfile, params);
    }
}
