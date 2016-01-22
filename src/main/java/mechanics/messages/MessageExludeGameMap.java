package mechanics.messages;

import mechanics.GameMechanics;
import messagesystem.Address;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageExludeGameMap  extends MessageToGameMechanics {
    Address gameMapForExclude;

    public MessageExludeGameMap(Address from, Address to){
        super(from, to);
        gameMapForExclude = from;
    }

    @Override
    protected void exec(GameMechanics gameMechanics){
        gameMechanics.removeMap(gameMapForExclude);
    }
}