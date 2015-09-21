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
 * Created by uschsh on 21.09.15.
 */
public class ExitServlet extends HttpServlet {
    private AccountService accountService;

    public ExitServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String session = request.getSession().getId().toString();
        accountService.getSessions(session);
/*
        Map<String, Object> pageVariables = new HashMap<>();
        UserProfile profile = accountService.getUser(name);

        //Если профиль не нулевой и его пароль совпадает с введенным
        if (profile != null && profile.getPassword().equals(password)) {
            pageVariables.put("loginStatus", "Login passed");

            response.getWriter().println(PageGenerator.getPage("signinform.html", pageVariables));
        } else {
            pageVariables.put("loginStatus", "Wrong login/password");
            response.getWriter().println(PageGenerator.getPage("signinform.html", pageVariables));
        }

*/
    }
}
