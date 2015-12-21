//package mechanics;
//
//import main.UserProfile;
//import messageSystem.AddressService;
//import messageSystem.MessageSystem;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.*;
//
///**
// * Created by Андрей on 17.11.2015.
// */
//public class GameMechanicsTest {
//    MessageSystem messageSystem = mock(MessageSystem.class);
//    AddressService addressService = mock(AddressService.class);
//
//    @Before
//    public void setUp(){
//        when(messageSystem.getAddressService()).thenReturn(addressService);
//    }
//
//    @Test
//    public void testGetMinPlayersForStart() throws Exception {
//        GameMechanics mechanics = new GameMechanics(messageSystem);
//        assertTrue(mechanics.getMinPlayersForStart() > 1);
//    }
//
//    @Test
//    public void testAddUserToCreateOneMap() throws Exception {
//        GameMechanics mechanics = new GameMechanics(messageSystem);
//
//        assertEquals(mechanics.getAmountPlayerInGame(), 0);
//        assertEquals(mechanics.getAmountPlayerInQueue(), 0);
//        assertEquals(mechanics.getAmountMap(), 0);
//
//        int minPlayersForStart = mechanics.getMinPlayersForStart();
//        for(int i=0; i<minPlayersForStart - 1; ++i){
//            mechanics.addUser(new UserProfile("user", "userpassw", "user@email"));
//            assertEquals(mechanics.getAmountPlayerInQueue(), i + 1);
//            assertEquals(mechanics.getAmountPlayerInGame(), 0);
//        }
//
//        mechanics.addUser(new UserProfile("user", "userpassw", "user@email"));
//        assertEquals(mechanics.getAmountPlayerInGame(), minPlayersForStart);
//        assertEquals(mechanics.getAmountPlayerInQueue(), 0);
//        assertEquals(mechanics.getAmountMap(), 1);
//
//        mechanics.addUser(new UserProfile("user", "userpassw", "user@email"));
//        assertEquals(mechanics.getAmountPlayerInGame(), minPlayersForStart + 1);
//        assertEquals(mechanics.getAmountPlayerInQueue(), 0);
//        assertEquals(mechanics.getAmountMap(), 1);
//    }
//
//    @Test
//    public void testAddUserToCreateManyMap() throws Exception {
//        GameMechanics mechanics = new GameMechanics(messageSystem);
//
//        int amountPlayersInCommand = GameMap.getMaxPlayersInCommand();
//        for(int i=0; i<amountPlayersInCommand * 5; ++i){
//            mechanics.addUser(new UserProfile("user", "userpassw", "user@email"));
//            int mustPlayerInQueue = (i+1)%(amountPlayersInCommand*2) >= mechanics.getMinPlayersForStart() ? 0 : (i+1)%mechanics.getMinPlayersForStart();
//            int mustPlayerInGame = (i+1)%(amountPlayersInCommand*2) >= mechanics.getMinPlayersForStart() ? i+1 : i + 1 - mustPlayerInQueue;
//            assertEquals(mechanics.getAmountPlayerInQueue(), mustPlayerInQueue);
//            assertEquals(mechanics.getAmountPlayerInGame(), mustPlayerInGame);
//        }
//        assertEquals(mechanics.getAmountMap(), 3);
//    }
//
//    @Test
//    public void testRemoveUser() throws Exception {
//        GameMechanics mechanics = new GameMechanics(messageSystem);
//
//        UserProfile userProfile = new UserProfile("", "", "");
//        userProfile = spy(userProfile);
//
//        mechanics.addUser(userProfile);
//        assertTrue(mechanics.getAmountPlayerInQueue() == 1 || mechanics.getAmountPlayerInGame() == 1);
//
//        mechanics.removeUser(userProfile);
//        assertTrue(mechanics.getAmountPlayerInQueue() == 0 && mechanics.getAmountPlayerInGame() == 0);
//    }
//
//    /*
//    @Test
//    public void testStartStop() throws Exception {
//        GameMechanics mechanics = new GameMechanics();
//        assertTrue(!mechanics.isInGame());
//        mechanics.start();
//        assertTrue(mechanics.isInGame());
//        mechanics.stop();
//        assertTrue(!mechanics.isInGame());
//    }*/
//
//
//    @Test
//    public void testRemoveMap() throws Exception {
//        GameMechanics mechanics = new GameMechanics(messageSystem);
//        UserProfile userProfile = new UserProfile("","","");
//        mechanics.addUser(userProfile);
//        for(int i=1; i<mechanics.getMinPlayersForStart(); ++i){
//            mechanics.addUser(new UserProfile("", "", ""));
//        }
//
//        assertEquals(mechanics.getAmountMap(), 1);
//        mechanics.removeMap(mechanics.getMapWithUser(userProfile));
//        assertEquals(mechanics.getAmountMap(), 0);
//    }
//
//
//    @Test
//    public void testMovePlayer() throws Exception {
//        GameMechanics mechanics = new GameMechanics(messageSystem);
//        UserProfile userProfile = new UserProfile("","","");
//        mechanics.addUser(userProfile);
//        for(int i=1; i<mechanics.getMinPlayersForStart(); ++i){
//            mechanics.addUser(new UserProfile("", "", ""));
//        }
//        GameMap map = spy(mechanics.getMapWithUser(userProfile));
//        mechanics.replaseMap(mechanics.getMapWithUser(userProfile), map);
//
//        mechanics.movePlayer(userProfile, "up");
//        verify(map, times(1)).entityMove(userProfile, "up");
//    }
//
//
//    @Test
//    public void testStartFlagCapture() throws Exception {
//        GameMechanics mechanics = new GameMechanics(messageSystem);
//        UserProfile userProfile = new UserProfile("","","");
//        mechanics.addUser(userProfile);
//        for(int i=1; i<mechanics.getMinPlayersForStart(); ++i){
//            mechanics.addUser(new UserProfile("", "", ""));
//        }
//        GameMap map = spy(mechanics.getMapWithUser(userProfile));
//        mechanics.replaseMap(mechanics.getMapWithUser(userProfile), map);
//
//        mechanics.startFlagCapture(userProfile);
//        verify(map, times(1)).startFlagCapture(userProfile);
//    }
//
//    @Test
//    public void testSetPlayerTarget() throws Exception {
//        GameMechanics mechanics = new GameMechanics(messageSystem);
//        UserProfile userProfile = new UserProfile("","","");
//        mechanics.addUser(userProfile);
//        for(int i=1; i<mechanics.getMinPlayersForStart(); ++i){
//            mechanics.addUser(new UserProfile("", "", ""));
//        }
//        GameMap map = spy(mechanics.getMapWithUser(userProfile));
//        mechanics.replaseMap(mechanics.getMapWithUser(userProfile), map);
//
//        mechanics.setPlayerTarget(userProfile, 1, 1);
//        verify(map, times(1)).setPlayerTarget(userProfile, 1, 1);
//    }
//
//    @Test
//    public void testUseAbility() throws Exception {
//        GameMechanics mechanics = new GameMechanics(messageSystem);
//        UserProfile userProfile = new UserProfile("","","");
//        mechanics.addUser(userProfile);
//        for(int i=1; i<mechanics.getMinPlayersForStart(); ++i){
//            mechanics.addUser(new UserProfile("", "", ""));
//        }
//        GameMap map = spy(mechanics.getMapWithUser(userProfile));
//        mechanics.replaseMap(mechanics.getMapWithUser(userProfile), map);
//
//        mechanics.useAbility(userProfile, "NoneEffectAbility");
//        verify(map, times(1)).useAbility(userProfile, "NoneEffectAbility");
//    }
//
//}