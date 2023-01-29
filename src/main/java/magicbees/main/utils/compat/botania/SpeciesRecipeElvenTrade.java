package magicbees.main.utils.compat.botania;

import java.util.LinkedList;
import java.util.List;

import magicbees.bees.BeeManager;
import magicbees.bees.BeeSpecies;

import net.minecraft.item.ItemStack;

import vazkii.botania.api.recipe.RecipeElvenTrade;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;

public class SpeciesRecipeElvenTrade extends RecipeElvenTrade {

    private IAlleleBeeSpecies inputSpecies;

    public SpeciesRecipeElvenTrade(BeeSpecies inSpecies, BeeSpecies outSpecies) {
        super(
                BeeManager.getDefaultItemStackForSpecies(outSpecies.getSpecies(), EnumBeeType.DRONE),
                BeeManager.getDefaultItemStackForSpecies(inSpecies.getSpecies(), EnumBeeType.DRONE));
        inputSpecies = inSpecies.getSpecies();
    }

    @Override
    public boolean matches(List<ItemStack> stacks, boolean removeMatched) {
        List<ItemStack> matchedStacks = new LinkedList<ItemStack>();
        boolean found = false;

        for (ItemStack inStack : stacks) {
            if (inStack == null) {
                continue;
            }

            if (BeeManager.beeRoot.isMember(inStack, EnumBeeType.DRONE.ordinal())) {
                IBee bee = BeeManager.beeRoot.getMember(inStack);
                if (bee.getGenome().getPrimary().equals(inputSpecies)) {
                    matchedStacks.add(inStack);
                    found = true;
                    break;
                }
            }
        }

        if (removeMatched) {
            for (ItemStack stack : matchedStacks) {
                stacks.remove(stack);
            }
        }

        return found;
    }
}
