package frontend.messages;

import frontend.Frontend;
import main.UserProfile;
import messagesystem.Address;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Андрей on 20.12.2015.
 */
public class MessageIsAuthenticated extends MessageToFrontend {
    private String sessionId;
    private UserProfile userProfile;

    public MessageIsAuthenticated(Address from, Address to, String sessionId, @Nullable UserProfile userProfile){
        super(from, to);
        this.sessionId = sessionId;
        this.userProfile = userProfile;
    }

    @Override
    protected void exec(Frontend frontend) {
        frontend.authenticated(sessionId, userProfile);
        frontend.addResponseAuthorization(sessionId, userProfile!=null);
    }
}
