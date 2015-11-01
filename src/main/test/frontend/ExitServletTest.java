package frontend;

import main.AccountService;
import main.UserProfile;
import org.junit.Before;
import org.junit.Test;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by uschsh on 02.11.15.
 */
public class ExitServletTest {
    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private HttpSession session = mock(HttpSession.class);

    private final UserProfile testFirstUser = new UserProfile("testLogin1", "testPassword1", "testEmail1");
    private final UserProfile testSecondUser = new UserProfile("testLogin2", "testPassword2", "testEmail2");

    private final AccountService accountService = new AccountService();

    @Before
    public void setUp() throws IOException {
        accountService.addUser(testFirstUser.getLogin(), testFirstUser);
        accountService.addUser(testSecondUser.getLogin(), testSecondUser);

        accountService.addSession(anyString(), testFirstUser);

        String sessionIdSecond = "sessionidsecond";
        accountService.addSession(sessionIdSecond, testSecondUser);

        when(request.getSession(true)).thenReturn(session);
        when(session.getId()).thenReturn(sessionIdSecond);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        try {
            when(response.getWriter()).thenReturn(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoPost() throws IOException {
        ExitServlet exitServlet = new ExitServlet(accountService);

        assertEquals(accountService.getAuthUsersNumber(), 2);

        try {
            exitServlet.doPost(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        assertEquals(accountService.getAuthUsersNumber(), 1);
    }
}