package magicbees.bees.allele.flowerProvider;

import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IPollinatable;
import java.util.EnumSet;
import magicbees.main.utils.LocalizationManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.oredict.OreDictionary;

public class FlowerProviderBookshelf extends FlowerProvider {

    public FlowerProviderBookshelf() {
        registerAcceptableFlower(Blocks.bookshelf, OreDictionary.WILDCARD_VALUE);
    }

    @Override
    @Deprecated
    public boolean growFlower(World world, IIndividual genome, int x, int i, int j) {
        return true;
    }

    @Override
    public String getFlowerType() {
        return "flowersBookshelf";
    }

    @Override
    public ItemStack[] affectProducts(World world, IIndividual genome, int x, int i, int j, ItemStack products[]) {
        return products;
    }

    @Override
    public boolean isAcceptedPollinatable(World world, IPollinatable pollinatable) {
        EnumSet<EnumPlantType> types = pollinatable.getPlantType();
        return types.size() > 0 && !types.contains(EnumPlantType.Nether) && !types.contains(EnumPlantType.Water);
    }

    @Override
    public String getDescription() {
        return LocalizationManager.getLocalizedString("flowerProvider.book");
    }
}
