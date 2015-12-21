package mechanics.gameMap.messages;

import main.UserProfile;
import mechanics.gameMap.GameMap;
import messageSystem.Address;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageUseAbilityOnGameMap extends MessageToGameMap{
    UserProfile userProfile;
    String abilityName;

    public MessageUseAbilityOnGameMap(Address from, Address to, UserProfile userProfile, String abilityName){
        super(from, to);
        this.userProfile = userProfile;
        this.abilityName = abilityName;
    }

    @Override
    protected void exec(GameMap service){
        service.useAbility(userProfile, abilityName);
    }
}
