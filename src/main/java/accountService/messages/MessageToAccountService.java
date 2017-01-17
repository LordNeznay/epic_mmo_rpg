package accountservice.messages;

import accountservice.AccountService;
import messagesystem.Abonent;
import messagesystem.Address;
import messagesystem.Message;

/**
 * Created by Андрей on 20.12.2015.
 */
public abstract class MessageToAccountService extends Message {
    MessageToAccountService(Address from, Address to){
        super(from, to);
    }

    @Override
    public final void exec(Abonent abonent) {
        if (abonent instanceof AccountService) {
            exec((AccountService) abonent);
        }
    }

    protected abstract void exec(AccountService service);
}
