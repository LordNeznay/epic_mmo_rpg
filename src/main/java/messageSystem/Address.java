package messagesystem;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Андрей on 20.12.2015.
 */
public class Address {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
    private final int id;

    public  Address(){
        id = ID_GENERATOR.getAndIncrement();
    }

    @Override
    public int hashCode(){
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        return id == address.id;
    }
}
