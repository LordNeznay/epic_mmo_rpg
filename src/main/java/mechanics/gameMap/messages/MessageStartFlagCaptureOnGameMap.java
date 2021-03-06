package mechanics.gamemap.messages;

import main.UserProfile;
import mechanics.gamemap.GameMap;
import messagesystem.Address;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageStartFlagCaptureOnGameMap extends MessageToGameMap{
    UserProfile userProfile;

    public MessageStartFlagCaptureOnGameMap(Address from, Address to, UserProfile userProfile){
        super(from, to);
        this.userProfile = userProfile;
    }

    @Override
    protected void exec(GameMap service){
        service.startFlagCapture(userProfile);
    }
}
