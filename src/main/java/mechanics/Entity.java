package mechanics;

import com.sun.javafx.geom.Vec2d;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Андрей on 27.10.2015.
 */
public class Entity {
    private static final int STEP_TIME = 100;
    private static final int MOVE_DELAY = 400;
    private static Vec2d CommandsRedSpawnPoint = new Vec2d(0, 0);
    private static Vec2d CommandsBlueSpawnPoint = new Vec2d(0, 0);
    private int x = 0;
    private int y = 0;
    private String command = "";
    private GameMap map = null;
    private int timeUntilMove = 0;
    private Entity target = null;

    public Entity(@NotNull GameMap _map){
        map = _map;

    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public static void setCommandsRedSpawnPoint(Vec2d commandsRedSpawnPoint) {
        CommandsRedSpawnPoint = commandsRedSpawnPoint;
    }

    public static void setCommandsBlueSpawnPoint(Vec2d commandsBlueSpawnPoint) {
        CommandsBlueSpawnPoint = commandsBlueSpawnPoint;
    }

    public Vec2d getCoord(){
        return new Vec2d(x, y);
    }

    private void goToSpawn(){
        int lastX = x;
        int lastY = y;
        if(command.equals("CommandRed")){
            x = (int)CommandsRedSpawnPoint.x;
            y = (int)CommandsRedSpawnPoint.y;
        } else {
            x = (int)CommandsBlueSpawnPoint.x;
            y = (int)CommandsBlueSpawnPoint.y;
        }
        map.updatePositionEntity(this, lastX, lastY);
    }

    public void setCommand(String newCommand){
        command = newCommand;
        goToSpawn();
    }

    public String getCommand() {
        return command;
    }

    public void stepping(){
        timeUntilMove = timeUntilMove > 0 ? timeUntilMove-STEP_TIME : 0;
    }

    public void move(String params){
        if(timeUntilMove > 0) return;
        switch (params){
            case "up":
                if(map.isPassability(new Vec2d(x, y-1))) {
                    y = y - 1;
                    timeUntilMove = MOVE_DELAY;
                }
                break;
            case "down":
                if(map.isPassability(new Vec2d(x, y+1))) {
                    y = y + 1;
                    timeUntilMove = MOVE_DELAY;
                }
                break;
            case "left":
                if(map.isPassability(new Vec2d(x-1, y))) {
                    x = x - 1;
                    timeUntilMove = MOVE_DELAY;
                }
                break;
            case "right":
                if(map.isPassability(new Vec2d(x+1, y))) {
                    x = x + 1;
                    timeUntilMove = MOVE_DELAY;
                }
                break;
        }
    }
}
