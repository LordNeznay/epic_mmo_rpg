package mechanics.abilityAction;

import mechanics.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Андрей on 24.11.2015.
 */
public class NoneEffectAction extends AbilityAction{
    @Override
    public void run(@NotNull Entity entity){
        //do nothing
    }

}
