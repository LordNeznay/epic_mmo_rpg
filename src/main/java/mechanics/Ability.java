package mechanics;

/**
 * Created by Андрей on 01.11.2015.
 */
public abstract class Ability {
    protected static final int STEP_TIME = 100;
    protected int COOLDOWN = 0;
    protected boolean CAN_ATTACK_TEAMMATE = false;
    protected boolean CAN_HEALING_OPPONENT = false;
    protected int RANGE = 1;
    private int cooldown = 0;

    public int getRange(){
        return RANGE;
    }

    public boolean isCAN_ATTACK_TEAMMATE() {
        return CAN_ATTACK_TEAMMATE;
    }

    public boolean isCAN_HEALING_OPPONENT() {
        return CAN_HEALING_OPPONENT;
    }

    public void stepping(){
        cooldown -= STEP_TIME;
        if(cooldown < 0) cooldown = 0;
    }

    public boolean isAvailable(){
        if(cooldown > 0){
            return false;
        }
        return true;
    }

    protected AbilityAction getAction(){
        return null;
    }

    public final AbilityAction use(){
        if(!isAvailable()){
            return null;
        } else {
            AbilityAction action = getAction();
            if(action == null){
                return null;
            } else {
                cooldown = COOLDOWN;
                return action;
            }
        }
    }
}
