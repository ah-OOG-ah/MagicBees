package magicbees.bees.allele.effect;

import java.util.List;

import magicbees.bees.AlleleEffect;
import magicbees.bees.BeeManager;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.genetics.IEffectData;

public class AlleleEffectSpawnMob extends AlleleEffect {

    protected boolean aggosOnPlayer;
    protected boolean spawnsWhilePlayerNear;
    protected String alternateMob;
    protected String mobName;
    protected String mobSound;
    protected int chanceToSpawn;
    protected int maxMobsInArea;

    public AlleleEffectSpawnMob(String id, boolean isDominant, String mobToSpawn) {
        this(id, isDominant, mobToSpawn, null);
    }

    public AlleleEffectSpawnMob(String id, boolean isDominant, String mobToSpawn, String mobSoundFx) {
        super(id, isDominant, 200);
        this.aggosOnPlayer = false;
        this.mobName = mobToSpawn;
        this.chanceToSpawn = 100;
        this.maxMobsInArea = 6;
        this.mobSound = mobSoundFx;
    }

    @Override
    public IEffectData validateStorage(IEffectData storedData) {
        if (storedData == null || !(storedData instanceof magicbees.bees.allele.effect.EffectData)) {
            // Throttle; Ready to spawn
            storedData = new EffectData(1, 1, 0);
        }
        return storedData;
    }

    @Override
    public IEffectData doEffectThrottled(IBeeGenome genome, IEffectData storedData, IBeeHousing housing) {
        // Check if we're ready to spawn the mob.
        boolean didSpawn = false;
        if (storedData.getBoolean(0)) {
            World w = housing.getWorld();

            int roll = w.rand.nextInt(100);

            if (roll <= chanceToSpawn) {
                if (spawnsWhilePlayerNear) {
                    EntityPlayer target = findPlayerTarget(genome, housing);

                    // Let's spawn a mob. =D
                    if (target != null) {
                        didSpawn = spawnMob(genome, target, w, housing, false);
                    } else if (alternateMob != null) {
                        didSpawn = spawnMob(genome, null, w, housing, true);
                    }
                } else {
                    storedData.setBoolean(0, !this.spawnMob(genome, null, w, housing, true));
                }
            } else {
                playMobLiveSound(genome, housing, w);
                didSpawn = true;
            }

            if (didSpawn) {
                storedData.setBoolean(0, !didSpawn);
                storedData.setInteger(0, storedData.getInteger(0) - throttle);
            }
        } else {
            storedData.setBoolean(0, true);
        }

        return storedData;
    }

    private void playMobLiveSound(IBeeGenome genome, IBeeHousing housing, World w) {
        if (mobSound != null && w.rand.nextInt(100) < 35) {
            ChunkCoordinates coords = housing.getCoordinates();

            int range = genome.getTerritory()[0];
            double x = coords.posX + w.rand.nextDouble() * (range * 2) - range;
            range = genome.getTerritory()[1];
            double y = coords.posY + w.rand.nextDouble() * (range * 2) - range;
            range = genome.getTerritory()[2];
            double z = coords.posZ + w.rand.nextDouble() * (range * 2) - range;
            w.playSoundEffect(x, y, z, this.mobSound, 0.5f, (w.rand.nextFloat() - w.rand.nextFloat()) * 0.2f + 1.0f);
        }
    }

    private EntityPlayer findPlayerTarget(IBeeGenome genome, IBeeHousing housing) {
        List<EntityPlayer> entities = getEntitiesWithinRange(genome, housing, EntityPlayer.class);
        for (EntityPlayer e : entities) {
            // Check for wearing armor & cancel
            // Full armor suit is treated as "invisible."
            if (forestry.api.apiculture.BeeManager.armorApiaristHelper.wearsItems((EntityLivingBase) e, getUID(), true)
                    < 4) {
                return e;
            }
        }
        return null;
    }

    protected boolean spawnMob(IBeeGenome bee, EntityPlayer player, World world, IBeeHousing housing,
            boolean spawnAlternate) {
        boolean spawnedFlag = false;

        EntityLiving mob;
        if (spawnAlternate && alternateMob != null) {
            mob = (EntityLiving) EntityList.createEntityByName(alternateMob, world);
        } else {
            mob = (EntityLiving) EntityList.createEntityByName(mobName, world);
        }

        if (mob != null) {
            double pos[] = this.randomMobSpawnCoords(world, bee, housing);

            int entitiesCount = world.getEntitiesWithinAABB(
                    mob.getClass(),
                    AxisAlignedBB.getBoundingBox(
                            (int) pos[0],
                            (int) pos[1],
                            (int) pos[2],
                            (int) pos[0] + 1,
                            (int) pos[1] + 1,
                            (int) pos[2] + 1).expand(8.0D, 4.0D, 8.0D))
                    .size();

            mob.setPositionAndRotation(pos[0], pos[1], pos[2], world.rand.nextFloat() * 360f, 0f);

            if (entitiesCount < maxMobsInArea && mob.getCanSpawnHere()) {
                spawnedFlag = world.spawnEntityInWorld(mob);
                if (aggosOnPlayer && player != null) {
                    if (forestry.api.apiculture.BeeManager.armorApiaristHelper
                            .wearsItems((EntityLivingBase) player, getUID(), true) < 4) {
                        // Protect fully suited player from initial murder intent.
                        mob.setAttackTarget(player);
                    }
                }
            }
        }

        return spawnedFlag;
    }

    public AlleleEffectSpawnMob setSpawnsOnPlayerNear(String alternateMobToSpawn) {
        spawnsWhilePlayerNear = true;
        alternateMob = alternateMobToSpawn;
        return this;
    }

    public AlleleEffectSpawnMob setAggrosPlayerOnSpawn() {
        aggosOnPlayer = true;
        return this;
    }

    public AlleleEffectSpawnMob setThrottle(int val) {
        throttle = val;
        return this;
    }

    public AlleleEffectSpawnMob setChanceToSpawn(int value) {
        chanceToSpawn = value;
        return this;
    }

    public AlleleEffectSpawnMob setMaxMobsInSpawnZone(int value) {
        maxMobsInArea = value;
        return this;
    }

    protected double[] randomMobSpawnCoords(World world, IBeeGenome bee, IBeeHousing housing) {
        ChunkCoordinates coords = housing.getCoordinates();
        IBeeModifier beeModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);

        double pos[] = new double[3];
        pos[0] = coords.posX
                + (world.rand.nextDouble() * (bee.getTerritory()[0] * beeModifier.getTerritoryModifier(bee, 1f))
                        - bee.getTerritory()[0] / 2);
        pos[1] = coords.posY + world.rand.nextInt(3) - 1;
        pos[2] = coords.posZ
                + (world.rand.nextDouble() * (bee.getTerritory()[2] * beeModifier.getTerritoryModifier(bee, 1f))
                        - bee.getTerritory()[2] / 2);
        return pos;
    }
}
