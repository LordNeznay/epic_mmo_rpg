package mechanics;

import com.sun.javafx.geom.Vec2d;
import main.UserProfile;
import mechanics.ability.OrdinaryHealing;
import mechanics.ability.OrdinaryHit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Андрей on 27.10.2015.
 */
public class Entity {
    private static final int MAX_HIT_POINTS = 1000;
    private static final int STEP_TIME = 100;
    private static final int MOVE_DELAY = 200;
    private static final int ABILITY_DELAY = 1000;
    private static Vec2d s_commandsRedSpawnPoint = new Vec2d(0, 0);
    private static Vec2d s_commandsBlueSpawnPoint = new Vec2d(0, 0);
    private int hitPoints = MAX_HIT_POINTS;
    private int x = 0;
    private int y = 0;
    private String command = "";
    private GameMap map = null;
    private int timeUntilMove = 0;
    private Entity target = null;
    private Map<String, Ability> abilities = new HashMap<String, Ability>();
    private int abilityDelay = 0;

    public Entity(@NotNull GameMap _map){
        map = _map;
        abilities.put("OrdinaryHealing", new OrdinaryHealing());
        abilities.put("OrdinaryHit", new OrdinaryHit());
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(@Nullable Entity target) {
        this.target = target;
    }

    public static void setCommandsRedSpawnPoint(Vec2d commandsRedSpawnPoint) {
        s_commandsRedSpawnPoint = commandsRedSpawnPoint;
    }

    public static void setCommandsBlueSpawnPoint(Vec2d commandsBlueSpawnPoint) {
        s_commandsBlueSpawnPoint = commandsBlueSpawnPoint;
    }

    public Vec2d getCoord(){
        return new Vec2d(x, y);
    }

    private void goToSpawn(){
        int lastX = x;
        int lastY = y;
        if(command.equals("CommandRed")){
            x = (int) s_commandsRedSpawnPoint.x;
            y = (int) s_commandsRedSpawnPoint.y;
        } else {
            x = (int) s_commandsBlueSpawnPoint.x;
            y = (int) s_commandsBlueSpawnPoint.y;
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

    public void stepping(UserProfile userProfile){
        timeUntilMove = timeUntilMove > 0 ? timeUntilMove-STEP_TIME : 0;

        StringBuilder abilityStatus = new StringBuilder();
        abilityStatus.append('[');
        int amountAbility = 0;
        for (Map.Entry<String, Ability> entry : abilities.entrySet())
        {
            if(amountAbility != 0){
                abilityStatus.append(',');
            }
            abilityStatus.append("{\"name\": \"");
            abilityStatus.append(entry.getKey());
            abilityStatus.append("\", \"time\": ");
            abilityStatus.append((double)entry.getValue().getCooldown() / 1000);
            entry.getValue().stepping();
            abilityStatus.append('}');
            ++amountAbility;
        }
        abilityStatus.append(']');
        JSONObject request = new JSONObject();
        request.put("type", "abilityStatus");
        request.put("abilityStatus", abilityStatus.toString());
        userProfile.getUserSocket().sendMessage(request.toString());
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
            default: break;
        }
    }


    public void affect(AbilityAction action){
        action.run(this);
    }

    public void makeDamage(int damage){
        hitPoints -= damage;
        if(hitPoints < 0){
            goToSpawn();
            hitPoints = MAX_HIT_POINTS;
        }
        if(hitPoints > MAX_HIT_POINTS){
            hitPoints = MAX_HIT_POINTS;
        }
    }

    public void useAbility(String abilityName){
        if(target == null) return;
        Ability ability;
        try{
            ability = abilities.get(abilityName);
        } catch (RuntimeException e){
            return;
        }
        if(ability == null) return;

        if(ability instanceof AttackAbility) {
            if (!ability.isCAN_ATTACK_TEAMMATE() && target.command.equals(command)) {
                return;
            }
        }
        if(ability instanceof HealingAbility) {
            if (!ability.isCAN_HEALING_OPPONENT() && !target.command.equals(command)) {
                return;
            }
        }
        Vec2d t = target.getCoord();
        Vec2d p = getCoord();
        if((int)Math.sqrt((t.x - p.x)*(t.x-p.x) + (t.y - p.y)*(t.y - p.y)) > ability.getRange()){
            return;
        }
        AbilityAction action = ability.use();
        if(action != null) {
            target.affect(action);
        }
    }

    public int getHitPoints(){
        return hitPoints;
    }

    public boolean isHaveTarget(){
        return target != null;
    }

    public int getTargetsHitPoints(){
        if(target!=null) {
            return target.hitPoints;
        }
        return 0;
    }
}
