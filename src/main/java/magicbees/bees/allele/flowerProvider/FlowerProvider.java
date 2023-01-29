package magicbees.bees.allele.flowerProvider;

import java.util.Set;

import net.minecraft.block.Block;

import forestry.api.apiculture.FlowerManager;
import forestry.api.genetics.IFlower;
import forestry.api.genetics.IFlowerGrowthRule;
import forestry.api.genetics.IFlowerProvider;

public abstract class FlowerProvider implements IFlowerProvider {

    @Override
    @Deprecated
    public Set<IFlower> getFlowers() {
        return FlowerManager.flowerRegistry.getAcceptableFlowers(getFlowerType());
    }

    protected void registerPlantableFlower(Block block, int meta, int weight) {
        FlowerManager.flowerRegistry.registerPlantableFlower(block, meta, weight, getFlowerType());
    }

    protected void registerAcceptableFlower(Block block, int meta) {
        FlowerManager.flowerRegistry.registerAcceptableFlower(block, meta, getFlowerType());
    }

    protected void registerGrowthRule(IFlowerGrowthRule growthRule) {
        FlowerManager.flowerRegistry.registerGrowthRule(growthRule, getFlowerType());
    }
}
