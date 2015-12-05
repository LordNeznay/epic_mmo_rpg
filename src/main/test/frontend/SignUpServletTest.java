package frontend;

import dbservice.DBService;
import main.AccountService;
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

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by uschsh on 01.11.15.
 */
public class SignUpServletTest {

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private static DBService dbService;
    private static AccountService s_accountService;
    private StringWriter stringWriter;

    @BeforeClass
    public static void setBefore() throws IOException {
        dbService   =   new DBService("test");
        s_accountService =   new AccountService(dbService);
    }

    @Before
    public void setUp() throws IOException {
        when(request.getParameter("name")).thenReturn("name");
        when(request.getParameter("password")).thenReturn("password");

        stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        try {
            when(response.getWriter()).thenReturn(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void after() throws IOException {
        dbService.shutdown();
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        SignUpServlet signUp = new SignUpServlet(s_accountService);
        try {
            signUp.doPost(request, response);
        } catch(ServletException e) {
            e.printStackTrace();
        }

        assertEquals(s_accountService.getRegUsersNumber(), 1);
    }

    @Test
    public void testRegExisting() throws ServletException, IOException {
        SignUpServlet signUp = new SignUpServlet(s_accountService);
        try {
            signUp.doPost(request, response);
            signUp.doPost(request, response);
        } catch(ServletException e) {
            e.printStackTrace();
        }

        assert stringWriter.toString().contains("already exists");
    }


}