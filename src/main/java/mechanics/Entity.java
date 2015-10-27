package mechanics;

import com.sun.javafx.geom.Vec2d;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Андрей on 27.10.2015.
 */
public class Entity {
    private int x = 10;
    private int y = 10;
    private String command = "";
    private GameMap map = null;

    public Entity(@NotNull GameMap _map){
        map = _map;
    }

    public Vec2d getCoord(){
        return new Vec2d(x, y);
    }

    public void setCommand(String newCommand){
        command = newCommand;
    }

    public String getCommand() {
        return command;
    }
}
