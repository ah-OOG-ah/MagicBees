package magicbees.bees.allele.effect;

import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.genetics.IEffectData;
import java.util.List;
import magicbees.bees.AlleleEffect;
import magicbees.bees.BeeManager;
import magicbees.main.utils.BlockUtil;
import magicbees.main.utils.compat.thaumcraft.NodeHelper;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import thaumcraft.api.nodes.NodeType;

public class AlleleEffectNodeConversion extends AlleleEffect {

    private NodeType targetType;

    public AlleleEffectNodeConversion(String id, NodeType targetNodeType, boolean isDominant, int timeout) {
        super(id, isDominant, timeout);
        targetType = targetNodeType;
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
        int range = (int) Math.ceil(genome.getTerritory()[0] * beeModifier.getTerritoryModifier(genome, 1f));
        List<Chunk> chunks = BlockUtil.getChunksInSearchRange(world, coords.posX, coords.posZ, range);

        if (NodeHelper.convertNodeInRangeToType(
                chunks, world, coords.posX, coords.posY, coords.posZ, range, targetType)) {
            storedData.setInteger(0, storedData.getInteger(0) - throttle);
        }

        return storedData;
    }
}
