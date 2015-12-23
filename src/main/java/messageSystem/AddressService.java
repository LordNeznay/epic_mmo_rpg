package messageSystem;

import frontend.Frontend;
import accountService.AccountService;
import mechanics.GameMechanics;


/**
 * Created by Андрей on 20.12.2015.
 */
public class AddressService {
    private Address gameMechanics;
    private Address frontend;
    private Address accountService;

    public void registerGameMechanics(GameMechanics _gameMechanics){
        gameMechanics = _gameMechanics.getAddress();
    }

    public void registerAccountService(AccountService _accountService){
        accountService = _accountService.getAddress();
    }

    public void registerFrontend(Frontend _frontend){
        frontend = _frontend.getAddress();
    }

    public Address getGameMechanicsAddress(){
        return gameMechanics;
    }

    public Address getFrontendAddress(){
        return frontend;
    }

    public Address getAccountServiceAddress(){
        return accountService;
    }

    /*public synchronized Address getGameMapAddress() {
        int index = gameMapCounter.getAndIncrement();
        if (index >= gameMapList.size()) {
            gameMapCounter.set(0);
        }
        return gameMapList.get(index);
    }*/

}
