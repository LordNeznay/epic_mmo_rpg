package mechanics.gamemap.messages;

import mechanics.gamemap.GameMap;
import messagesystem.Address;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageDeleteGameMap extends MessageToGameMap {
    public MessageDeleteGameMap(Address from, Address to){
        super(from, to);
    }

    @Override
    protected void exec(GameMap service){
        service.sendResultMap();
        service.stop();
    }
}
