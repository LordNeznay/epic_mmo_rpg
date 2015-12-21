package frontend;

import main.TimeHelper;
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
    @NotNull private Frontend frontend;

    public AdminServlet(@NotNull Frontend frontend) {
        this.frontend = frontend;
    }


    @Override
    protected void doGet(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {

        String shutdown_time = request.getParameter("shutdown");

        if(shutdown_time != null) {
            int time = Integer.valueOf(shutdown_time);
            System.out.print("Server will be down after: " + time + " ms\n");
            frontend.signalShutdown();
            TimeHelper.sleep(time);
        }


        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("auth_users", Long.toString(frontend.getAuthUsersNumber()));
        pageVariables.put("reg_users", "Когда-нибудь я придумаю, как красиво с помощью системы сообдений получить переменную с другого сервера, но это уже совсем другая история.");

        response.getWriter().println(PageGenerator.getPage("adminform.html", pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
