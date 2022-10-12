package magicbees.bees.allele.flowerProvider;

import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IPollinatable;
import magicbees.main.utils.LocalizationManager;
import magicbees.main.utils.compat.ThaumcraftHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FlowerProviderAuraNode extends FlowerProvider {

    public FlowerProviderAuraNode() {
        registerAcceptableFlower(ThaumcraftHelper.airy, ThaumcraftHelper.AiryBlockType.NODE.ordinal());
        registerAcceptableFlower(ThaumcraftHelper.airy, ThaumcraftHelper.AiryBlockType.ENERGIZED_NODE.ordinal());
    }

    @Override
    public boolean isAcceptedPollinatable(World world, IPollinatable pollinatable) {
        return pollinatable.getPlantType().size() > 0;
    }

    @Override
    @Deprecated
    public boolean growFlower(World world, IIndividual genome, int x, int y, int z) {
        return true;
    }

    @Override
    public String getFlowerType() {
        return "flowersThaumcraftAuraNode";
    }

    @Override
    public String getDescription() {
        return LocalizationManager.getLocalizedString("flowerProvider.node");
    }

    @Override
    public ItemStack[] affectProducts(World world, IIndividual genome, int x, int y, int z, ItemStack[] products) {
        return products;
    }

    protected ItemStack[] addItemToProducts(ItemStack[] products, ItemStack itemStack) {
        for (ItemStack stack : products) {
            if (stack.getItem() == itemStack.getItem() && stack.getItemDamage() == itemStack.getItemDamage()) {
                if (stack.stackSize < stack.getItem().getItemStackLimit(stack)) {
                    stack.stackSize += itemStack.stackSize;
                    itemStack.stackSize =
                            Math.max(stack.stackSize - stack.getItem().getItemStackLimit(stack), 0);
                }
            }
        }

        if (itemStack.stackSize > 0) {
            ItemStack[] newProducts = new ItemStack[products.length + 1];
            for (int i = 0; i < products.length; ++i) {
                newProducts[i] = products[i];
            }
            newProducts[products.length] = itemStack;
            products = newProducts;
        }

        return products;
    }
}
