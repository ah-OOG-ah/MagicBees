package magicbees.bees.allele.flowerProvider;

import forestry.api.genetics.IFlowerGrowthHelper;
import forestry.api.genetics.IFlowerGrowthRule;
import forestry.api.genetics.IFlowerRegistry;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IPollinatable;
import magicbees.main.utils.LocalizationManager;
import magicbees.main.utils.compat.BotaniaHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class FlowerProviderBotania extends FlowerProvider implements IFlowerGrowthRule {

    public FlowerProviderBotania() {
        registerPlantableFlower(BotaniaHelper.blockMysticalFlower, OreDictionary.WILDCARD_VALUE, 1);
        registerGrowthRule(this);
    }

    @Override
    @Deprecated
    public boolean growFlower(World world, IIndividual individual, int x, int y, int z) {
        return growFlower(world, x, y, z);
    }

    @Override
    public boolean growFlower(IFlowerGrowthHelper helper, String flowerType, World world, int x, int y, int z) {
        return growFlower(world, x, y, z);
    }

    @Override
    @Deprecated
    public boolean growFlower(
            IFlowerRegistry fr, String flowerType, World world, IIndividual individual, int x, int y, int z) {
        return growFlower(world, x, y, z);
    }

    private boolean growFlower(World world, int x, int y, int z) {
        if (world.isAirBlock(x, y, z) && BotaniaHelper.blockMysticalFlower.canPlaceBlockAt(world, x, y, z)) {
            world.setBlock(x, y, z, BotaniaHelper.blockMysticalFlower);
            world.setBlockMetadataWithNotify(x, y, z, world.rand.nextInt(16), 2);
        }
        return false;
    }

    @Override
    public ItemStack[] affectProducts(World world, IIndividual individual, int x, int y, int z, ItemStack[] products) {
        return products;
    }

    @Override
    public String getDescription() {
        return LocalizationManager.getLocalizedString("flowerProvider.botania");
    }

    @Override
    public String getFlowerType() {
        return "flowersBotania";
    }

    @Override
    public boolean isAcceptedPollinatable(World world, IPollinatable pollinatable) {
        return pollinatable.getPlantType().size() > 0;
    }
}
