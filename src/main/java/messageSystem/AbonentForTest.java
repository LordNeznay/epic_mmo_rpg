package messageSystem;

import org.jetbrains.annotations.TestOnly;

/**
 * Created by Андрей on 21.12.2015.
 */

@TestOnly
public class AbonentForTest implements Abonent{
    private final Address address = new Address();

    @Override
    public Address getAddress(){
        return address;
    }
}
