package accountservice.messages;

import accountservice.AccountService;
import frontend.messages.MessageIsAuthenticated;
import frontend.messages.MessageIsUserExisted;
import messagesystem.Address;
import messagesystem.AddressService;
import messagesystem.Message;
import messagesystem.MessageSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageIsUserExistTest {
    MessageSystem messageSystem = mock(MessageSystem.class);
    AccountService accountService = mock(AccountService.class);
    AddressService addressService = mock(AddressService.class);
    Address from = new Address();
    Address to = new Address();

    String name = "name";
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
        when(accountService.isExistUser(anyString())).thenReturn(true);

        Message ms = new MessageIsUserExist(from, to, sessionId, name);
        ms.exec(accountService);
        verify(accountService, times(1)).isExistUser(name);
        verify(messageSystem, times(1)).sendMessage(any(MessageIsUserExisted.class));
    }
}