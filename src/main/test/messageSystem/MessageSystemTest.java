package messageSystem;

import org.junit.Before;
import org.junit.Test;
import utils.ReflectionHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by Андрей on 21.12.2015.
 */
public class MessageSystemTest {
    MessageSystem messageSystem = new MessageSystem();

    @Before
    public void setUp(){
        messageSystem = spy(messageSystem);
    }

    @Test
    public void testAddService() throws Exception {
        assertEquals(messageSystem.getAmountMessages(), 0);
        AbonentForTest abonent = new AbonentForTest();
        messageSystem.addService(abonent);
        assertEquals(messageSystem.getAmountMessages(), 1);
    }

    @Test
    public void testSendMessageAndExecForAbonent() throws Exception {
        AbonentForTest abonent = new AbonentForTest();
        messageSystem.addService(abonent);
        assertEquals(messageSystem.getAmountMessagesFromServise(abonent.getAddress()), 0);
        messageSystem.sendMessage(new MessageForTest(abonent.getAddress(), abonent.getAddress()));
        assertEquals(messageSystem.getAmountMessagesFromServise(abonent.getAddress()), 1);
        messageSystem.execForAbonent(abonent);
        assertEquals(messageSystem.getAmountMessagesFromServise(abonent.getAddress()), 0);
    }


}