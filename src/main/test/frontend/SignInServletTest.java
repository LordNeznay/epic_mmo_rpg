package frontend;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by uschsh on 01.11.15.
 */
public class SignInServletTest {
    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private HttpSession session = mock(HttpSession.class);
    private static Frontend s_frontend = mock(Frontend.class);
    private StringWriter stringWriter;

    @Before
    public void setUp() throws IOException {
        when(request.getSession(true)).thenReturn(session);
        when(request.getParameter("name")).thenReturn("name");
        when(request.getParameter("password")).thenReturn("password");

        when(session.getId()).thenReturn("sessionid");


        stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        try {
            when(response.getWriter()).thenReturn(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        SignInServlet signIn = new SignInServlet(s_frontend);
        when(s_frontend.isAuthenticated(anyString())).thenReturn(false);

        try {
            signIn.doGet(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        assert stringWriter.toString().contains("false");
    }

    @Test
    public void testNotExistingUser() throws IOException {
        when(s_frontend.isResivedResponseAuthorization(anyString())).thenReturn(true);
        when(s_frontend.getResponseAuthorization(anyString())).thenReturn(false);
        SignInServlet signIn = new SignInServlet(s_frontend);

        try {
            signIn.doPost(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        assert stringWriter.toString().contains("Wrong login");

    }

    @Test
    public void testExistingUser() throws IOException {
        when(s_frontend.isResivedResponseAuthorization(anyString())).thenReturn(true);
        when(s_frontend.getResponseAuthorization(anyString())).thenReturn(true);

        SignInServlet signIn = new SignInServlet(s_frontend);

        try {
            signIn.doPost(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        assert stringWriter.toString().contains("null");

    }


}