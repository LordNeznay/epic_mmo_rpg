package messageSystem;

import org.jetbrains.annotations.TestOnly;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Андрей on 20.12.2015.
 */
public class MessageSystem {
    private final Map<Address, ConcurrentLinkedQueue<Message>> messages = new HashMap<>();
    private final AddressService addressService = new AddressService();

    public AddressService getAddressService(){
        return addressService;
    }

    public void addService(Abonent abonent){
        messages.put(abonent.getAddress(), new ConcurrentLinkedQueue<>());
    }

    public void sendMessage(Message message){
        messages.get(message.getTo()).add(message);
    }

    public void execForAbonent(Abonent abonent){
        ConcurrentLinkedQueue<Message> queue = messages.get(abonent.getAddress());
        while (!queue.isEmpty()){
            Message message = queue.poll();
            message.exec(abonent);
        }
    }

    @TestOnly
    public int getAmountMessages(){
        return messages.size();
    }

    @TestOnly
    public int getAmountMessagesFromServise(Address address){
        return messages.get(address).size();
    }
}
