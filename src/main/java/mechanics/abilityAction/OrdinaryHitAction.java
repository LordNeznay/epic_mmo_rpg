package mechanics.abilityAction;

import mechanics.Entity;
import org.jetbrains.annotations.NotNull;
import utils.Helper;

/**
 * Created by Андрей on 01.11.2015.
 */
public final class OrdinaryHitAction extends AbilityAction{
    @Override
    public void run(@NotNull Entity entity){
        entity.makeDamage(Helper.randomInt(100, 200));
    }
}
