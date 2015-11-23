package mechanics.ability;

import mechanics.Ability;
import mechanics.AbilityAction;
import mechanics.HealingAbility;
import mechanics.abilityAction.OrdinaryHealingAction;
import resource.Configuration;

/**
 * Created by Андрей on 02.11.2015.
 */
public class OrdinaryHealing extends Ability implements HealingAbility {
    public OrdinaryHealing(){
        cooldown = Configuration.getInstance().getAbilityCooldown("OrdinaryHealing");
        range = Configuration.getInstance().getAbilityRange("OrdinaryHealing");
    }

    @Override
    protected AbilityAction getAction(){
        return new OrdinaryHealingAction();
    }
}
