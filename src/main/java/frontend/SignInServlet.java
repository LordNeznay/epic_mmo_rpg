package frontend;

import main.AccountService;
import main.UserProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    @Nullable private AccountService accountService;

    public SignInServlet(@Nullable AccountService accService) {
        this.accountService = accService;
    }

    @Override
    protected void doGet(@NotNull HttpServletRequest request,@NotNull HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        String sesseionId = null;
        if(session != null) {
            sesseionId = session.getId();
        }else
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);



        if((accountService != null) && (sesseionId != null)) {
            Map<String, Object> pageVariables = new HashMap<>();
            if (accountService.getSessions(sesseionId) == null) {
                pageVariables.put("loginStatus", "Enter login/password");
                response.getWriter().println(PageGenerator.getPage("signinform.html", pageVariables));
            } else {
                pageVariables.put("status", "ok");
                response.getWriter().println(PageGenerator.getPage("exitform.html", pageVariables));
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        if((name != null) && (password != null)) {

            if(accountService != null) {
                Map<String, Object> pageVariables = new HashMap<>();
                UserProfile profile = accountService.getUser(name);


                if (profile != null && profile.getPassword().equals(password)) {
                    String session = request.getSession().getId().toString();
                    accountService.addSessions(session, profile);
                    pageVariables.put("status", "ok");
                    response.getWriter().println(PageGenerator.getPage("exitform.html", pageVariables));
                } else {
                    pageVariables.put("loginStatus", "Wrong login/password");
                    response.getWriter().println(PageGenerator.getPage("signinform.html", pageVariables));
                }
                response.setStatus(HttpServletResponse.SC_OK);
            }

        }else
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
