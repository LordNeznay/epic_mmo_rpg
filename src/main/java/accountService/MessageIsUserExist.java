package accountService;

import frontend.MessageIsUserExisted;
import messageSystem.Address;
import messageSystem.Message;

/**
 * Created by Андрей on 20.12.2015.
 */
public class MessageIsUserExist extends MessageToAccountService {
    String sessionId;
    String login;

    public MessageIsUserExist(Address from, Address to, String sessionId, String login){
        super(from, to);
        this.sessionId = sessionId;
        this.login = login;
    }

    @Override
    protected void exec(AccountService service){
        final boolean isExist = service.isExistUser(login);
        final Message back = new MessageIsUserExisted(getTo(), getFrom(), sessionId, isExist);
        service.getMessageSystem().sendMessage(back);
    }
}
