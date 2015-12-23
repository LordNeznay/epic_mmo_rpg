package frontend;

import main.TimeHelper;
import org.jetbrains.annotations.NotNull;
import resource.ServerConfiguration;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 */
public class SignInServlet extends HttpServlet {
    @NotNull private Frontend frontend;
    private static final int RESPONSE_TIME = ServerConfiguration.getInstance().getServletResponseTime();

    public SignInServlet(@NotNull Frontend frontend) {
        this.frontend = frontend;
    }

    @Override
    protected void doGet(@NotNull HttpServletRequest request,@NotNull HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        String sessionId = null;
        if(session != null) {
            sessionId = session.getId();
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


        if(sessionId != null) {
            Map<String, Object> pageVariables = new HashMap<>();
            if (!frontend.isAuthenticated(sessionId)){
                pageVariables.put("loginStatus", "false");
            } else {
                pageVariables.put("loginStatus", "true");
            }
            response.getWriter().println(PageGenerator.getPage("loginStatus.json", pageVariables));
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        if((name != null) && (password != null)) {
            Map<String, Object> pageVariables = new HashMap<>();

            String session = request.getSession(true).getId();
            frontend.authenticate(name, password, session);

            while (! frontend.isResivedResponseAuthorization(session)){
                TimeHelper.sleep(RESPONSE_TIME);
            }

            if (frontend.getResponseAuthorization(session)) {
                pageVariables.put("errors", "null");
                response.getWriter().println(PageGenerator.getPage("loginResult.json", pageVariables));
            } else {
                pageVariables.put("errors", "Wrong login/password");
                response.getWriter().println(PageGenerator.getPage("loginResult.json", pageVariables));
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
