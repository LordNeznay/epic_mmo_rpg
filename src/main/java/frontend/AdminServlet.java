package frontend;

import main.AccountService;
import main.TimeHelper;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by uschsh on 22.09.15.
 */
public class AdminServlet extends HttpServlet {
    private AccountService accountService;

    public AdminServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String shutdown_time = request.getParameter("shutdown");

        if(shutdown_time != null) {
            int time = Integer.valueOf(shutdown_time);
            System.out.print("Server will be down after: "+ time + " ms");
            TimeHelper.sleep(time);
            System.out.print("\nShutdown");
            System.exit(0);
        }

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("auth_users", accountService.getAuthUsersNumber() + "");
        pageVariables.put("reg_users", accountService.getRegUsersNumber() + "");

        response.getWriter().println(PageGenerator.getPage("adminform.html", pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
