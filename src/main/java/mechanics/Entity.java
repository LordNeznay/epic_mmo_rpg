package mechanics;

import com.sun.javafx.geom.Vec2d;
import main.UserProfile;
import mechanics.ability.*;
import mechanics.abilityAction.AbilityAction;
import mechanics.gameMap.GameMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.ResponseConstructor;
import utils.ResponseHeaders;
import resource.ServerConfiguration;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Андрей on 27.10.2015.
 */
public class Entity {
    private static final int MAX_HIT_POINTS = ServerConfiguration.getInstance().getMaxHitPoints();
    private static final int STEP_TIME = ServerConfiguration.getInstance().getStepTime();
    private static final int MOVE_DELAY = ServerConfiguration.getInstance().getMoveDelay();
    private static final int ABILITY_DELAY = ServerConfiguration.getInstance().getAbilityDelay();
    private static Vec2d s_commandsRedSpawnPoint = new Vec2d(0, 0);
    private static Vec2d s_commandsBlueSpawnPoint = new Vec2d(0, 0);
    private int hitPoints = MAX_HIT_POINTS;
    private int x = 0;
    private int lastX = 0;
    private int y = 0;
    private int lastY = 0;
    private String command = "";
    private GameMap map = null;
    private int timeUntilMove = 0;
    private Entity target = null;
    private Map<String, Ability> abilities = new HashMap<>();
    private int abilityDelay = 0;
    private int number = 0;
    private String directAbility = "none";

    public Entity(@NotNull GameMap _map){
        map = _map;
        abilities.put("Healing", new OrdinaryHealing());
        abilities.put("Hit", new OrdinaryHit());
    }

    public String getDirectAbility(){
        return directAbility;
    }

    public Entity getTarget() {
        return target;
    }

    public void setNumber(int _number){
        number = _number;
    }

    public int getNumber(){
        return number;
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

    private void moving(Vec2d destination){
        lastX = x;
        lastY = y;
        x = (int)destination.x;
        y = (int)destination.y;
        map.updatePositionEntity(this, lastX, lastY);
    }

    private void goToSpawn(){
        if(command.equals("CommandRed")){
            moving(new Vec2d(s_commandsRedSpawnPoint.x, s_commandsRedSpawnPoint.y));
        } else {
            moving(new Vec2d(s_commandsBlueSpawnPoint.x, s_commandsBlueSpawnPoint.y));
        }
    }

    public void setCommand(String newCommand){
        command = newCommand;
        goToSpawn();
    }

    public String getCommand() {
        return command;
    }

    public boolean isCoordChange(){
        return !(lastX == x && lastY == y);
    }

    public void steppingAllAbility(){
        for (Map.Entry<String, Ability> entry : abilities.entrySet()) {
            entry.getValue().stepping();
        }
    }

    public void stepping(UserProfile userProfile){
        lastX = x;
        lastY = y;
        steppingAllAbility();
        timeUntilMove = timeUntilMove > 0 ? timeUntilMove-STEP_TIME : 0;
        abilityDelay = abilityDelay > 0 ? abilityDelay-STEP_TIME : 0;
        sendAbilityStatus(userProfile);
        directAbility = "none";
    }

    private void sendAbilityStatus(UserProfile userProfile){
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
            int abilityCooldown = entry.getValue().getCurrentCooldown();
            if(abilityDelay != 0 && abilityCooldown<= abilityDelay) {
                abilityCooldown = abilityDelay;
            }
            abilityStatus.append((double) abilityCooldown/ 1000);
            abilityStatus.append('}');
            ++amountAbility;
        }
        abilityStatus.append(']');

        String response = ResponseConstructor.getResponse(ResponseHeaders.ABILITY_STATUS, abilityStatus.toString());
        userProfile.addMessageForSending(response);
    }

    public void move(String params){
        if(timeUntilMove > 0) return;
        switch (params){
            case "up":
                if(map.isPassability(new Vec2d(x, y-1))) {
                    moving(new Vec2d(x, y-1));
                    timeUntilMove = MOVE_DELAY;
                }
                break;
            case "down":
                if(map.isPassability(new Vec2d(x, y+1))) {
                    moving(new Vec2d(x, y+1));
                    timeUntilMove = MOVE_DELAY;
                }
                break;
            case "left":
                if(map.isPassability(new Vec2d(x-1, y))) {
                    moving(new Vec2d(x-1, y));
                    timeUntilMove = MOVE_DELAY;
                }
                break;
            case "right":
                if(map.isPassability(new Vec2d(x+1, y))) {
                    moving(new Vec2d(x+1, y));
                    timeUntilMove = MOVE_DELAY;
                }
                break;
            default: break;
        }
    }


    public void affect(@Nullable AbilityAction action){
        if(action != null) {
            action.run(this);
        }
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

    private boolean isDistanceInRangeAbility(Ability ability, Vec2d targetPosition, Vec2d startPosition){
        return (int) Math.sqrt((targetPosition.x - startPosition.x) * (targetPosition.x - startPosition.x) + (targetPosition.y - startPosition.y) * (targetPosition.y - startPosition.y)) <= ability.getRange();
    }

    private boolean isMayUseAbility(Ability ability){
        if(ability instanceof AttackAbility) {
            if (!ability.isCanAttackTemmate() && target.command.equals(command)) {
                return false;
            }
        }
        if(ability instanceof HealingAbility) {
            if (!ability.isCanHealingOpponent() && !target.command.equals(command)) {
                return false;
            }
        }
        return true;
    }

    public void useAbility(String abilityName){
        if(abilityDelay != 0){
            return;
        }
        if(target == null) return;
        if(!isAbilityExist(abilityName)){
            return;
        }
        Ability ability = abilities.get(abilityName);

        if(!isMayUseAbility(ability)){
            return;
        }
        if(!isDistanceInRangeAbility(ability, target.getCoord(), getCoord())){
            return;
        }
        AbilityAction action = ability.use();
        if(action != null) {
            Vec2d trg = target.getCoord();
            if(trg.x > x) {
                if (trg.y == y) {
                    directAbility = "rr";
                } else if (trg.y > y) {
                    directAbility = "rb";
                } else if (trg.y < y) {
                    directAbility = "rt";
                }
            } else if(trg.x < x){
                if (trg.y == y) {
                    directAbility = "lr";
                } else if (trg.y > y) {
                    directAbility = "lb";
                } else if (trg.y < y) {
                    directAbility = "lt";
                }
            } else {
                if (trg.y == y) {
                    directAbility = "cr";
                } else if (trg.y > y) {
                    directAbility = "cb";
                } else if (trg.y < y) {
                    directAbility = "ct";
                }
            }
            target.affect(action);
            abilityDelay = ABILITY_DELAY;
        }
    }

    public void addAbility(String abilityName, Ability ability){
        if(!isAbilityExist(abilityName)){
            abilities.put(abilityName, ability);
        }
    }

    public boolean isAbilityExist(String abilityName){
        Ability ability;
        try{
            ability = abilities.get(abilityName);
        } catch (RuntimeException e){
            return false;
        }
        return ability != null;
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
