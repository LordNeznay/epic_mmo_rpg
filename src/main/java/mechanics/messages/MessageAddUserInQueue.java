package mechanics.messages;

import main.UserProfile;
import mechanics.GameMechanics;
import messagesystem.Address;

/**
 * Created by Андрей on 20.12.2015.
 */
public class MessageAddUserInQueue extends MessageToGameMechanics{
    private UserProfile userProfile;

    public MessageAddUserInQueue(Address from, Address to, UserProfile userProfile){
        super(from, to);
        this.userProfile = userProfile;
    }

    @Override
    protected void exec(GameMechanics service){
        service.addUser(userProfile);
    }
}
