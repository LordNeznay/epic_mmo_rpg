package frontend;

import javax.servlet.annotation.WebServlet;

import accountService.AccountService;
import mechanics.GameMechanics;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Created by uschsh on 25.10.15.
 */

@WebServlet(name = "WebSocketGameServlet", urlPatterns = { "/gameplay" })
public class WebSocketGameServlet extends WebSocketServlet {
    @NotNull private Frontend frontend;
    private static final int IDLE_TIME = 3 * 60 * 1000;

    public WebSocketGameServlet(@NotNull Frontend frontend) {
        this.frontend = frontend;
    }

    @Override
    public void configure(WebSocketServletFactory factory){
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new GameWebSocketCreator(frontend));
    }
}
