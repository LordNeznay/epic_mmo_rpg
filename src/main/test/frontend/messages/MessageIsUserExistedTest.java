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
public class MessageIsUserExistedTest {
    Frontend frontend = mock(Frontend.class);
    String sessionId = "sessionId";
    Address from = new Address();
    Address to = new Address();

    @Test
    public void testExec() throws Exception {
        Message ms = new MessageIsUserExisted(from, to, sessionId, true);
        ms.exec(frontend);
        verify(frontend, times(1)).addResponseExistUser(sessionId, true);

        ms = new MessageIsUserExisted(from, to, sessionId, false);
        ms.exec(frontend);
        verify(frontend, times(1)).addResponseExistUser(sessionId, false);
    }
}