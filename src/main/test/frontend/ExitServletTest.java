package frontend;

import dbservice.DBService;
import main.AccountService;
import main.UserProfile;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by uschsh on 02.11.15.
 */
public class ExitServletTest {
    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private HttpSession session = mock(HttpSession.class);

    private static UserProfile s_testFirstUser = new UserProfile("testLogin1", "testPassword1", "testEmail1");
    private static UserProfile s_testSecondUser = new UserProfile("testLogin2", "testPassword2", "testEmail2");
    private static DBService dbService;
    private static AccountService s_accountService;
    private static String s_sessionIdSecond = "sessionidsecond";

    @BeforeClass
    public static void setBefore() throws IOException {
        dbService   =   new DBService("test");
        s_accountService = new AccountService(dbService);
        s_accountService.addUser(s_testFirstUser.getLogin(), s_testFirstUser);
        s_accountService.addUser(s_testSecondUser.getLogin(), s_testSecondUser);

        s_accountService.addSession("session", s_testFirstUser);

        s_accountService.addSession(s_sessionIdSecond, s_testSecondUser);
    }

    @AfterClass
    public static void after() throws IOException {
        dbService.shutdown();
    }

    @Before
    public void setUp() throws IOException {
        when(request.getSession(true)).thenReturn(session);
        when(session.getId()).thenReturn(s_sessionIdSecond);

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
        ExitServlet exitServlet = new ExitServlet(s_accountService);

        assertEquals(s_accountService.getAuthUsersNumber(), 2);

        try {
            exitServlet.doPost(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        assertEquals(s_accountService.getAuthUsersNumber(), 1);
    }
}