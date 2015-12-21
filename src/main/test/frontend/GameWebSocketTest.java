package frontend;

import main.UserProfile;
import mechanics.messages.*;
import messageSystem.Address;
import messageSystem.AddressService;
import messageSystem.MessageSystem;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Андрей on 21.12.2015.
 */
public class GameWebSocketTest {
    MessageSystem messageSystem = mock(MessageSystem.class);
    Frontend frontend = mock(Frontend.class);
    AddressService addressService = mock(AddressService.class);
    GameWebSocket socket;
    UserProfile user = new UserProfile("login", "password", "email");
    Session session = mock(Session.class);
    Address frontendAddress = new Address();
    Address gameMechanicsAddress = new Address();

    @Before
    public void setUp(){
        when(addressService.getGameMechanicsAddress()).thenReturn(gameMechanicsAddress);
        when(messageSystem.getAddressService()).thenReturn(addressService);
        when(frontend.getAddress()).thenReturn(frontendAddress);
        when(frontend.getMessageSystem()).thenReturn(messageSystem);
        socket = new GameWebSocket(user, frontend);
    }

    @Test
    public void testGetUser() throws Exception {
        assertEquals(socket.getUser(), user);
    }

    @Test
    public void testOnMessage() throws Exception {

        String message = "{\"command\": \"join_game\"}";
        socket.onMessage(message);
        verify(messageSystem, times(1)).sendMessage(any(MessageAddUserInQueue.class));

        message = "{\"command\": \"leave_game\"}";
        socket.onMessage(message);
        verify(messageSystem, times(2)).sendMessage(any(MessageRemoveUserFromGame.class));

        message = "{\"command\": \"action\", \"action\" : \"move\", \"direction\" : \"left\"}";
        socket.onMessage(message);
        verify(messageSystem, times(3)).sendMessage(any(MessageMovePlayer.class));

        message = "{\"command\": \"action\", \"action\" : \"setTarget\", \"x\" : 1, \"y\": 1}";
        socket.onMessage(message);
        verify(messageSystem, times(4)).sendMessage(any(MessageSetPlayerTarget.class));

        message = "{\"command\": \"action\", \"action\" : \"useAbility\", \"abilityName\" : \"NoneEffectAbility\"}";
        socket.onMessage(message);
        verify(messageSystem, times(5)).sendMessage(any(MessageUseAbility.class));

        message = "{\"command\": \"action\", \"action\" : \"flagCapture\"}";
        socket.onMessage(message);
        verify(messageSystem, times(6)).sendMessage(any(MessageStartFlagCapture.class));
    }

    @Test
    public void testOnOpen() throws Exception {
        socket.onOpen(session);
        assertEquals(socket.getSession(), session);
    }

    @Test
    public void testSetGetSession() throws Exception {
        socket.setSession(session);
        assertEquals(socket.getSession(), session);
    }

}