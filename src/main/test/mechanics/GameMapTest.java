package mechanics;

import main.UserProfile;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Андрей on 18.11.2015.
 */
public class GameMapTest {

    @Test
    public void testGetMaxPlayersInCommand() throws Exception {
        assertTrue(GameMap.getMaxPlayersInCommand() >= 2);
    }

    @Test
    public void testAddUser() throws Exception {
        GameMap map = new GameMap();

        assertTrue(map.getAmountBluePlayers() == 0 && map.getAmountRedPlayers() == 0);


        UserProfile user1 = new UserProfile("user", "userpassw", "user@email");
        UserProfile user2 = new UserProfile("user", "userpassw", "user@email");
        assertTrue(map.addUser(user1));
        assertTrue(map.getAmountBluePlayers() == 0 || map.getAmountRedPlayers() == 0);
        assertTrue(map.getAmountBluePlayers() == 1 || map.getAmountRedPlayers() == 1);

        map.addUser(new UserProfile("user", "userpassw", "user@email"));
        assertTrue(map.getAmountBluePlayers() == 1 && map.getAmountRedPlayers() == 1);


        for(int i=0; i<GameMap.getMaxPlayersInCommand() - 2; ++i){
            map.addUser(new UserProfile("user", "userpassw", "user@email"));
            map.addUser(new UserProfile("user", "userpassw", "user@email"));
        }

        map.addUser(user2);
        map.addUser(new UserProfile("user", "userpassw", "user@email"));
        assertTrue(map.getAmountBluePlayers() == GameMap.getMaxPlayersInCommand() && map.getAmountRedPlayers() == GameMap.getMaxPlayersInCommand());


        assertTrue(!map.addUser(new UserProfile("user", "userpassw", "user@email")));


        map.removeUser(user1);
        map.removeUser(user2);
        map.addUser(new UserProfile("user", "userpassw", "user@email"));
        map.addUser(new UserProfile("user", "userpassw", "user@email"));
        assertTrue(map.getAmountBluePlayers() == GameMap.getMaxPlayersInCommand() && map.getAmountRedPlayers() == GameMap.getMaxPlayersInCommand());
    }
}