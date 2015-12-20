package accountService;

import dbservice.DBService;
import dbservice.UserDataSet;
import main.UserProfile;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import resource.ServerConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class AccountService implements Abonent, Runnable{
    private final Address address = new Address();
    private final MessageSystem messageSystem;
    private DBService dbservice;

    @Override
    public Address getAddress(){
        return address;
    }

    public AccountService(MessageSystem messageSystem, DBService dbservice) {
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().registerAccountService(this);
        this.dbservice = dbservice;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public boolean addUser(String userName, UserProfile userProfile) {
        if (dbservice.isAvailable(userName)) {
            return false;
        }
        dbservice.saveUser(new UserDataSet(userProfile.getLogin(), userProfile.getEmail(), userProfile.getPassword(), 0, null));
        return true;
    }

    @Deprecated
    public void registerUser(String login, String password){

    }

    @Deprecated
    @Nullable public UserProfile authenticate(String login, String password){
        if(login.equals("admin") && password.equals("admin")){
            return new UserProfile(login, password,login);
        }
        return null;
    }

    @Deprecated
    public boolean isExistUser(String login){
        return login.equals("admin");
    }

    @Deprecated
    public void shutdown(){

    }

    @Nullable
    public UserProfile getUserByName(String userName) {
        UserDataSet dataSet = dbservice.getByName(userName);
        if(dataSet == null)
            return null;
        return new UserProfile(dataSet.getName(), dataSet.getPassword(), dataSet.getEmail());
    }


    public long getAuthUsersNumber() { return dbservice.getAuthUser(); }

    public long getRegUsersNumber() { return dbservice.getRegCount(); }

    @Override
    public void run() {
        while (true){
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(ServerConfiguration.getInstance().getStepTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
