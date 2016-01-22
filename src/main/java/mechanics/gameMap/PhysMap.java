package mechanics.gamemap;

import com.sun.javafx.geom.Vec2d;

/**
 * Created by Андрей on 27.10.2015.
 */
public interface PhysMap {
    Vec2d getSize();
    boolean isPassability(Vec2d cell);
    String getObjectLayer();
}
