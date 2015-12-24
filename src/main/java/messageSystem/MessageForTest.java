package messageSystem;

import org.jetbrains.annotations.TestOnly;

/**
 * Created by Андрей on 21.12.2015.
 */

public class MessageForTest extends Message {
    public MessageForTest(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {

    }
}
