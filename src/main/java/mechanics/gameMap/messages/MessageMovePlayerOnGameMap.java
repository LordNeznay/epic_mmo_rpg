package mechanics.gamemap.messages;

import main.UserProfile;
import mechanics.gamemap.GameMap;
import messagesystem.Address;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageMovePlayerOnGameMap extends MessageToGameMap{
    UserProfile userProfile;
    String direction;

    public MessageMovePlayerOnGameMap(Address from, Address to, UserProfile userProfile, String direction){
        super(from, to);
        this.userProfile = userProfile;
        this.direction = direction;
    }

    @Override
    protected void exec(GameMap service){
        service.entityMove(userProfile, direction);
    }
}