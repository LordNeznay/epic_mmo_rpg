package mechanics;

import com.sun.javafx.geom.Vec2d;
import main.UserProfile;
import mechanics.gameMap.GameMap;
import mechanics.gameMap.PhysMap;
import messageSystem.AddressService;
import messageSystem.MessageSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import utils.ReflectionHelper;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by Андрей on 18.11.2015.
 */
public class GameMapTest {
    MessageSystem messageSystem = mock(MessageSystem.class);
    AddressService addressService = mock(AddressService.class);
    GameMap gameMap;

    @Before
    public void setUp() throws Exception {
        when(messageSystem.getAddressService()).thenReturn(addressService);
        gameMap = new GameMap(messageSystem);

    }

    @After
    public void after(){
        gameMap.stop();
    }

    @Test
    public void testGetMaxPlayersInCommand() throws Exception {
        assertTrue(GameMap.getMaxPlayersInCommand() >= 2);
    }

    @Test
    public void testAddUser() throws Exception {
        GameMap map = gameMap;

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



    @Test
    public void testGetResult() throws Exception {
        GameMap map = gameMap;
        Flag flag = mock(Flag.class);
        when(flag.getResult()).thenReturn(" ");
        when(flag.getResult(anyBoolean(), anyString())).thenReturn(" ");
        ReflectionHelper.setFieldValue(map, "flag", flag);

        ReflectionHelper.setFieldValue(map, "amountBluePlayers", 0);
        ReflectionHelper.setFieldValue(map, "amountRedPlayers", 1);
        map.getResult();
        verify(flag, times(1)).getResult(true, "CommandRed");

        ReflectionHelper.setFieldValue(map, "amountBluePlayers", 1);
        ReflectionHelper.setFieldValue(map, "amountRedPlayers", 0);
        map.getResult();
        verify(flag, times(1)).getResult(true, "CommandBlue");

        ReflectionHelper.setFieldValue(map, "amountBluePlayers", 1);
        ReflectionHelper.setFieldValue(map, "amountRedPlayers", 1);
        map.getResult();
        verify(flag, times(1)).getResult();
    }


    @Test
    public void testSendPlayersPosition() throws NoSuchFieldException, SecurityException {
        GameMap map = gameMap;
        UserProfile user = new UserProfile("", "", "");
        user = spy(user);
        Entity entity = mock(Entity.class);
        when(entity.getCoord()).thenReturn(new Vec2d(0,0));
        Map<UserProfile, Entity> entities = new HashMap<>();
        entities.put(user, entity);
        ReflectionHelper.setFieldValue(map, "entities", entities);

        when(entity.isCoordChange()).thenReturn(false);
        map.sendPlayersPosition(user);
        verify(user, times(0)).addMessageForSending(anyString());

        when(entity.isCoordChange()).thenReturn(true);
        map.sendPlayersPosition(user);
        verify(user, times(1)).addMessageForSending(anyString());
    }

    @Test
    public void testSendEntityInViewArea() throws Exception {
        GameMap map = gameMap;
        UserProfile user = new UserProfile("", "", "");
        user = spy(user);
        map.addUser(user);

        Mockito.reset(user);
        map.sendEntityInViewArea(user);
        verify(user, times(1)).addMessageForSending(anyString());
    }


    @Test
    public void testIsPlace() throws Exception {
        GameMap map = gameMap;

        int maxPlayer = (int)ReflectionHelper.getFieldValue(map, "MAX_PLAYERS_IN_COMMAND");

        ReflectionHelper.setFieldValue(map, "amountRedPlayers", 0);
        ReflectionHelper.setFieldValue(map, "amountBluePlayers", 0);
        assertTrue(map.isPlace());

        ReflectionHelper.setFieldValue(map, "amountRedPlayers", maxPlayer);
        ReflectionHelper.setFieldValue(map, "amountBluePlayers", 0);
        assertTrue(map.isPlace());

        ReflectionHelper.setFieldValue(map, "amountRedPlayers", maxPlayer);
        ReflectionHelper.setFieldValue(map, "amountBluePlayers", maxPlayer);
        assertTrue(!map.isPlace());
    }

    @Test
    public void testRemoveUser() throws Exception {
        GameMap map = gameMap;
        UserProfile user = new UserProfile("", "", "");

        map.addUser(user);
        assertTrue(map.getAmountBluePlayers() == 1 || map.getAmountRedPlayers() == 1);

        map.removeUser(user);
        assertTrue(map.getAmountBluePlayers() == 0 && map.getAmountRedPlayers() == 0);
    }


    @Test
    public void testEntityMove() throws Exception {
        GameMap map = gameMap;
        UserProfile user = new UserProfile("", "", "");
        user = spy(user);
        Entity entity = spy(new Entity(map));

        Map<UserProfile, Entity> entities = new HashMap<>();
        entities.put(user, entity);
        ReflectionHelper.setFieldValue(map, "entities", entities);

        String params = "top";
        map.entityMove(user, params);
        verify(entity, times(1)).move(params);
    }


    @Test
    public void testIsPassability() throws Exception {
        GameMap map = gameMap;
        PhysMap physMap = (PhysMap)ReflectionHelper.getFieldValue(map, "physMap");
        assert physMap != null;
        physMap = spy(physMap);
        ReflectionHelper.setFieldValue(map, "physMap", physMap);
        Entity[][] entityLocation = (Entity[][])ReflectionHelper.getFieldValue(map, "entityLocation");
        assert entityLocation != null;

        when(physMap.isPassability(new Vec2d(1,1))).thenReturn(true);
        entityLocation[1][1] = null;
        ReflectionHelper.setFieldValue(map, "entityLocation", entityLocation);
        assertTrue(map.isPassability(new Vec2d(1,1)));

        when(physMap.isPassability(new Vec2d(1,1))).thenReturn(true);
        entityLocation[1][1] = new Entity(map);
        ReflectionHelper.setFieldValue(map, "entityLocation", entityLocation);
        assertTrue(!map.isPassability(new Vec2d(1,1)));

        when(physMap.isPassability(new Vec2d(1,1))).thenReturn(false);
        entityLocation[1][1] = null;
        ReflectionHelper.setFieldValue(map, "entityLocation", entityLocation);
        assertTrue(!map.isPassability(new Vec2d(1,1)));
    }

    @Test
    public void testUpdatePositionEntity() throws Exception {
        GameMap map = gameMap;
        Entity[][] entityLocation = (Entity[][])ReflectionHelper.getFieldValue(map, "entityLocation");
        assert entityLocation != null;

        Entity entity = new Entity(map);
        entityLocation[1][1] = null;
        entityLocation[1][2] = entity;

        ReflectionHelper.setFieldValue(entity, "x", 1);
        ReflectionHelper.setFieldValue(entity, "y", 1);
        map.updatePositionEntity(entity, 1, 2);

        assertEquals(entityLocation[1][1], entity);
        assertEquals(entityLocation[1][2], null);
    }

    @Test
    public void testStartFlagCapture() throws Exception {
        GameMap map = gameMap;
        UserProfile user = new UserProfile("", "", "");
        user = spy(user);
        Entity entity = spy(new Entity(map));
        Flag flag = (Flag)ReflectionHelper.getFieldValue(map, "flag");
        assert flag != null;
        flag = spy(flag);
        ReflectionHelper.setFieldValue(map, "flag", flag);

        Map<UserProfile, Entity> entities = new HashMap<>();
        entities.put(user, entity);
        ReflectionHelper.setFieldValue(map, "entities", entities);

        map.startFlagCapture(user);
        verify(flag, times(1)).startCapture(entity);
    }

    @Test
    public void testSetPlayerTarget() throws Exception {
        GameMap map = gameMap;
        UserProfile user = new UserProfile("", "", "");
        user = spy(user);
        Entity entity = new Entity(map);
        entity = spy(entity);
        Entity[][] entityLocation = (Entity[][])ReflectionHelper.getFieldValue(map, "entityLocation");
        assert entityLocation != null;

        Map<UserProfile, Entity> entities = new HashMap<>();
        entities.put(user, entity);
        ReflectionHelper.setFieldValue(map, "entities", entities);
        when(entity.getCoord()).thenReturn(new Vec2d(1,1));

        int posX = (int)ReflectionHelper.getFieldValue(map, "VIEW_WIDTH_2");
        int posY = (int)ReflectionHelper.getFieldValue(map, "VIEW_HEIGHT_2");

        entityLocation[2][2] = null;
        map.setPlayerTarget(user, posX+1, posY+1);
        verify(entity, times(0)).setTarget(entity);

        entityLocation[1][1] = entity;
        map.setPlayerTarget(user, posX,posY);
        verify(entity, times(1)).setTarget(entity);
    }

    @Test
    public void testUseAbility() throws Exception {
        GameMap map = gameMap;
        UserProfile user = new UserProfile("", "", "");
        user = spy(user);
        Entity entity = spy(new Entity(map));

        Map<UserProfile, Entity> entities = new HashMap<>();
        entities.put(user, entity);
        ReflectionHelper.setFieldValue(map, "entities", entities);

        String abilityName = "NoneEffectAbility";
        map.useAbility(user, abilityName);
        verify(entity, times(1)).useAbility(abilityName);
    }
}