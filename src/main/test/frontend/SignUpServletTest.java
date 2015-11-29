package frontend;

import dbservice.DBService;
import main.AccountService;
import org.junit.Before;
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
    private DBService dbService = mock(DBService.class);
    private final AccountService accountService = new AccountService(dbService);
    private StringWriter stringWriter;

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

    @Test
    public void testDoPost() throws ServletException, IOException {
        SignUpServlet signUp = new SignUpServlet(accountService);
        try {
            signUp.doPost(request, response);
        } catch(ServletException e) {
            e.printStackTrace();
        }

        assertEquals(accountService.getRegUsersNumber(), 1);
    }

    @Test
    public void testRegExisting() throws ServletException, IOException {
        SignUpServlet signUp = new SignUpServlet(accountService);
        try {
            signUp.doPost(request, response);
            signUp.doPost(request, response);
        } catch(ServletException e) {
            e.printStackTrace();
        }

        assert stringWriter.toString().contains("already exists");
    }


}