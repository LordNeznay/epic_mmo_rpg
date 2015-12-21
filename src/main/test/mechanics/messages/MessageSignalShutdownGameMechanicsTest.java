package mechanics.messages;

import main.UserProfile;
import mechanics.GameMechanics;
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
public class MessageSignalShutdownGameMechanicsTest {
    MessageSystem messageSystem = mock(MessageSystem.class);
    GameMechanics mechanicService = mock(GameMechanics.class);
    AddressService addressService = mock(AddressService.class);
    Address from = new Address();
    Address to = new Address();

    @Before
    public void setUp(){
        when(addressService.getFrontendAddress()).thenReturn(to);
        when(messageSystem.getAddressService()).thenReturn(addressService);
        when(mechanicService.getAddress()).thenReturn(from);
        when(mechanicService.getMessageSystem()).thenReturn(messageSystem);
    }

    @Test
    public void testExec() throws Exception {
        Message ms = new MessageSignalShutdownGameMechanics(from, to);
        ms.exec(mechanicService);
        verify(mechanicService, times(1)).shutdown();
    }
}