package accountService;

import dbservice.DBService;
import dbservice.UserDataSet;
import main.UserProfile;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import resource.ServerConfiguration;


/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class AccountService implements Abonent, Runnable{
    private static final int STEP_TIME = ServerConfiguration.getInstance().getStepTime();
    private volatile boolean isWorked = false;
    private final Address address = new Address();
    private final MessageSystem messageSystem;
    private DBService dbservice;
    @NotNull private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public Address getAddress(){
        return address;
    }


    public AccountService(MessageSystem messageSystem, DBService dbservice) {
        LOGGER.info("Account-server was started");
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().registerAccountService(this);
        this.dbservice = dbservice;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public boolean addUser(String userName, UserProfile userProfile) {
        if (!dbservice.isAvailable(userName)) {
            return false;
        }
        dbservice.saveUser(new UserDataSet(userProfile.getLogin(), userProfile.getEmail(), userProfile.getPassword(), 0));
        return true;
    }

    public void registerUser(String login, String password){
        if (dbservice.isAvailable(login)) {
            dbservice.saveUser(new UserDataSet(login, "useremail", password, 0));
        }
    }

    @Nullable public UserProfile authenticate(String login, String password){
        UserDataSet dataSet = dbservice.getByName(login);
        if((dataSet != null) && (dataSet.getPassword().equals(password))) {
            return new UserProfile(dataSet.getName(), dataSet.getPassword(), dataSet.getEmail());
        }
        return null;
    }

    public boolean isExistUser(String login){
        return !dbservice.isAvailable(login);
    }

    public void shutdown(){
        dbservice.shutdown();
        isWorked = false;
    }

    @Override
    public void run() {
        isWorked = true;
        while (isWorked){
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(STEP_TIME);
            } catch (InterruptedException e) {
                LOGGER.info("Account-server was shutdown with InterruptedException");
                return;
            }
        }
        LOGGER.info("Account-server was shutdown");
    }
}