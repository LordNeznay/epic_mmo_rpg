package mechanics.messages;

import main.UserProfile;
import mechanics.GameMechanics;
import messagesystem.Address;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageSetPlayerTarget extends MessageToGameMechanics {
    UserProfile userProfile;
    int x;
    int y;

    public MessageSetPlayerTarget(Address from, Address to, UserProfile userProfile, int x, int y){
        super(from, to);
        this.userProfile = userProfile;
        this.x = x;
        this.y = y;
    }

    @Override
    protected void exec(GameMechanics gameMechanics){
        gameMechanics.setPlayerTarget(userProfile, x, y);
    }
}