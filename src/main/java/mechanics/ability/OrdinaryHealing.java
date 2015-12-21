package mechanics.ability;

import mechanics.abilityAction.AbilityAction;
import mechanics.abilityAction.OrdinaryHealingAction;
import resource.ServerConfiguration;

/**
 * Created by Андрей on 02.11.2015.
 */
public class OrdinaryHealing extends Ability implements HealingAbility {
    public OrdinaryHealing(){
        cooldown = ServerConfiguration.getInstance().getAbilityCooldown("OrdinaryHealing");
        range = ServerConfiguration.getInstance().getAbilityRange("OrdinaryHealing");
    }

    @Override
    protected AbilityAction getAction(){
        return new OrdinaryHealingAction();
    }
}
