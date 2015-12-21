package mechanics.ability;

import mechanics.abilityAction.AbilityAction;
import mechanics.abilityAction.NoneEffectAction;

/**
 * Created by Андрей on 24.11.2015.
 */
public class NoneEffectAbility extends Ability implements HealingAbility {
    public NoneEffectAbility(){
        cooldown = 0;
        range = 1;
    }

    @Override
    protected AbilityAction getAction(){
        return new NoneEffectAction();
    }
}
