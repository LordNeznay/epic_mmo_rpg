package mechanics;

import main.UserProfile;

import java.util.ArrayList;

/**
 * Created by uschsh on 26.10.15.
 */
public class GameMap {
    private int w = 25;
    private int h = 25;
    private ArrayList<MapCell> backgroundLayer = new ArrayList<MapCell>();
    private ArrayList<MapCell> passabilityLayer = new ArrayList<MapCell>();

    private ArrayList<UserProfile> users = new ArrayList<UserProfile>();

    public void addUser(UserProfile userProfile) {
        users.add(userProfile);
    }
}
