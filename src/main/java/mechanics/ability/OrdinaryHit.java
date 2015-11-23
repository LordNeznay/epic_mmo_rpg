package mechanics.ability;

import mechanics.Ability;
import mechanics.AbilityAction;
import mechanics.AttackAbility;
import mechanics.abilityAction.OrdinaryHitAction;
import resource.Configuration;

/**
 * Created by Андрей on 01.11.2015.
 */
public final class OrdinaryHit extends Ability implements AttackAbility{

    public OrdinaryHit(){
        cooldown = Configuration.getInstance().getAbilityCooldown("OrdinaryHit");
        range = Configuration.getInstance().getAbilityRange("OrdinaryHit");
    }

    @Override
    protected AbilityAction getAction(){
        return new OrdinaryHitAction();
    }
}
