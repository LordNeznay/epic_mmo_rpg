package utils;

import com.sun.javafx.geom.Vec2d;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Андрей on 01.12.2015.
 */
public class ResponseConstructorTest {

    @Test
    public void testGetResponse() throws Exception {
        String result = ResponseConstructor.getResponse("one_type", "{}");
        assertEquals(result, "{\"t\":\"one_type\",\"b\":{}}");
    }

    @Test
    public void testGetCoordJson() throws Exception {
        Vec2d coord = new Vec2d(1, 1);
        String result = ResponseConstructor.getCoordJson(coord);
        assertEquals(result, "{\"x\":1,\"y\":1}");
    }

    @Test
    public void testGetTargetJson() throws Exception {
        Vec2d coord = new Vec2d(1, 1);
        String result = ResponseConstructor.getTargetJson(coord);
        assertEquals(result, "{\"x\":1,\"y\":1,\"image\":\"target.png\"}");
    }

    @Test
    public void testGetFlagJson() throws Exception {
        Vec2d vec = new Vec2d(1, 1);

        String command = "CommandRed";
        String result = ResponseConstructor.getFlagJson(vec, command);
        assertEquals(result, "{\"x\":1,\"y\":1,\"image\":\"red_flag.png\",\"number\":-1}");

        command = "CommandBlue";
        result = ResponseConstructor.getFlagJson(vec, command);
        assertEquals(result, "{\"x\":1,\"y\":1,\"image\":\"blue_flag.png\",\"number\":-1}");

        command = "none";
        result = ResponseConstructor.getFlagJson(vec, command);
        assertEquals(result, "{\"x\":1,\"y\":1,\"image\":\"flag.png\",\"number\":-1}");
    }


    @Test
    public void testGetEntityJson() throws Exception {
        Vec2d vec = new Vec2d(1, 1);

        String command = "CommandRed";
        String result = ResponseConstructor.getEntityJson(vec, command, 1);
        assertEquals(result, "{\"x\":1,\"y\":1,\"image\":\"red_people.png\",\"number\":1,\"da\":\"none\"}");

        command = "CommandBlue";
        result = ResponseConstructor.getEntityJson(vec, command, 1);
        assertEquals(result, "{\"x\":1,\"y\":1,\"image\":\"blue_people.png\",\"number\":1,\"da\":\"none\"}");

        command = "none";
        result = ResponseConstructor.getEntityJson(vec, command, 1);
        assertEquals(result, "{\"x\":1,\"y\":1,\"image\":\"people.png\",\"number\":1,\"da\":\"none\"}");
    }

    @Test
    public void testEntitiesInViewArea() throws Exception {
        Vec2d vec = new Vec2d(1, 1);

        String result = ResponseConstructor.entitiesInViewArea(vec, "{},{},{}");
        assertEquals(result, "{\"player\":{\"x\":1,\"y\":1,\"image\":\"people.png\"},\"entities\":[{},{},{}]}");
    }

    @Test
    public void testEntityStatus() throws Exception {
        String result = ResponseConstructor.entityStatus(1, true, 2);
        assertEquals(result, "{\"hp\":1,\"thp\":2}");

        result = ResponseConstructor.entityStatus(1, false, 2);
        assertEquals(result, "{\"hp\":1}");
    }

    @Test
    public void testStatusFlag() throws Exception {
        String result = ResponseConstructor.statusFlag(1, 1, "CommandRed", "CommandBlue", 200, 1000.0);
        assertEquals(result, "{\"commandRed\":1,\"commandBlue\":1,\"captureTime\":\"B0.2\"}");

        result = ResponseConstructor.statusFlag(1, 1, "CommandBlue", "CommandRed", 200, 1000.0);
        assertEquals(result, "{\"commandRed\":1,\"commandBlue\":1,\"captureTime\":\"R0.2\"}");

        result = ResponseConstructor.statusFlag(1, 1, "CommandBlue", "some_value", 0, 1000.0);
        assertEquals(result, "{\"commandRed\":1,\"commandBlue\":1,\"captureTime\":\"B\"}");

        result = ResponseConstructor.statusFlag(1, 1, "CommandRed", "some_value", 0, 1000.0);
        assertEquals(result, "{\"commandRed\":1,\"commandBlue\":1,\"captureTime\":\"R\"}");

        result = ResponseConstructor.statusFlag(1, 1, "none", "some_value", 0, 1000.0);
        assertEquals(result, "{\"commandRed\":1,\"commandBlue\":1,\"captureTime\":\"\"}");
    }

    @Test
    public void testResultGame() throws Exception {
        String result = ResponseConstructor.resultGame(1, 2, "CommandBlue", false);
        assertEquals(result, "{\"CommandRed\":1,\"CommandBlue\":2,\"winner\":\"CommandBlue\",\"isTechnical\":false}");

        result = ResponseConstructor.resultGame(1, 2, "CommandRed", true);
        assertEquals(result, "{\"CommandRed\":1,\"CommandBlue\":2,\"winner\":\"CommandRed\",\"isTechnical\":true}");
    }
}