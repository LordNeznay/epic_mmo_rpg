package frontend;

import main.AccountService;
import main.UserProfile;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by uschsh on 21.09.15.
 */
public class ExitServlet extends HttpServlet {
    private AccountService accountService;

    public ExitServlet(AccountService accService) {
        this.accountService = accService;
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        String sesseionId = null;
        if(session != null) {
            sesseionId = session.getId();
        }else
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        if((sesseionId != null) && (accountService != null) && (accountService.getSessions(sesseionId) != null)) {
            accountService.removeUser(sesseionId);
        }
    }
}
