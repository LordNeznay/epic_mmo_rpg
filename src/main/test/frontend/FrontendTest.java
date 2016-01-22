package frontend;

import accountservice.messages.MessageAuthenticate;
import accountservice.messages.MessageIsUserExist;
import accountservice.messages.MessageRegisterUser;
import accountservice.messages.MessageSignalShutdownAccountService;
import main.UserProfile;
import mechanics.messages.MessageSignalShutdownGameMechanics;
import messagesystem.Address;
import messagesystem.AddressService;
import messagesystem.MessageSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import resource.ServerConfiguration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Андрей on 21.12.2015.
 */
public class FrontendTest {
    MessageSystem messageSystem = mock(MessageSystem.class);
    Frontend frontend;
    AddressService addressService = mock(AddressService.class);
//    Address accountServiseStub = new Address();
    Address frontendAddress = new Address();

    @Before
    public void setUp() throws Exception {
//        when(addressService.getAccountServiceAddress()).thenReturn(accountServiseStub);
        when(messageSystem.getAddressService()).thenReturn(addressService);
        frontend = new Frontend(messageSystem, ServerConfiguration.getInstance().getPort());
        frontend = spy(frontend);
        when(frontend.getAddress()).thenReturn(frontendAddress);
    }

    @After
    public void close(){
        frontend.stop();
    }

    @Test
    public void testAuthenticatedGetRemoveUser() throws Exception {
        UserProfile testUser = new UserProfile("login", "password", "email");
        assertEquals(frontend.getAuthUsersNumber(), 0);
        frontend.authenticated("sessionId", testUser);
        assertEquals(frontend.getAuthUsersNumber(), 1);
        frontend.authenticated("sessionId2", null);
        assertEquals(frontend.getUserBySession("sessionId"), testUser);
        assertEquals(frontend.getAuthUsersNumber(), 1);
        frontend.removeUser("sessionId");
        assertEquals(frontend.getAuthUsersNumber(), 0);
    }

    @Test
    public void testAddGetResponseAuthorization() throws Exception {
        assertEquals(frontend.sizeResponseAuthorization(), 0);
        frontend.addResponseAuthorization("sessionIdTrue", true);
        frontend.addResponseAuthorization("sessionIdFalse", false);
        assertEquals(frontend.sizeResponseAuthorization(), 2);
        assertEquals(frontend.getResponseAuthorization("sessionIdTrue"), true);
        assertEquals(frontend.getResponseAuthorization("sessionIdFalse"), false);
        assertEquals(frontend.sizeResponseAuthorization(), 0);
    }

    @Test
    public void testIsResivedResponseAuthorization() throws Exception {
        frontend.addResponseAuthorization("sessionId1", true);
        assertTrue(frontend.isResivedResponseAuthorization("sessionId1"));
        assertFalse(frontend.isResivedResponseAuthorization("sessionId2"));
    }

    @Test
    public void testAuthenticate() throws Exception {
        frontend.authenticate("login", "password", "sessionId");
        verify(messageSystem, times(1)).sendMessage(any(MessageAuthenticate.class));
}

    @Test
    public void testAddGetResponseExistUser() throws Exception {
        assertEquals(frontend.sizeResponseExistUser(), 0);
        frontend.addResponseExistUser("sessionIdTrue", true);
        frontend.addResponseExistUser("sessionIdFalse", false);
        assertEquals(frontend.sizeResponseExistUser(), 2);
        assertEquals(frontend.getResponseExistUser("sessionIdTrue"), true);
        assertEquals(frontend.getResponseExistUser("sessionIdFalse"), false);
        assertEquals(frontend.sizeResponseExistUser(), 0);
    }

    @Test
    public void testIsResivedResponseExistUser() throws Exception {
        frontend.addResponseExistUser("sessionId1", true);
        assertTrue(frontend.isResivedResponseExistUser("sessionId1"));
        assertFalse(frontend.isResivedResponseExistUser("sessionId2"));
    }

    @Test
    public void testIsUserExist() throws Exception {
        frontend.isUserExist("session", "login");
        verify(messageSystem, times(1)).sendMessage(any(MessageIsUserExist.class));
    }

    @Test
    public void testRegisterUser() throws Exception {
        frontend.registerUser("login", "password");
        verify(messageSystem, times(1)).sendMessage(any(MessageRegisterUser.class));
    }

    @Test
    public void testSignalShutdown() throws Exception {
        frontend.signalShutdown();
        //times() здесь - общее количество отправденных сообщений
        verify(messageSystem, times(2)).sendMessage(any(MessageSignalShutdownAccountService.class));
        verify(messageSystem, times(2)).sendMessage(any(MessageSignalShutdownGameMechanics.class));
    }
}