package frontend;

import dbservice.DBService;
import main.AccountService;
import main.UserProfile;
import mechanics.GameMechanics;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by uschsh on 02.11.15.
 */
public class AdminServletTest {
    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private static DBService dbService;
    private static GameMechanics gameMechanics = mock(GameMechanics.class);
    private static AccountService s_accountService;

    @BeforeClass
    public static void setBefore() throws IOException {
        dbService   =   new DBService("test");
        s_accountService = new AccountService(dbService);
        s_accountService.addUser("firstUser", new UserProfile("firstUser", "password", "email"));
        s_accountService.addUser("secondUser", new UserProfile("secondUser", "password", "email"));
    }

    @AfterClass
    public static void after() throws IOException {
        dbService.shutdown();
    }


    @Before
    public void setUp() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        try {
            when(response.getWriter()).thenReturn(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoGet() throws IOException {
        AdminServlet adminServlet = new AdminServlet(s_accountService, gameMechanics, dbService);

        try {
            adminServlet.doGet(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        assertEquals(s_accountService.getAuthUsersNumber(), 0);
        assertEquals(s_accountService.getRegUsersNumber(), 2);
    }

    @Test
    public void testDoGetStopServer() throws IOException {
        //when(request.getParameter("shutdown")).thenReturn("100");

        AdminServlet adminServlet = new AdminServlet(s_accountService, gameMechanics, dbService);

        try {
            adminServlet.doGet(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}