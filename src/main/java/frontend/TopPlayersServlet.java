package frontend;

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
 * Created by uschsh on 25.11.15.
 */
public class TopPlayersServlet extends HttpServlet {

    @Override
    protected void doGet(@NotNull HttpServletRequest request,@NotNull HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        response.getWriter().println(PageGenerator.getPage("topPlayers.json", pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
