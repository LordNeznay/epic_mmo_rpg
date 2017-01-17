package accountservice.messages;

import accountservice.AccountService;
import messagesystem.Address;

/**
 * Created by Андрей on 20.12.2015.
 */
public class MessageSignalShutdownAccountService extends MessageToAccountService {
    public MessageSignalShutdownAccountService(Address from, Address to) {
        super(from, to);

    }

    @Override
    protected void exec(AccountService service) {
        service.shutdown();
    }
}