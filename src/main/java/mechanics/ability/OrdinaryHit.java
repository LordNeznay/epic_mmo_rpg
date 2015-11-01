package mechanics.ability;

import mechanics.Ability;
import mechanics.AbilityAction;
import mechanics.AttackAbility;
import mechanics.abilityAction.OrdinaryHitAction;

import javax.swing.*;

/**
 * Created by Андрей on 01.11.2015.
 */
public final class OrdinaryHit extends Ability implements AttackAbility{

    public OrdinaryHit(){
        ROLLBACK = 1000;
        RANGE = 1;
    }

    @Override
    protected AbilityAction getAction(){
        return new OrdinaryHitAction();
    }
}
