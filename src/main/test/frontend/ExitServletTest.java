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
 * Created by uschsh on 02.11.15.
 */
public class ExitServletTest {
    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private HttpSession session = mock(HttpSession.class);

    private static Frontend s_frontend = mock(Frontend.class);


    @Before
    public void setUp() throws IOException {
        when(request.getSession(true)).thenReturn(session);

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
        ExitServlet exitServlet = new ExitServlet(s_frontend);


        try {
            exitServlet.doPost(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        verify(s_frontend, times(1)).removeUser(anyString());
    }
}