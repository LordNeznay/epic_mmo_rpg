package base;

import java.awt.Point;
/**
 * Created by uschsh on 25.10.15.
 */

public class GameUser {
    private final String myName;
    private Point position;

    public GameUser(String myName) {
        this.myName = myName;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        position.setLocation(x, y);
    }
}
