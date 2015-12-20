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

import static org.mockito.Mockito.*;

/**
 * Created by uschsh on 01.11.15.
 */
public class SignUpServletTest {

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
    public void testDoPost() throws ServletException, IOException {
        when(s_frontend.isResivedResponseExistUser(anyString())).thenReturn(true);
        when(s_frontend.getResponseExistUser(anyString())).thenReturn(false);

        SignUpServlet signUp = new SignUpServlet(s_frontend);
        try {
            signUp.doPost(request, response);
        } catch(ServletException e) {
            e.printStackTrace();
        }

        assert !stringWriter.toString().contains("already exists");
    }

    @Test
    public void testRegExisting() throws ServletException, IOException {
        when(s_frontend.isResivedResponseExistUser(anyString())).thenReturn(true);
        when(s_frontend.getResponseExistUser(anyString())).thenReturn(true);

        SignUpServlet signUp = new SignUpServlet(s_frontend);
        try {
            signUp.doPost(request, response);
        } catch(ServletException e) {
            e.printStackTrace();
        }

        assert stringWriter.toString().contains("already exists");
    }
}