package frontend;

import main.AccountService;
import main.UserProfile;
import mechanics.GameMechanics;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
/**
 * Created by uschsh on 25.10.15.
 */
public class GameWebSocketCreator implements WebSocketCreator {
    AccountService accountService;
    private GameMechanics gameMechanics;

    public GameWebSocketCreator(AccountService accountService,  GameMechanics gameMechanics) {
        this.accountService = accountService;
        this.gameMechanics = gameMechanics;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {

        String sessionId = req.getHttpServletRequest().getSession().getId();
        UserProfile userProfile = accountService.getUser(sessionId);

        return new GameWebSocket(userProfile, gameMechanics);
    }
}
