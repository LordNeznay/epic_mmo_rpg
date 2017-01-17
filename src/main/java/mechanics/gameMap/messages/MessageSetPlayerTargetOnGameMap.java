package mechanics.gamemap.messages;

import main.UserProfile;
import mechanics.gamemap.GameMap;
import messagesystem.Address;

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