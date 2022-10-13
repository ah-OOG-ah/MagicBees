package magicbees.bees.allele.effect;

import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.genetics.IEffectData;
import magicbees.bees.AlleleEffect;
import magicbees.bees.BeeManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class AlleleEffectSpawnMobWeighted extends AlleleEffect {
    private String[] entityNames;
    private int[] spawnChance;

    public AlleleEffectSpawnMobWeighted(String id, boolean isDominant, int throttle, String[] mobs, int[] chance) {
        super(id, isDominant, throttle);
        if (mobs.length != chance.length) {
            throw new IllegalArgumentException("Array lengths must be equal!");
        }
        this.entityNames = mobs;
        this.spawnChance = chance;
    }

    @Override
    public IEffectData validateStorage(IEffectData storedData) {
        if (storedData == null || !(storedData instanceof magicbees.bees.allele.effect.EffectData)) {
            storedData = new EffectData(1, 0, 0);
        }
        return storedData;
    }

    @Override
    public IEffectData doEffectThrottled(IBeeGenome genome, IEffectData storedData, IBeeHousing housing) {
        if (this.spawnMob(genome, housing)) {
            storedData.setInteger(0, 0);
        }

        return storedData;
    }

    protected boolean spawnMob(IBeeGenome genome, IBeeHousing housing) {
        boolean flag = false;

        World w = housing.getWorld();
        int roll = 0;

        for (int i = 0; i < this.spawnChance.length && !flag; ++i) {
            roll = 100 - w.rand.nextInt(100) + 1;
            if (roll < this.spawnChance[i]) {
                Entity mob = EntityList.createEntityByName(this.entityNames[i], w);
                if (mob != null) {
                    flag = true;
                    double[] coords = this.randomMobSpawnCoords(w, genome, housing);

                    mob.setPositionAndRotation(coords[0], coords[1], coords[2], w.rand.nextFloat() * 360f, 0f);
                    if (mob instanceof EntityLiving) {
                        if (((EntityLiving) mob).getCanSpawnHere()) {
                            w.spawnEntityInWorld(mob);
                        }
                    } else {
                        w.spawnEntityInWorld(mob);
                    }
                }
            }
        }

        return flag;
    }

    protected double[] randomMobSpawnCoords(World world, IBeeGenome bee, IBeeHousing housing) {
        ChunkCoordinates coords = housing.getCoordinates();
        IBeeModifier beeModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);

        double pos[] = new double[3];
        pos[0] = coords.posX
                + world.rand.nextDouble() * (bee.getTerritory()[0] * beeModifier.getTerritoryModifier(bee, 1f))
                - bee.getTerritory()[0] / 2;
        pos[1] = coords.posY
                + world.rand.nextDouble() * (bee.getTerritory()[1] * beeModifier.getTerritoryModifier(bee, 1f))
                - bee.getTerritory()[1] / 2;
        pos[2] = coords.posZ
                + world.rand.nextDouble() * (bee.getTerritory()[2] * beeModifier.getTerritoryModifier(bee, 1f))
                - bee.getTerritory()[2] / 2;
        return pos;
    }
}
