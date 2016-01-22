package mechanics;

import com.sun.javafx.geom.Vec2d;
import utils.ResponseConstructor;
import resource.ServerConfiguration;

/**
 * Created by Андрей on 01.11.2015.
 */
public class Flag {
    private static final double ONE_SECOND = 1000.0;
    private String owner = "none";
    private Entity invader = null;
    private static final int STEP_TIME = ServerConfiguration.getInstance().getStepTime();
    private static final int DELAY_ONE_POINT = ServerConfiguration.getInstance().getDelayOnePoint();
    private static final int CAPTURE_TIME = ServerConfiguration.getInstance().getCaptureTime();
    private int delayCapture = 0;
    private int delayOnePoint = DELAY_ONE_POINT;
    private Vec2d position = new Vec2d(-10, -10);
    private int commandRedPoints = 0;
    private int commandBluePoints = 0;

    public boolean isMayInteract(Entity entity){
        Vec2d entityPosition = entity.getCoord();
        int dist = (int)Math.sqrt((entityPosition.x - position.x)*(entityPosition.x - position.x) + (entityPosition.y - position.y)*(entityPosition.y - position.y));
        return dist <= 1;
    }

    public String getOwner() {
        return owner;
    }

    public Vec2d getPosition() {
        return position;
    }

    public int getMaxPoints(){
        return commandBluePoints > commandRedPoints ? commandBluePoints : commandRedPoints;
    }

    public String getResult(){
        if(commandRedPoints > commandBluePoints){
            return getResult(false, "CommandRed");
        } else {
            return getResult(false, "CommandBlue");
        }
    }

    public String getResult(boolean isTechnicalWin, String winner){
        return ResponseConstructor.resultGame(commandRedPoints, commandBluePoints, winner, isTechnicalWin);
    }

    public boolean startCapture(Entity invaderEntity){
        if(!isMayInteract(invaderEntity)){
            return false;
        }
        if(owner.equals(invaderEntity.getCommand())){
            return false;
        }
        if(invader != null && invader.getCommand().equals(invaderEntity.getCommand())){
            return false;
        }
        invader = invaderEntity;
        delayCapture = CAPTURE_TIME;
        return true;
    }

    public void setPosition(Vec2d position) {
        this.position = position;
    }

    public void stepping(){
        if(invader != null){
            if(isMayInteract(invader)) {
                delayCapture -= STEP_TIME;
                if (delayCapture <= 0) {
                    owner = invader.getCommand();
                    invader = null;
                    delayOnePoint = DELAY_ONE_POINT;
                    delayCapture = 0;
                }
            } else {
                invader = null;
                delayCapture = 0;
            }
        } else {
            if (!owner.equals("none")) {
                delayOnePoint -= STEP_TIME;
                if (delayOnePoint <= 0) {
                    delayOnePoint = DELAY_ONE_POINT;
                    if (owner.equals("CommandBlue")) {
                        ++commandBluePoints;
                    }
                    if (owner.equals("CommandRed")) {
                        ++commandRedPoints;
                    }
                }
            }
        }
    }


    public String getStatus(){
        return ResponseConstructor.statusFlag(commandRedPoints, commandBluePoints,owner, invader==null ? "" : invader.getCommand(), delayCapture, ONE_SECOND);
    }
}
