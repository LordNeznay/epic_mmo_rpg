package mechanics.gamemap.messages;

import main.UserProfile;
import mechanics.gamemap.GameMap;
import messagesystem.Address;
import messagesystem.AddressService;
import messagesystem.Message;
import messagesystem.MessageSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageDeleteGameMapTest  {
    MessageSystem messageSystem = mock(MessageSystem.class);
    GameMap gameMap = mock(GameMap.class);
    AddressService addressService = mock(AddressService.class);
    Address from = new Address();
    Address to = new Address();

    UserProfile userProfile = new UserProfile("login", "password", "email");

    @Before
    public void setUp(){
        when(addressService.getFrontendAddress()).thenReturn(to);
        when(messageSystem.getAddressService()).thenReturn(addressService);
        when(gameMap.getAddress()).thenReturn(from);
        when(gameMap.getMessageSystem()).thenReturn(messageSystem);
    }

    @Test
    public void testExec() throws Exception {
        Message ms = new MessageDeleteGameMap(from, to);
        ms.exec(gameMap);
        verify(gameMap, times(1)).sendResultMap();
        verify(gameMap, times(1)).stop();
    }
}