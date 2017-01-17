package utils;

import com.sun.javafx.geom.Vec2d;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Андрей on 30.11.2015.
 */
public class ResponseConstructor {
    @NotNull
    @Contract(pure = true)
    public static String getResponse(String type, String body){
        return "{\"t\":\"" +
                type +
                "\",\"b\":" +
                body +
                '}';
    }

    @NotNull
    @Contract(pure = true)
    public static String getConfirmRequest(String type){
        return "{\"t\":\"" + type + "\"}";
    }

    @NotNull
    @Contract(pure = true)
    public static String getCoordJson(Vec2d vec){
        return "{\"x\":" + (int)vec.x + ",\"y\":" + (int)vec.y + '}';
    }

    @NotNull
    @Contract(pure = true)
    public static String getTargetJson(Vec2d vec){
        return "{\"x\":" + (int)vec.x + ",\"y\":" + (int)vec.y + ",\"image\":\"target.png\"}";
    }

    @NotNull
    @Contract(pure = true)
    public static String getFlagJson(Vec2d vec, String command){
        StringBuilder result = new StringBuilder();
        result.append("{\"x\":");
        result.append((int)vec.x);
        result.append(",\"y\":");
        result.append((int)vec.y);
        result.append(",\"image\":");
        switch(command){
            case "CommandBlue":
                result.append("\"blue_flag.png\"");
                break;
            case "CommandRed":
                result.append("\"red_flag.png\"");
                break;
            default:
                result.append("\"flag.png\"");
                break;
        }
        result.append(",\"number\":");
        result.append(-1);
        result.append('}');
        return result.toString();
    }


    @NotNull
    @Contract(pure = true)
    public static String getEntityJson(Vec2d vec, String command, int number){
        return getEntityJson(vec, command, number, "none");
    }

    @NotNull
    @Contract(pure = true)
    public static String getEntityJson(Vec2d vec, String command, int number, String directAbility){
        StringBuilder result = new StringBuilder();
        result.append("{\"x\":");
        result.append((int)vec.x);
        result.append(",\"y\":");
        result.append((int)vec.y);
        result.append(",\"image\":");
        switch(command){
            case "CommandBlue":
                result.append("\"blue_people.png\"");
                break;
            case "CommandRed":
                result.append("\"red_people.png\"");
                break;
            default:
                result.append("\"people.png\"");
                break;
        }
        result.append(",\"number\":");
        result.append(number);
        result.append(",\"da\":\"");
        result.append(directAbility);
        result.append('"');
        result.append('}');
        return result.toString();
    }

    @NotNull
    @Contract(pure = true)
    public static String entitiesInViewArea(Vec2d vec, String entities){
        return '{' + "\"player\":{\"x\":" + (int)vec.x + ",\"y\":" + (int)vec.y + ",\"image\":\"people.png\"}," + "\"entities\":[" + entities + "]}";
    }

    @NotNull
    @Contract(pure = true)
    public static String entityStatus(int hitPoints, boolean isHaveTarget, int targetHitPoints){
        StringBuilder entityStatus = new StringBuilder();
        entityStatus.append('{');
        entityStatus.append("\"hp\":");
        entityStatus.append(hitPoints);
        if(isHaveTarget) {
            entityStatus.append(",\"thp\":");
            entityStatus.append(targetHitPoints);
        }
        entityStatus.append('}');
        return entityStatus.toString();
    }

    @NotNull
    @Contract(pure = true)
    public static String statusFlag(int commandRedPoints, int commandBluePoints, String owner, String invader, int delayCapture, double ONE_SECOND){
        StringBuilder flagStatus = new StringBuilder();
        flagStatus.append("{\"commandRed\":");
        flagStatus.append(commandRedPoints);
        flagStatus.append(",\"commandBlue\":");
        flagStatus.append(commandBluePoints);
        flagStatus.append(",\"captureTime\":\"");
        if(delayCapture == 0){
            if(!owner.equals("none")) {
                flagStatus.append(owner.equals("CommandRed") ? "R" : "B");
            }
        } else {
            flagStatus.append(invader.equals("CommandRed") ? "R" : "B");
            flagStatus.append((double)delayCapture / ONE_SECOND);
        }
        flagStatus.append("\"}");
        return flagStatus.toString();
    }

    @NotNull
    @Contract(pure = true)
    public static String resultGame(int commandRedPoints, int commandBluePoints, String winner, boolean isTechnicalWin){
        StringBuilder result = new StringBuilder();
        result.append("{\"CommandRed\":");
        result.append(commandRedPoints);
        result.append(",\"CommandBlue\":");
        result.append(commandBluePoints);
        result.append(",\"winner\":\"");
        result.append(winner);
        result.append("\",\"isTechnical\":");
        if(isTechnicalWin){
            result.append("true");
        } else {
            result.append("false");
        }
        result.append('}');
        return result.toString();
    }

}
