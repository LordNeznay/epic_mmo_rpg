package mechanics;

import com.sun.javafx.geom.Vec2d;
import main.UserProfile;
import mechanics.ability.Ability;
import mechanics.ability.NoneEffectAbility;
import mechanics.abilityaction.AbilityAction;
import mechanics.abilityaction.NoneEffectAction;
import mechanics.gamemap.GameMap;
import messagesystem.Address;
import messagesystem.AddressService;
import messagesystem.MessageSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Андрей on 18.11.2015.
 */

public class EntityTest {
    MessageSystem messageSystem = mock(MessageSystem.class);
    AddressService addressService = mock(AddressService.class);
    Address accountServiseStub = new Address();
    Address address = new Address();
    GameMap gameMap;

    @Before
    public void setUp() throws Exception {
        when(addressService.getAccountServiceAddress()).thenReturn(accountServiseStub);
        when(messageSystem.getAddressService()).thenReturn(addressService);
        gameMap = new GameMap(messageSystem);
        gameMap = spy(gameMap);
        when(gameMap.getAddress()).thenReturn(address);
    }

    @After
    public void after(){
        gameMap.stop();
    }

    @Test
    public void testMove() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        GameMap map = mock(GameMap.class);
        Entity entity = new Entity(map);
        Vec2d entityPos = entity.getCoord();

        Field delay = entity.getClass().getDeclaredField("timeUntilMove");
        delay.setAccessible(true);

        entityPos.x += 1;
        when(map.isPassability(entityPos)).thenReturn(true);
        entity.move("right");
        delay.set(entity, 0);
        assertEquals(entity.getCoord(), entityPos);

        entityPos.x -= 1;
        when(map.isPassability(entityPos)).thenReturn(true);
        entity.move("left");
        delay.set(entity, 0);
        assertEquals(entity.getCoord(), entityPos);

        entityPos.y += 1;
        when(map.isPassability(entityPos)).thenReturn(true);
        entity.move("down");
        delay.set(entity, 0);
        assertEquals(entity.getCoord(), entityPos);

        entityPos.y -= 1;
        when(map.isPassability(entityPos)).thenReturn(true);
        entity.move("up");
        delay.set(entity, 0);
        assertEquals(entity.getCoord(), entityPos);
    }

    @Test
    public void testMakeDamage() throws Exception {
        GameMap map = gameMap;
        Entity entity = new Entity(map);

        int hitPoints = entity.getHitPoints();

        entity.makeDamage(hitPoints/2);
        assertEquals(entity.getHitPoints(), hitPoints / 2);

        entity.makeDamage(-hitPoints / 2);
        assertEquals(entity.getHitPoints(), hitPoints);

        entity.makeDamage(-hitPoints);
        assertEquals(entity.getHitPoints(), hitPoints);

        entity.makeDamage(hitPoints + 10);
        assertEquals(entity.getHitPoints(), hitPoints);
    }


    @Test
    public void testStepping() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException {
        UserProfile userProfile = mock(UserProfile.class);

        GameMap map = gameMap;
        Entity entity = new Entity(map);

        Field abilityDelay = entity.getClass().getDeclaredField("abilityDelay");
        abilityDelay.setAccessible(true);
        abilityDelay.set(entity, 0);

        Field moveDelay = entity.getClass().getDeclaredField("timeUntilMove");
        moveDelay.setAccessible(true);
        moveDelay.set(entity, 0);

        Field step = entity.getClass().getDeclaredField("STEP_TIME");
        step.setAccessible(true);
        Integer STEP_TIME = step.getInt(entity);

        entity = spy(entity);

        entity.stepping(userProfile);
        assertTrue(moveDelay.getInt(entity) <= 0 && abilityDelay.getInt(entity) <= 0);
        verify(entity, times(1)).steppingAllAbility();

        moveDelay.set(entity, STEP_TIME*3/2);
        abilityDelay.set(entity, STEP_TIME*3/2);
        entity.stepping(userProfile);
        assertTrue(moveDelay.getInt(entity)+STEP_TIME == STEP_TIME*3/2 && abilityDelay.getInt(entity)+STEP_TIME == STEP_TIME*3/2);
        verify(entity, times(2)).steppingAllAbility();

        entity.stepping(userProfile);
        assertTrue(moveDelay.getInt(entity) <= 0 && abilityDelay.getInt(entity) <= 0);
        verify(entity, times(3)).steppingAllAbility();
    }

    @Test
    public void testAffect() throws Exception {
        GameMap map = gameMap;
        Entity entity = new Entity(map);
        AbilityAction abilityAction = new NoneEffectAction();
        AbilityAction spyAbilityAction = spy(abilityAction);

        entity.affect(spyAbilityAction);
        verify(spyAbilityAction, times(1)).run(entity);
    }


    @Test
    public void testUseAbility() throws NoSuchFieldException, SecurityException, IllegalAccessException {
        GameMap map = gameMap;
        Entity entity = new Entity(map);
        Entity target = new Entity(map);
        target = spy(target);

        Field allAbilityCooldown = entity.getClass().getDeclaredField("abilityDelay");
        allAbilityCooldown.setAccessible(true);
        allAbilityCooldown.set(entity, 0);

        Ability ability = spy(new NoneEffectAbility());
        AbilityAction someAction = new NoneEffectAction();
        when(ability.use()).thenReturn(someAction);


        String abilityName = "VeryVeryUnicalAbilityName__ertymu,io.,bmuvyctbxrtcgyvhubinkiu,mbhyvntcbrfdexvrbtcnvymubi,";
        entity.addAbility(abilityName, ability);



        entity.setTarget(target);
        entity.useAbility(abilityName);
        verify(target, times(1)).affect(someAction);
        assertTrue((Integer)allAbilityCooldown.get(entity) > 0);

        allAbilityCooldown.set(entity, 0);
        entity.useAbility(abilityName);
        verify(target, times(2)).affect(someAction);

        allAbilityCooldown.set(entity, 1);
        entity.useAbility(abilityName);
        verify(target, times(2)).affect(someAction);
    }

}