package accountService.messages;

import accountService.AccountService;
import frontend.messages.MessageIsAuthenticated;
import main.UserProfile;
import messageSystem.Address;
import messageSystem.Message;

/**
 * Created by Андрей on 20.12.2015.
 */
public final class MessageAuthenticate extends MessageToAccountService {
    private String name;
    private String password;
    private String sessionId;

    public MessageAuthenticate(Address from, Address to, String name, String password, String sessionId){
        super(from, to);
        this.name = name;
        this.password = password;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(AccountService service){
        final UserProfile user = service.authenticate(name, password);
        final Message back = new MessageIsAuthenticated(getTo(), getFrom(), sessionId, user);
        service.getMessageSystem().sendMessage(back);
    }
}
