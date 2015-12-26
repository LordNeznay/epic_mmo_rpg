package mechanics.messages;

import main.UserProfile;
import mechanics.GameMechanics;
import messageSystem.Address;

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
        System.out.print("User was excluded " + userProfile.getLogin() + '\n');
    }
}
