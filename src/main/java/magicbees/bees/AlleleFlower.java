package magicbees.bees;

import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.genetics.IAlleleFlowers;
import forestry.api.genetics.IFlowerProvider;

public class AlleleFlower extends Allele implements IAlleleFlowers {
    private IFlowerProvider provider;

    public AlleleFlower(String uid, IFlowerProvider flowerProvider, boolean isDominant) {
        super("flower" + uid, isDominant, EnumBeeChromosome.FLOWER_PROVIDER);
        this.provider = flowerProvider;
    }

    @Override
    public IFlowerProvider getProvider() {
        return this.provider;
    }

    @Override
    public String getName() {
        return this.provider.getDescription();
    }
}
