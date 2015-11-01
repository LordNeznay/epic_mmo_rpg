package mechanics.ability;

import mechanics.Ability;
import mechanics.AbilityAction;
import mechanics.HealingAbility;
import mechanics.abilityAction.OrdinaryHealingAction;

/**
 * Created by Андрей on 02.11.2015.
 */
public class OrdinaryHealing extends Ability implements HealingAbility {
    public OrdinaryHealing(){
        COOLDOWN = 1000;
        RANGE = 3;
    }

    @Override
    protected AbilityAction getAction(){
        return new OrdinaryHealingAction();
    }
}
