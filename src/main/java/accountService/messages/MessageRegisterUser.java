package accountservice.messages;

import accountservice.AccountService;
import messagesystem.Address;

/**
 * Created by Андрей on 20.12.2015.
 */
public class MessageRegisterUser  extends MessageToAccountService {
    private String name;
    private String password;

    public MessageRegisterUser(Address from, Address to, String name, String password){
        super(from, to);
        this.name = name;
        this.password = password;
    }

    @Override
    protected void exec(AccountService service){
        service.registerUser(name, password);
    }
}
