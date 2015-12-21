package mechanics.ability;

import mechanics.abilityAction.AbilityAction;
import org.jetbrains.annotations.Nullable;
import resource.ServerConfiguration;

/**
 * Created by Андрей on 01.11.2015.
 */
public abstract class Ability {
    protected static final int STEP_TIME = ServerConfiguration.getInstance().getStepTime();
    protected int cooldown = 0;
    protected boolean can_attack_temmate = false;
    protected boolean can_healing_opponent = false;
    protected int range = 1;
    private int currentCooldown = 0;

    public int getCurrentCooldown(){
        return currentCooldown;
    }

    public int getRange(){
        return range;
    }

    public boolean isCanAttackTemmate() {
        return can_attack_temmate;
    }

    public boolean isCanHealingOpponent() {
        return can_healing_opponent;
    }

    public void stepping(){
        currentCooldown -= STEP_TIME;
        if(currentCooldown < 0) currentCooldown = 0;
    }

    public boolean isAvailable(){
        return currentCooldown <= 0;
    }

    @Nullable
    protected AbilityAction getAction(){
        return null;
    }

    @Nullable
    public final AbilityAction use(){
        if(!isAvailable()){
            return null;
        } else {
            AbilityAction action = getAction();
            if(action == null){
                return null;
            } else {
                currentCooldown = cooldown;
                return action;
            }
        }
    }
}
