package mechanics.messages;

import mechanics.GameMechanics;
import messagesystem.Address;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageSignalShutdownGameMechanics extends MessageToGameMechanics {
    public MessageSignalShutdownGameMechanics(Address from, Address to) {
        super(from, to);

    }

    @Override
    protected void exec(GameMechanics service) {
        service.shutdown();
    }
}