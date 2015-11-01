package mechanics;

import com.sun.javafx.geom.Vec2d;
import main.UserProfile;
import org.json.simple.JSONObject;

/**
 * Created by Андрей on 01.11.2015.
 */
public class Flag {
    private String owner = "none";
    private String invader = "none";
    private static final int STEP_TIME = 100;
    private static final int DELAY_ONE_POINT = 1000;
    private static final int CAPTURE_TIME = 5000;
    private int delayCapture = 0;
    private int delayOnePoint = DELAY_ONE_POINT;
    private Vec2d position = new Vec2d(-10, -10);
    private int commandRedPoints = 0;
    private int commandBluePoints = 0;

    public boolean isMayInteract(Entity entity){
        Vec2d entityPosition = entity.getCoord();
        if(entityPosition.x - position.x < -1 || entityPosition.x - position.x > 1 || entityPosition.y - position.y < -1 || entityPosition.y - position.y > 1){
            return false;
        }
        return true;
    }

    public String getOwner() {
        return owner;
    }

    public Vec2d getPosition() {
        return position;
    }

    public boolean startCapture(Entity invaderEntity){
        Vec2d entityPosition = invaderEntity.getCoord();
        if(!isMayInteract(invaderEntity)){
            return false;
        }
        if(owner.equals(invaderEntity.getCommand())){
            invader = "none";
            return true;
        }
        if(invader.equals(invaderEntity.getCommand())){
            return false;
        }
        invader = invaderEntity.getCommand();
        delayCapture = CAPTURE_TIME;
        return true;
    }

    public void setPosition(Vec2d position) {
        this.position = position;
    }

    public void stepping(){
        if(!invader.equals("none")){
            delayCapture -= STEP_TIME;
            if(delayCapture <= 0){
                owner = invader;
                invader = "none";
                delayOnePoint = DELAY_ONE_POINT;
            }
        }
        if(!owner.equals("none")){
            delayOnePoint -= STEP_TIME;
            if(delayOnePoint <= 0){
                delayOnePoint = DELAY_ONE_POINT;
                if(owner.equals("CommandBlue")){
                    ++commandBluePoints;
                }
                if(owner.equals("CommandRed")){
                    ++commandRedPoints;
                }
            }
        }
    }

    public void sendStatus(UserProfile userProfile){
        StringBuilder flagStatus = new StringBuilder();
        flagStatus.append("{ \"commandRed\": ");
        flagStatus.append(commandRedPoints);
        flagStatus.append(", \"commandBlue\": ");
        flagStatus.append(commandBluePoints);
        flagStatus.append("}");

        JSONObject request = new JSONObject();
        request.put("type", "flagStatus");
        request.put("flagStatus", flagStatus.toString());
        userProfile.getUserSocket().sendMessage( request.toString());
    }
}
