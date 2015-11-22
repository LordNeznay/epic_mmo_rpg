package mechanics;

import com.sun.javafx.geom.Vec2d;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Андрей on 18.11.2015.
 */


/*
    Пример Виталика по доступу к приватным полям
    static class A {
        private String a;
        @Override
        public String toString() {
            return a;
        }
    }

    public static void main(String[] args) {
        A a = new A();
        try {
            Field aF = A.class.getDeclaredField("a");
            aF.setAccessible(true);
            aF.set(a, "check");
            System.out.println(a);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
 */
public class EntityTest {

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
        GameMap map = new GameMap();
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
}