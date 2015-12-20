package frontend;

import main.TimeHelper;
import org.jetbrains.annotations.NotNull;
import resource.ServerConfiguration;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class SignUpServlet extends HttpServlet {
    @NotNull private Frontend frontend;
    private static final int RESPONSE_TIME = ServerConfiguration.getInstance().getServletResponseTime();

    public SignUpServlet(@NotNull Frontend frontend) {
        this.frontend = frontend;
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        String session = request.getSession(true).getId();

        if((name != null) && (password != null)) {
            frontend.isUserExist(session, name);

            while (!frontend.isResivedResponseExistUser(session)){
                TimeHelper.sleep(RESPONSE_TIME);
            }

            Map<String, Object> pageVariables = new HashMap<>();
            if (!frontend.getResponseExistUser(session)) {
                frontend.registerUser(name, password);
                pageVariables.put("errors", "null");
            } else {
                pageVariables.put("errors", "User with name '" + name + "' already exists");
            }

            response.getWriter().println(PageGenerator.getPage("signupResult.json", pageVariables));
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }




}
