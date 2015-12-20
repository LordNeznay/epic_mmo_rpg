package frontend;

import messageSystem.Address;

/**
 * Created by Андрей on 20.12.2015.
 */
public class MessageIsUserExisted extends MessageToFrontend {
    private String sessionId;
    private boolean isUserExist;

    public MessageIsUserExisted(Address from, Address to, String sessionId, boolean isUserExist){
        super(from, to);
        this.sessionId = sessionId;
        this.isUserExist = isUserExist;
    }

    @Override
    protected void exec(Frontend frontend) {
        frontend.addResponseExistUser(sessionId, isUserExist);
    }
}
