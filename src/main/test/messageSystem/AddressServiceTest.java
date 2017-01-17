package messagesystem;

import accountservice.AccountService;
import frontend.Frontend;
import mechanics.GameMechanics;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Андрей on 21.12.2015.
 */
public class AddressServiceTest {

    @Test
    public void testRegisterGameMechanics() throws Exception {
        Address address = new Address();
        AddressService addressService = new AddressService();
        GameMechanics gameMechanics = mock(GameMechanics.class);
        when(gameMechanics.getAddress()).thenReturn(address);
        addressService.registerGameMechanics(gameMechanics);
        assertEquals(addressService.getGameMechanicsAddress(), address);
    }

    @Test
    public void testRegisterAccountService() throws Exception {
        Address address = new Address();
        AddressService addressService = new AddressService();
        AccountService accountServise = mock(AccountService.class);
        when(accountServise.getAddress()).thenReturn(address);
        addressService.registerAccountService(accountServise);
        assertEquals(addressService.getAccountServiceAddress(), address);
    }

    @Test
    public void testRegisterFrontend() throws Exception {
        Address address = new Address();
        AddressService addressService = new AddressService();
        Frontend frontend = mock(Frontend.class);
        when(frontend.getAddress()).thenReturn(address);
        addressService.registerFrontend(frontend);
        assertEquals(addressService.getFrontendAddress(), address);
    }

}