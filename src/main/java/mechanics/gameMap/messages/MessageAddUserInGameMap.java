package mechanics.gameMap.messages;

import main.UserProfile;
import mechanics.gameMap.GameMap;
import messageSystem.Address;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageAddUserInGameMap extends MessageToGameMap {
    UserProfile user;

    public MessageAddUserInGameMap(Address from, Address to, UserProfile user){
        super(from, to);
        this.user = user;
    }

    @Override
    protected void exec(GameMap service){
        service.addUser(user);
        System.out.print("User " + user.getLogin() + " was added in game\n");
    }
}
