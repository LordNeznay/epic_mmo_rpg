package mechanics.gameMap.messages;

import main.UserProfile;
import mechanics.gameMap.GameMap;
import messageSystem.Address;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageRemoveUserOnGameMap extends MessageToGameMap{
    UserProfile userProfile;

    public MessageRemoveUserOnGameMap(Address from, Address to, UserProfile userProfile){
        super(from, to);
        this.userProfile = userProfile;
    }

    @Override
    protected void exec(GameMap service){
        service.removeUser(userProfile);
        userProfile.sendMessage();
    }
}