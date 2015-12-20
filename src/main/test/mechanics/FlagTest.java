package mechanics;

import com.sun.javafx.geom.Vec2d;
import org.junit.Test;
import utils.ReflectionHelper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Андрей on 01.12.2015.
 */
public class FlagTest {

    @Test
    public void testIsMayInteract() throws Exception {
        Flag flag = new Flag();
        ReflectionHelper.setFieldValue(flag, "position", new Vec2d(2, 2));
        Entity entity = mock(Entity.class);

        when(entity.getCoord()).thenReturn(new Vec2d(1,1));
        assertTrue(flag.isMayInteract(entity));

        when(entity.getCoord()).thenReturn(new Vec2d(0,1));
        assertTrue(!flag.isMayInteract(entity));
    }

    @Test
    public void testGetResult() throws Exception {
        Flag flag = new Flag();
        ReflectionHelper.setFieldValue(flag, "commandRedPoints", 10);
        ReflectionHelper.setFieldValue(flag, "commandBluePoints", 1);
        flag = spy(flag);

        flag.getResult();
        verify(flag, times(1)).getResult(false, "CommandRed");


        flag = new Flag();
        ReflectionHelper.setFieldValue(flag, "commandRedPoints", 1);
        ReflectionHelper.setFieldValue(flag, "commandBluePoints", 10);
        flag = spy(flag);

        flag.getResult();
        verify(flag, times(1)).getResult(false, "CommandBlue");
    }

}