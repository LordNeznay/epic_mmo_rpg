package mechanics.gameMap.messages;

import main.UserProfile;
import mechanics.gameMap.GameMap;
import messageSystem.Address;
import messageSystem.AddressService;
import messageSystem.Message;
import messageSystem.MessageSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageUseAbilityOnGameMapTest  {
    MessageSystem messageSystem = mock(MessageSystem.class);
    GameMap gameMap = mock(GameMap.class);
    AddressService addressService = mock(AddressService.class);
    Address from = new Address();
    Address to = new Address();

    UserProfile userProfile = new UserProfile("login", "password", "email");
    String abilityName = "NoneEffectAbility";

    @Before
    public void setUp(){
        when(addressService.getFrontendAddress()).thenReturn(to);
        when(messageSystem.getAddressService()).thenReturn(addressService);
        when(gameMap.getAddress()).thenReturn(from);
        when(gameMap.getMessageSystem()).thenReturn(messageSystem);
    }

    @Test
    public void testExec() throws Exception {
        Message ms = new MessageUseAbilityOnGameMap(from, to, userProfile, abilityName);
        ms.exec(gameMap);
        verify(gameMap, times(1)).useAbility(userProfile, abilityName);
    }
}