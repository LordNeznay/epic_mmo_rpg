package mechanics;

import com.sun.javafx.geom.Vec2d;

/**
 * Created by Андрей on 27.10.2015.
 */
public class Entity {
    private int x = 10;
    private int y = 10;

    public Vec2d getCoord(){
        return new Vec2d(x, y);
    }
}
