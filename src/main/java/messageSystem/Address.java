package messageSystem;

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
    public boolean equals(Object obj){
        return this.hashCode() == obj.hashCode();
    }
}
