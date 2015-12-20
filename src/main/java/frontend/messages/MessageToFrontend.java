package frontend.messages;

import frontend.Frontend;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;

/**
 * Created by Андрей on 20.12.2015.
 */
public abstract class MessageToFrontend extends Message {
    public MessageToFrontend(Address from, Address to){
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof Frontend) {
            exec((Frontend) abonent);
        }
    }

    protected abstract void exec(Frontend frontEnd);
}
