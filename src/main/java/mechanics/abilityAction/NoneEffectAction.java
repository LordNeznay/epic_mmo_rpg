package mechanics.abilityAction;

import mechanics.AbilityAction;
import mechanics.Entity;
import org.jetbrains.annotations.NotNull;
import utils.Helper;

/**
 * Created by Андрей on 24.11.2015.
 */
public class NoneEffectAction extends AbilityAction{
    @Override
    public void run(@NotNull Entity entity){
        //do nothing
    }

}
