package mechanics.messages;

import main.UserProfile;
import mechanics.GameMechanics;
import messagesystem.Address;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageMovePlayer  extends MessageToGameMechanics {
    UserProfile userProfile;
    String direction;

    public MessageMovePlayer(Address from, Address to, UserProfile userProfile, String direction){
        super(from, to);
        this.userProfile = userProfile;
        this.direction = direction;
    }

    @Override
    protected void exec(GameMechanics gameMechanics){
        gameMechanics.movePlayer(userProfile, direction);
    }
}
