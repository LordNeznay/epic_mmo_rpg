package mechanics;

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
}