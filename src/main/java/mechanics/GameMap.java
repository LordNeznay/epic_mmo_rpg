package mechanics;

import main.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by uschsh on 26.10.15.
 */
public class GameMap {
    public static final int MAX_PLAYERS_IN_COMMAND = 1;

    private Map<UserProfile, Entity> commandA = new HashMap<UserProfile, Entity>();
    private Map<UserProfile, Entity> commandB = new HashMap<UserProfile, Entity>();
    private Map<UserProfile, Integer> commands = new HashMap<UserProfile, Integer>();
    private PhysMapJson physMap = new PhysMapJson();

    public boolean addUser(UserProfile userProfile) {
        if(commandA.size() == MAX_PLAYERS_IN_COMMAND && commandB.size() == MAX_PLAYERS_IN_COMMAND){
            return false;
        }
        Entity userEntity = new Entity();
        if(commandA.size() > commandB.size()){
            commandB.put(userProfile, userEntity);
            commands.put(userProfile, 2);

        } else {
            commandA.put(userProfile, userEntity);
            commands.put(userProfile, 1);
        }
        String temp = physMap.getArea(userEntity.getCoord());
        userProfile.getUserSocket().sendMessage(temp);
        return true;
    }

    public String getArea(UserProfile userProfile){
        Entity entity;
        switch (commands.get(userProfile)){
            case 1:
                entity = commandA.get(userProfile);
            case 2:
                entity = commandB.get(userProfile);
            default:
                entity = new Entity();
        }

        return physMap.getArea(entity.getCoord());
    }
}
