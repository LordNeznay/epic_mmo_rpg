package utils;

import main.UserProfile;
import mechanics.GameMap;
import mechanics.GameMechanics;

/**
 * Created by Андрей on 03.11.2015.
 */
public final class Repairer {
    private static Repairer s_obj;
    private GameMechanics gameMechanics = null;
    private Repairer(){}

    public static Repairer getInstance(){
        if(s_obj == null){
            s_obj = new Repairer();
        }
        return s_obj;
    }

    public void setGameMechanics(GameMechanics _gameMechanics){
        gameMechanics = _gameMechanics;
    }

    public void repairGameMap(GameMap gameMap){
        gameMechanics.removeMap(gameMap);
        System.out.println("Game map was fixed!");
    }

    public void repaireUser(UserProfile userProfile){
        if(gameMechanics != null){
            gameMechanics.removeUser(userProfile);
            System.out.println(userProfile.getLogin() + " was fixed!");
        }
    }
}
