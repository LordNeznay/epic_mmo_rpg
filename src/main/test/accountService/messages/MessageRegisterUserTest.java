package accountservice.messages;

import accountservice.AccountService;
import frontend.messages.MessageIsAuthenticated;
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
public class MessageRegisterUserTest {
    MessageSystem messageSystem = mock(MessageSystem.class);
    AccountService accountService = mock(AccountService.class);
    AddressService addressService = mock(AddressService.class);
    Address from = new Address();
    Address to = new Address();

    String name = "name";
    String password = "password";

    @Before
    public void setUp(){
        when(addressService.getFrontendAddress()).thenReturn(to);
        when(messageSystem.getAddressService()).thenReturn(addressService);
        when(accountService.getAddress()).thenReturn(from);
        when(accountService.getMessageSystem()).thenReturn(messageSystem);
    }

    @Test
    public void testExec() throws Exception {
        Message ms = new MessageRegisterUser(from, to, name, password);
        ms.exec(accountService);
        verify(accountService, times(1)).registerUser(name,password);
    }
}