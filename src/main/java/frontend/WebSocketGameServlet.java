package frontend;

import javax.servlet.annotation.WebServlet;

import main.AccountService;
import mechanics.GameMechanics;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
/**
 * Created by uschsh on 25.10.15.
 */

@WebServlet(name = "WebSocketGameServlet", urlPatterns = { "/gameplay" })
public class WebSocketGameServlet extends WebSocketServlet {
    private final static int IDLE_TIME = 3 * 60 * 1000;
    AccountService accountService;
    private GameMechanics gameMechanics;

    public WebSocketGameServlet(AccountService accountService, GameMechanics gameMechanics) {
        this.accountService = accountService;
        this.gameMechanics = gameMechanics;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {

        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new GameWebSocketCreator(accountService, gameMechanics));
    }
}
