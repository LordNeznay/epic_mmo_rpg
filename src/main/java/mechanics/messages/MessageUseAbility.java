package mechanics.messages;

import main.UserProfile;
import mechanics.GameMechanics;
import messageSystem.Address;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageUseAbility extends MessageToGameMechanics {
    UserProfile userProfile;
    String abilityName;

    public MessageUseAbility(Address from, Address to, UserProfile userProfile, String abilityName){
        super(from, to);
        this.userProfile = userProfile;
        this.abilityName = abilityName;
    }

    @Override
    protected void exec(GameMechanics gameMechanics){
        gameMechanics.useAbility(userProfile, abilityName);
    }
}