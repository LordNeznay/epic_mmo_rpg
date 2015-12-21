package accountService.messages;

import accountService.AccountService;
import frontend.messages.MessageIsAuthenticated;
import main.UserProfile;
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
public class MessageAuthenticateTest {
    MessageSystem messageSystem = mock(MessageSystem.class);
    AccountService accountService = mock(AccountService.class);
    AddressService addressService = mock(AddressService.class);
    Address from = new Address();
    Address to = new Address();

    String name = "name";
    String password = "password";
    String sessionId = "sessionId";

    @Before
    public void setUp(){
        when(addressService.getFrontendAddress()).thenReturn(to);
        when(messageSystem.getAddressService()).thenReturn(addressService);
        when(accountService.getAddress()).thenReturn(from);
        when(accountService.getMessageSystem()).thenReturn(messageSystem);
    }

    @Test
    public void testExec() throws Exception {
        Message ms = new MessageAuthenticate(from, to, name, password, sessionId);
        ms.exec(accountService);
        verify(accountService, times(1)).authenticate(name,password);
        verify(messageSystem, times(1)).sendMessage(any(MessageIsAuthenticated.class));
    }
}