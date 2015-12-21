package mechanics.gameMap.messages;

import mechanics.gameMap.GameMap;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;

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