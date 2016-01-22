package mechanics.messages;

import mechanics.GameMechanics;
import messagesystem.Abonent;
import messagesystem.Address;
import messagesystem.Message;

/**
 * Created by Андрей on 20.12.2015.
 */
public abstract class MessageToGameMechanics extends Message {
public MessageToGameMechanics(Address from, Address to) {
        super(from, to);
        }

@Override
public void exec(Abonent abonent) {
        if (abonent instanceof GameMechanics) {
        exec((GameMechanics) abonent);
        }
        }

protected abstract void exec(GameMechanics frontEnd);
        }