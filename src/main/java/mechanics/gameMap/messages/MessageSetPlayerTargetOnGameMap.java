package mechanics.gameMap.messages;

import main.UserProfile;
import mechanics.gameMap.GameMap;
import messageSystem.Address;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageSetPlayerTargetOnGameMap extends MessageToGameMap{
    UserProfile userProfile;
    int x;
    int y;

    public MessageSetPlayerTargetOnGameMap(Address from, Address to, UserProfile userProfile, int x, int y){
        super(from, to);
        this.userProfile = userProfile;
        this.x = x;
        this.y = y;
    }

    @Override
    protected void exec(GameMap service){
        service.setPlayerTarget(userProfile, x, y);
    }
}