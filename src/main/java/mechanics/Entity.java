package mechanics;

import com.sun.javafx.geom.Vec2d;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Андрей on 27.10.2015.
 */
public class Entity {
    private int x = 8;
    private int y = 8;
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

    public void move(String params){
        switch (params){
            case "up":
                y = map.isPassability(new Vec2d(x, y-1)) ? y-1 : y;
                break;
            case "down":
                y = map.isPassability(new Vec2d(x, y+1)) ? y+1 : y;
                break;
            case "left":
                x = map.isPassability(new Vec2d(x-1, y)) ? x-1 : x;
                break;
            case "right":
                x = map.isPassability(new Vec2d(x+1, y)) ? x+1 : x;
                break;
        }
    }
}
