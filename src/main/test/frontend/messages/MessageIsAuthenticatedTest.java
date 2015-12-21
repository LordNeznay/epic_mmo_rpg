package frontend.messages;

import frontend.Frontend;
import main.UserProfile;
import messageSystem.Address;
import messageSystem.Message;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageIsAuthenticatedTest {
    Frontend frontend = mock(Frontend.class);
    UserProfile userProfile = new UserProfile("login", "password", "email");
    String sessionId = "sessionId";
    Address from = new Address();
    Address to = new Address();

    @Test
    public void testExec() throws Exception {
        Message ms = new MessageIsAuthenticated(from, to, sessionId, userProfile);
        ms.exec(frontend);
        verify(frontend, times(1)).authenticated(sessionId,userProfile);
        verify(frontend, times(1)).addResponseAuthorization(sessionId, true);

        ms = new MessageIsAuthenticated(from, to, sessionId, null);
        ms.exec(frontend);
        verify(frontend, times(1)).authenticated(sessionId,userProfile);
        verify(frontend, times(1)).addResponseAuthorization(sessionId, false);
    }
}