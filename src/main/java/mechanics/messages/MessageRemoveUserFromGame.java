package mechanics.messages;

import main.UserProfile;
import mechanics.GameMechanics;
import messagesystem.Address;

/**
 * Created by Андрей on 20.12.2015.
 */
public class MessageRemoveUserFromGame extends MessageToGameMechanics {
    UserProfile userProfile;

    public MessageRemoveUserFromGame(Address from, Address to, UserProfile userProfile){
        super(from, to);
        this.userProfile = userProfile;
    }

    @Override
    protected void exec(GameMechanics gameMechanics){
        gameMechanics.removeUser(userProfile);
    }
}
