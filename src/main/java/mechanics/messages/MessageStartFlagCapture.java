package mechanics.messages;

import main.UserProfile;
import mechanics.GameMechanics;
import messageSystem.Address;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageStartFlagCapture extends MessageToGameMechanics {
    UserProfile userProfile;

    public MessageStartFlagCapture(Address from, Address to, UserProfile userProfile){
        super(from, to);
        this.userProfile = userProfile;
    }

    @Override
    protected void exec(GameMechanics gameMechanics){
        gameMechanics.startFlagCapture(userProfile);
    }
}