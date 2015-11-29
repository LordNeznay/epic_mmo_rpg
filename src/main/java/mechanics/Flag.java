package mechanics;

import com.sun.javafx.geom.Vec2d;
import main.UserProfile;
import org.json.simple.JSONObject;
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
        return !(entityPosition.x - position.x < -1 || entityPosition.x - position.x > 1 || entityPosition.y - position.y < -1 || entityPosition.y - position.y > 1);
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
        StringBuilder result = new StringBuilder();
        result.append("{\"CommandRed\": ");
        result.append(commandRedPoints);
        result.append(", \"CommandBlue\": ");
        result.append(commandBluePoints);
        result.append(", \"winner\": \"");
        result.append(winner);
        result.append("\", \"isTechnical\": ");
        if(isTechnicalWin){
            result.append("true");
        } else {
            result.append("false");
        }
        result.append('}');
        return result.toString();
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

    @SuppressWarnings("unchecked")
    public void sendStatus(UserProfile userProfile){
        StringBuilder flagStatus = new StringBuilder();
        flagStatus.append("{ \"commandRed\": ");
        flagStatus.append(commandRedPoints);
        flagStatus.append(", \"commandBlue\": ");
        flagStatus.append(commandBluePoints);
        flagStatus.append(", \"captureTime\": \"");
        if(delayCapture == 0){
            if(!owner.equals("none")) {
                flagStatus.append(owner.equals("CommandRed") ? "R" : "B");
            }
        } else {
            flagStatus.append(invader.getCommand().equals("CommandRed") ? "R" : "B");
            flagStatus.append((double)delayCapture / ONE_SECOND);
        }
        flagStatus.append("\"}");

        JSONObject request = new JSONObject();
        request.put("type", "flagStatus");
        request.put("flagStatus", flagStatus.toString());
        userProfile.sendMessage(request.toString());
    }
}
