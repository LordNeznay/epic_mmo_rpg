package frontend;

import main.AccountService;
import main.UserProfile;
import org.jetbrains.annotations.NotNull;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class SignUpServlet extends HttpServlet {
    private AccountService accountService;

    public SignUpServlet(AccountService accService) {
        this.accountService = accService;
    }

    @Override
    protected void doGet(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        response.getWriter().println(PageGenerator.getPage("signupform.html", pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }
    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        if((name != null) && (password != null)) {
            if(accountService != null) {
                Map<String, Object> pageVariables = new HashMap<>();
                if (accountService.addUser(name, new UserProfile(name, password, ""))) {
                    pageVariables.put("signUpStatus", "New user created");
                } else {
                    pageVariables.put("signUpStatus", "User with name: " + name + " already exists");
                }

                response.getWriter().println(PageGenerator.getPage("signupstatus.html", pageVariables));
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }else
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }




}
