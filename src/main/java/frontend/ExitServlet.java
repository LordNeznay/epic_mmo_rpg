package frontend;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

/**
 * Created by uschsh on 21.09.15.
 */
public class ExitServlet extends HttpServlet {
    @NotNull private Frontend frontend;

    public ExitServlet(@NotNull Frontend frontend) {
        this.frontend = frontend;
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        String session = request.getSession(true).getId();
        frontend.removeUser(session);
    }
}
