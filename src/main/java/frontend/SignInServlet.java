package frontend;

import main.AccountService;
import main.UserProfile;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 */
public class SignInServlet extends HttpServlet {
    private AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String session = request.getSession().getId().toString();

        Map<String, Object> pageVariables = new HashMap<>();
        if(accountService.getSessions(session) == null) {

            pageVariables.put("loginStatus", "Enter login/password");
            response.getWriter().println(PageGenerator.getPage("signinform.html", pageVariables));
        }else {
            pageVariables.put("status", "ok");
            response.getWriter().println(PageGenerator.getPage("exitform.html", pageVariables));
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        //Извлечение параметров
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();
        UserProfile profile = accountService.getUser(name);

        //Если профиль не нулевой и его пароль совпадает с введенным
        if (profile != null && profile.getPassword().equals(password)) {
            String session = request.getSession().getId().toString();
            accountService.addSessions(session, profile);
            pageVariables.put("status", "ok");
            response.getWriter().println(PageGenerator.getPage("exitform.html", pageVariables));
        } else {
            pageVariables.put("loginStatus", "Wrong login/password");
            response.getWriter().println(PageGenerator.getPage("signinform.html", pageVariables));
        }


    }
}
