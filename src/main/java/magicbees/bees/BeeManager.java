package magicbees.bees;

import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import magicbees.block.types.HiveType;
import magicbees.item.types.DropType;
import magicbees.main.Config;
import magicbees.main.utils.Tuple;
import magicbees.main.utils.compat.ExtraBeesHelper;
import net.minecraft.item.ItemStack;

public class BeeManager {
    public static IBeeRoot beeRoot;

    private static List<Tuple<IAlleleBeeSpecies, Double>> worldgenSpeciesWeights =
            new ArrayList<Tuple<IAlleleBeeSpecies, Double>>();
    private static double worldgenSpeciesWeightsTotal = 0;

    public static void getBeeRoot() {
        beeRoot = (IBeeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootBees");
    }

    public static void setupAlleles() {
        Allele.setupAdditionalAlleles();
        BeeSpecies.setupBeeSpecies();
        Allele.registerDeprecatedAlleleReplacements();
    }

    public static void lateBeeInit() {
        BeeMutation.setupMutations();
        BeeProductHelper.initBaseProducts();
        BeeProductHelper.initOreDictSProducts();

        HiveType.initHiveData();

        beeRoot.setResearchSuitability(Config.drops.getStackForType(DropType.INTELLECT), 0.5f);
    }

    public static ItemStack getDefaultItemStackForSpecies(IAlleleBeeSpecies beeSpecies, EnumBeeType type) {
        IBee bee = getBeeFromSpecies(beeSpecies, false);
        return BeeManager.beeRoot.getMemberStack(bee, type.ordinal());
    }

    public static IBee getBeeFromSpecies(IAlleleBeeSpecies beeSpecies, boolean applyRainResist) {
        IAllele[] speciesTemplate = BeeManager.beeRoot.getTemplate(beeSpecies.getUID());

        if (applyRainResist) {
            speciesTemplate = BeeGenomeManager.addRainResist(speciesTemplate);
        }

        IBeeGenome genome = BeeManager.beeRoot.templateAsGenome(speciesTemplate);
        return BeeManager.beeRoot.getBee(null, genome);
    }

    public static IAlleleBeeSpecies getRandomWorldgenSpecies(Random r) {
        Collections.shuffle(worldgenSpeciesWeights);
        double value = r.nextDouble() * worldgenSpeciesWeightsTotal;
        IAlleleBeeSpecies species = Allele.getBaseSpecies("Forest");

        for (Tuple<IAlleleBeeSpecies, Double> t : worldgenSpeciesWeights) {
            value -= t.right.doubleValue();
            if (value <= 0.0) {
                species = t.left;
                break;
            }
        }

        return species;
    }

    public static void addWorldgenSpeciesWeight(IAlleleBeeSpecies species, double d) {
        worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(species, d));
        worldgenSpeciesWeightsTotal += d;
    }

    public static void populateSpeciesListRarity() {
        worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(Allele.getBaseSpecies("Forest"), 20.0));
        worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(Allele.getBaseSpecies("Meadows"), 20.0));
        worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(Allele.getBaseSpecies("Tropical"), 10.0));
        worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(Allele.getBaseSpecies("Modest"), 16.0));
        worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(Allele.getBaseSpecies("Wintry"), 10.0));
        worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(Allele.getBaseSpecies("Ended"), 0.5));

        if (ExtraBeesHelper.isActive()) {
            worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(Allele.getExtraSpecies("Water"), 12.0));
            worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(Allele.getExtraSpecies("Rock"), 16.0));
            worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(Allele.getExtraSpecies("Basalt"), 10.0));
        }

        worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(BeeSpecies.MYSTICAL.getSpecies(), 20.0));
        worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(BeeSpecies.UNUSUAL.getSpecies(), 20.0));
        worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(BeeSpecies.SORCEROUS.getSpecies(), 13.0));
        worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(BeeSpecies.ATTUNED.getSpecies(), 6.0));
        worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(BeeSpecies.INFERNAL.getSpecies(), 10.0));
        worldgenSpeciesWeights.add(new Tuple<IAlleleBeeSpecies, Double>(BeeSpecies.OBLIVION.getSpecies(), 1.0));

        for (Tuple<IAlleleBeeSpecies, Double> t : worldgenSpeciesWeights) {
            worldgenSpeciesWeightsTotal += t.right.doubleValue();
        }
    }
}
