package frontend;

import main.UserProfile;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by uschsh on 25.10.15.
 */
public class GameWebSocketCreator implements WebSocketCreator {
    @NotNull
    private Frontend frontend;

    public GameWebSocketCreator(@NotNull Frontend frontend) {
        this.frontend = frontend;
    }

    @Nullable
    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {

        String sessionId = req.getHttpServletRequest().getSession().getId();
        UserProfile userProfile = frontend.getUserBySession(sessionId);
        if(userProfile != null) {
            GameWebSocket socket = new GameWebSocket(userProfile, frontend);
            userProfile.addSocket(socket);
            return socket;
        } else {
            return null;
        }
    }
}
