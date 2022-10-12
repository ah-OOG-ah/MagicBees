package magicbees.main.utils.compat.botania;

import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import magicbees.bees.BeeManager;
import magicbees.bees.BeeSpecies;
import magicbees.main.utils.compat.ForestryHelper;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.recipe.RecipeManaInfusion;

public class SpeciesRecipeManaInfusion extends RecipeManaInfusion {

    private IAlleleBeeSpecies outputSpecies;
    private IAlleleBeeSpecies inputSpecies;
    private EnumBeeType beeType;

    private ItemStack outputCache;

    public SpeciesRecipeManaInfusion(BeeSpecies outSpecies, BeeSpecies inSpecies, int mana, EnumBeeType type) {
        super(
                BeeManager.getDefaultItemStackForSpecies(outSpecies.getSpecies(), type),
                BeeManager.getDefaultItemStackForSpecies(inSpecies.getSpecies(), type),
                mana);
        outputSpecies = outSpecies.getSpecies();
        inputSpecies = inSpecies.getSpecies();
        beeType = type;
        outputCache = super.getOutput();
    }

    @Override
    public boolean matches(ItemStack stack) {
        if (!BeeManager.beeRoot.isMember(stack, beeType.ordinal())) {
            return false;
        }

        IBee bee = BeeManager.beeRoot.getMember(stack);
        if (bee.getGenome().getPrimary().equals(inputSpecies)) {
            outputCache = ForestryHelper.replaceSpecies(stack, outputSpecies);
            outputCache.stackSize = 1;
            return true;
        }

        return false;
    }

    @Override
    public ItemStack getOutput() {
        ItemStack currentOutput = outputCache;
        outputCache = super.getOutput();
        return currentOutput;
    }
}
