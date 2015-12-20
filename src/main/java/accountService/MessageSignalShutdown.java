package accountService;

import messageSystem.Address;

/**
 * Created by Андрей on 20.12.2015.
 */
public class MessageSignalShutdown extends MessageToAccountService {
    public MessageSignalShutdown(Address from, Address to) {
        super(from, to);

    }

    @Override
    protected void exec(AccountService service) {
        service.shutdown();
    }
}