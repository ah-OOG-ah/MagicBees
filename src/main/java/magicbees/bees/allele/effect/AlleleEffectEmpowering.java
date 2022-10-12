package magicbees.bees.allele.effect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cpw.mods.fml.common.Optional;
import gregtech.api.interfaces.IAlleleBeeAcceleratableEffect;
import magicbees.bees.AlleleEffect;
import magicbees.bees.BeeManager;
import magicbees.main.Config;
import magicbees.main.utils.BlockUtil;
import magicbees.main.utils.compat.thaumcraft.NodeHelper;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.genetics.IEffectData;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;

import static magicbees.main.utils.compat.thaumcraft.NodeHelper.getWeightedRandomAspect;
import static magicbees.main.utils.compat.thaumcraft.NodeHelper.updateNode;

@Optional.Interface(iface = "gregtech.api.interfaces.IAlleleBeeAcceleratableEffect", modid = "gregtech")
public class AlleleEffectEmpowering extends AlleleEffect implements IAlleleBeeAcceleratableEffect {

	public AlleleEffectEmpowering(String id, boolean isDominant) {
		super(id, isDominant, 200);
		combinable = true;
	}

	@Override
	public IEffectData validateStorage(IEffectData storedData) {
		if (!(storedData instanceof EffectData)) {
			storedData = new magicbees.bees.allele.effect.EffectData(1, 0, 0);
		}
		return storedData;
	}

	@Override
	protected IEffectData doEffectThrottled(IBeeGenome genome, IEffectData storedData, IBeeHousing housing) {
		World world = housing.getWorld();
		ChunkCoordinates coords = housing.getCoordinates();
		IBeeModifier beeModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);
		int range = Math.max((int)Math.ceil(genome.getTerritory()[0] * beeModifier.getTerritoryModifier(genome, 1f)), 1);
		List<Chunk> chunks = BlockUtil.getChunksInSearchRange(world, coords.posX, coords.posZ, range);

        if (NodeHelper.growNodeInRange(chunks, world, coords.posX, coords.posY, coords.posZ, range)) {
        	storedData.setInteger(0, storedData.getInteger(0) - throttle);
        }

		return storedData;
	}

    @Optional.Method(modid = "gregtech")
    @Override
    public IEffectData doEffectAccelerated(IBeeGenome genome, IEffectData storedData, IBeeHousing housing, float did) {
        storedData.setInteger(0, 0);
        World world = housing.getWorld();
        ChunkCoordinates coords = housing.getCoordinates();
        IBeeModifier beeModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);
        int range = Math.max((int)Math.ceil(genome.getTerritory()[0] * beeModifier.getTerritoryModifier(genome, 1f)), 1);
        List<Chunk> chunks = BlockUtil.getChunksInSearchRange(world, coords.posX, coords.posZ, range);

        int idid = (int)did;

        List<INode> nodes = new ArrayList<>();
        Collections.shuffle(chunks);
        for(Chunk chunk : chunks) {
            List<INode> l = NodeHelper.findNodesInChunkWithinRange(chunk, coords.posX, coords.posY, coords.posZ, range);
            nodes.addAll(l);
            idid -= l.size();
            if(idid <= 0)
            {
                while(idid < 0)
                {
                    nodes.remove(0);
                    idid++;
                }
                break;
            }
        }

        if(nodes.isEmpty())
            return storedData;

        did /= (float) nodes.size();
        if(did < 1f)
            did = 1f;


        for(INode node : nodes){
            AspectList aspectsBase = node.getAspectsBase();
            int randBase = Math.max(aspectsBase.visSize(), 1);
            if (world.rand.nextInt(randBase) < 120) {
                Aspect aspectToAdd;
                int rollAttempts = 0;
                do {
                    aspectToAdd = getWeightedRandomAspect(world.rand);
                    ++rollAttempts;
                }
                while (aspectsBase.getAmount(aspectToAdd) > (Config.thaumcraftNodeMaxSize -1) && rollAttempts < 20);

                if (20 <= rollAttempts) {
                    continue;
                }

                short amount = (short)(1 + world.rand.nextInt((int)(2f * did)));
                aspectsBase.add(aspectToAdd, amount);
                node.getAspects().add(aspectToAdd, amount);
                updateNode(node, world);
            }
        }

        return storedData;
    }
}
