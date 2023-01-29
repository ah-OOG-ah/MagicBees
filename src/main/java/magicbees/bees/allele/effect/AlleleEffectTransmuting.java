package magicbees.bees.allele.effect;

import magicbees.bees.AlleleEffect;
import magicbees.bees.BeeManager;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.genetics.IEffectData;

public class AlleleEffectTransmuting extends AlleleEffect {

    private TransmutationEffectController transmutationController;

    public AlleleEffectTransmuting(String id, boolean isDominant, TransmutationEffectController effectController,
            int timeoutBeeTicks) {
        super(id, isDominant, timeoutBeeTicks);
        this.transmutationController = effectController;
    }

    @Override
    public IEffectData validateStorage(IEffectData storedData) {
        if (storedData == null || !(storedData instanceof magicbees.bees.allele.effect.EffectData)) {
            storedData = new magicbees.bees.allele.effect.EffectData(1, 0, 0);
        }
        return storedData;
    }

    @Override
    protected IEffectData doEffectThrottled(IBeeGenome genome, IEffectData storedData, IBeeHousing housing) {
        World world = housing.getWorld();
        ChunkCoordinates coords = housing.getCoordinates();
        IBeeModifier beeModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);

        // Get random coords within territory
        int xRange = (int) (beeModifier.getTerritoryModifier(genome, 1f) * genome.getTerritory()[0]);
        int yRange = (int) (beeModifier.getTerritoryModifier(genome, 1f) * genome.getTerritory()[1]);
        int zRange = (int) (beeModifier.getTerritoryModifier(genome, 1f) * genome.getTerritory()[2]);

        int xCoord = coords.posX + world.rand.nextInt(xRange) - xRange / 2;
        int yCoord = coords.posY + world.rand.nextInt(yRange) - yRange / 2;
        int zCoord = coords.posZ + world.rand.nextInt(zRange) - zRange / 2;

        BiomeGenBase biome = world.getBiomeGenForCoords(xCoord, zCoord);
        transmutationController.attemptTransmutations(
                world,
                biome,
                new ItemStack(
                        world.getBlock(xCoord, yCoord, zCoord),
                        1,
                        world.getBlockMetadata(xCoord, yCoord, zCoord)),
                xCoord,
                yCoord,
                zCoord);

        storedData.setInteger(0, 0);

        return storedData;
    }
}
