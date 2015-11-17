package mechanics;

import main.UserProfile;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Андрей on 17.11.2015.
 */
public class GameMechanicsTest {

    @Test
    public void testGetMinPlayersForStart() throws Exception {
        GameMechanics mechanics = new GameMechanics();
        assertTrue(mechanics.getMinPlayersForStart() > 1);
    }

    @Test
    public void testAddUserToCreateOneMap() throws Exception {
        GameMechanics mechanics = new GameMechanics();

        assertEquals(mechanics.getAmountPlayerInGame(), 0);
        assertEquals(mechanics.getAmountPlayerInQueue(), 0);
        assertEquals(mechanics.getAmountMap(), 0);

        int minPlayersForStart = mechanics.getMinPlayersForStart();
        for(int i=0; i<minPlayersForStart - 1; ++i){
            mechanics.addUser(new UserProfile("user", "userpassw", "user@email"));
            assertEquals(mechanics.getAmountPlayerInQueue(), i + 1);
            assertEquals(mechanics.getAmountPlayerInGame(), 0);
        }

        mechanics.addUser(new UserProfile("user", "userpassw", "user@email"));
        assertEquals(mechanics.getAmountPlayerInGame(), minPlayersForStart);
        assertEquals(mechanics.getAmountPlayerInQueue(), 0);
        assertEquals(mechanics.getAmountMap(), 1);

        mechanics.addUser(new UserProfile("user", "userpassw", "user@email"));
        assertEquals(mechanics.getAmountPlayerInGame(), minPlayersForStart + 1);
        assertEquals(mechanics.getAmountPlayerInQueue(), 0);
        assertEquals(mechanics.getAmountMap(), 1);
    }

    @Test
    public void testAddUserToCreateManyMap() throws Exception {
        GameMechanics mechanics = new GameMechanics();

        int amountPlayersInCommand = GameMap.getMaxPlayersInCommand();
        for(int i=0; i<amountPlayersInCommand * 5; ++i){
            mechanics.addUser(new UserProfile("user", "userpassw", "user@email"));
            int mustPlayerInQueue = (i+1)%(amountPlayersInCommand*2) >= mechanics.getMinPlayersForStart() ? 0 : (i+1)%mechanics.getMinPlayersForStart();
            int mustPlayerInGame = (i+1)%(amountPlayersInCommand*2) >= mechanics.getMinPlayersForStart() ? i+1 : i + 1 - mustPlayerInQueue;
            assertEquals(mechanics.getAmountPlayerInQueue(), mustPlayerInQueue);
            assertEquals(mechanics.getAmountPlayerInGame(), mustPlayerInGame);
        }
        assertEquals(mechanics.getAmountMap(), 3);
    }
}