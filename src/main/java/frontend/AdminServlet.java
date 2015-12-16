package frontend;

import dbservice.DBService;
import main.AccountService;
import main.TimeHelper;
import mechanics.GameMechanics;
import org.jetbrains.annotations.NotNull;
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
    private GameMechanics gameMechanics;
    private DBService dbservice;
    public AdminServlet(AccountService accService, GameMechanics gameMechanics, DBService dbservice) {

        this.accountService = accService;
        this.gameMechanics = gameMechanics;
        this.dbservice = dbservice;
    }

    @Override
    protected void doGet(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {

        String shutdown_time = request.getParameter("shutdown");

        if(shutdown_time != null) {
            int time = Integer.valueOf(shutdown_time);
            System.out.print("Server will be down after: " + time + " ms");
            gameMechanics.stop();
            dbservice.shutdown();
            TimeHelper.sleep(time);
            System.out.print("\nShutdown");
            System.exit(0);
        }

        if(accountService !=null) {
            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put("auth_users",Long.toString(accountService.getAuthUsersNumber()));
            pageVariables.put("reg_users", Long.toString(accountService.getRegUsersNumber()));

            response.getWriter().println(PageGenerator.getPage("adminform.html", pageVariables));
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
