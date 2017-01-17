package mechanics.gamemap.messages;

import mechanics.gamemap.GameMap;
import messagesystem.Abonent;
import messagesystem.Address;
import messagesystem.Message;

/**
 * Created by Андрей on 21.12.2015.
 */
public abstract class MessageToGameMap extends Message {
    public MessageToGameMap(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GameMap) {
            exec((GameMap) abonent);
        }
    }

    protected abstract void exec(GameMap frontEnd);
}