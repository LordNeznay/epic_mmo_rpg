package frontend;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

/**
 * Created by uschsh on 02.11.15.
 */
public class AdminServletTest {
    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private static Frontend s_frontend = mock(Frontend.class);
    StringWriter stringWriter;

    @Before
    public void setUp() throws IOException {
        stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        try {
            when(response.getWriter()).thenReturn(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoGet() throws IOException {
        when(s_frontend.getAuthUsersNumber()).thenReturn(666);
        AdminServlet adminServlet = new AdminServlet(s_frontend);

        try {
            adminServlet.doGet(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        assert stringWriter.toString().contains("666");

    }

    @Test
    public void testDoGetStopServer() throws IOException {
        when(request.getParameter("shutdown")).thenReturn("100");

        AdminServlet adminServlet = new AdminServlet(s_frontend);

        try {
            adminServlet.doGet(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        verify(s_frontend, times(1)).signalShutdown();
    }
}