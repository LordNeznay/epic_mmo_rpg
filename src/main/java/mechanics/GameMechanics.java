package mechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.AccountService;
import main.UserProfile;
/**
 * Created by uschsh on 26.10.15.
 */
public class GameMechanics {
    private static final int MIN_PLAYERS_FOR_START = 2;
    private AccountService accountService;
    private Map<UserProfile, GameMap> usersMaps = new HashMap<UserProfile, GameMap>();
    private ArrayList<UserProfile> userQueue = new ArrayList<UserProfile>();
    private ArrayList<GameMap> gameMaps = new ArrayList<GameMap>();

    public void addUser(UserProfile userProfile) {
        userQueue.add(userProfile);

        if(userQueue.size() == MIN_PLAYERS_FOR_START) {
            GameMap gameMap = new GameMap();
            gameMaps.add(gameMap);
            for(int i = 0; i < userQueue.size(); i++) {
                usersMaps.put(userQueue.get(i), gameMap);
                gameMap.addUser(userQueue.get(i));
            }
            userQueue.clear();
        }
    }

}
