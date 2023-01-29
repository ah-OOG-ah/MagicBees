package magicbees.bees.allele.flowerProvider;

import magicbees.main.utils.LocalizationManager;
import magicbees.main.utils.compat.ThaumcraftHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import forestry.api.genetics.IFlowerGrowthHelper;
import forestry.api.genetics.IFlowerGrowthRule;
import forestry.api.genetics.IFlowerRegistry;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IPollinatable;

public class FlowerProviderThaumcraftFlower extends FlowerProvider implements IFlowerGrowthRule {

    public FlowerProviderThaumcraftFlower() {
        registerPlantableFlower(ThaumcraftHelper.plant, ThaumcraftHelper.BlockPlant.SHIMMERLEAF.ordinal(), 1);
        registerPlantableFlower(ThaumcraftHelper.plant, ThaumcraftHelper.BlockPlant.CINDERPEARL.ordinal(), 1);
        registerGrowthRule(this);
    }

    @Override
    public String getFlowerType() {
        return "flowersThaumcraft";
    }

    @Override
    public String getDescription() {
        return LocalizationManager.getLocalizedString("flowerProvider.magic");
    }

    @Override
    public ItemStack[] affectProducts(World world, IIndividual genome, int x, int y, int z, ItemStack[] products) {
        return products;
    }

    @Override
    public boolean isAcceptedPollinatable(World world, IPollinatable pollinatable) {
        return 0 < pollinatable.getPlantType().size();
    }

    @Override
    @Deprecated
    public boolean growFlower(World world, IIndividual genome, int x, int y, int z) {
        return growFlower(world, x, y, z);
    }

    @Override
    public boolean growFlower(IFlowerGrowthHelper helper, String flowerType, World world, int x, int y, int z) {
        return growFlower(world, x, y, z);
    }

    @Override
    @Deprecated
    public boolean growFlower(IFlowerRegistry fr, String flowerType, World world, IIndividual individual, int x, int y,
            int z) {
        return growFlower(world, x, y, z);
    }

    private boolean growFlower(World world, int x, int y, int z) {
        boolean flag = false;
        Block blockDown = world.getBlock(x, y - 1, z);
        if (world.getBlock(x, y, z).isAir(world, x, y, z)) {
            if (blockDown == Blocks.dirt || blockDown == Blocks.grass) {
                world.setBlock(x, y, z, ThaumcraftHelper.plant, ThaumcraftHelper.BlockPlant.SHIMMERLEAF.ordinal(), 2);
                flag = true;
            } else if (blockDown == Blocks.sand) {
                world.setBlock(x, y, z, ThaumcraftHelper.plant, ThaumcraftHelper.BlockPlant.CINDERPEARL.ordinal(), 2);
                flag = true;
            }
        }
        return flag;
    }
}
