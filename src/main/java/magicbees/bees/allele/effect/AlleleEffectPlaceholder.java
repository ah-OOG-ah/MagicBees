package magicbees.bees.allele.effect;

import magicbees.bees.AlleleEffect;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.genetics.IEffectData;

public class AlleleEffectPlaceholder extends AlleleEffect {

    public AlleleEffectPlaceholder(String id, boolean isDominant) {
        super(id, isDominant, 100000);
    }

    @Override
    public IEffectData validateStorage(IEffectData storedData) {
        return storedData;
    }

    @Override
    protected IEffectData doEffectThrottled(IBeeGenome genome, IEffectData storedData, IBeeHousing housing) {
        return storedData;
    }
}
