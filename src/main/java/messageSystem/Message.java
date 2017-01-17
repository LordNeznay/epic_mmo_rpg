package messagesystem;

/**
 * Created by Андрей on 20.12.2015.
 */
public abstract class Message {
    private final Address from;
    private final Address to;

    public Message(Address from, Address to){
        this.from = from;
        this.to = to;
    }

    public Address getFrom(){
        return from;
    }

    public Address getTo(){
        return to;
    }

    public abstract void exec(Abonent abonent);
}
