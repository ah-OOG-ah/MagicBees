package magicbees.tileentity;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import forestry.api.apiculture.DefaultBeeListener;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeHousingInventory;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.core.IErrorLogic;
import forestry.api.core.IErrorState;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import magicbees.bees.BeeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

/**
 * Simulates enough of a bee house to fire the bee effects. =D
 *
 * @author MysteriousAges, mezz
 */
public class EffectJarHousing implements IBeeHousing {

    private static final IErrorLogic errorLogic = new JarErrorLogic();
    private static final Iterable<IBeeListener> beeListeners = ImmutableSet.<IBeeListener>of(new DefaultBeeListener());
    private static final Iterable<IBeeModifier> beeModifiers = ImmutableSet.<IBeeModifier>of(new JarHousingModifier());

    private final IBeekeepingLogic beekeepingLogic;
    private final TileEntityEffectJar jarEntity;
    private final BiomeGenBase biome;
    private final IBeeHousingInventory inventory;

    public EffectJarHousing(TileEntityEffectJar entity) {
        this.jarEntity = entity;
        this.biome = entity.getWorldObj().getBiomeGenForCoords(entity.xCoord, entity.zCoord);
        this.inventory = new JarBeeHousingInventory(entity);
        this.beekeepingLogic = BeeManager.beeRoot.createBeekeepingLogic(this);
    }

    @Override
    public ChunkCoordinates getCoordinates() {
        return new ChunkCoordinates(jarEntity.xCoord, jarEntity.yCoord, jarEntity.zCoord);
    }

    @Override
    public BiomeGenBase getBiome() {
        return biome;
    }

    @Override
    public EnumTemperature getTemperature() {
        return EnumTemperature.getFromBiome(biome, jarEntity.xCoord, jarEntity.yCoord, jarEntity.zCoord);
    }

    @Override
    public EnumHumidity getHumidity() {
        return EnumHumidity.getFromValue(biome.rainfall);
    }

    @Override
    public int getBlockLightValue() {
        return jarEntity.getWorldObj().getBlockLightValue(jarEntity.xCoord, jarEntity.yCoord + 1, jarEntity.zCoord);
    }

    @Override
    public boolean canBlockSeeTheSky() {
        return jarEntity.getWorldObj().canBlockSeeTheSky(jarEntity.xCoord, jarEntity.yCoord + 1, jarEntity.zCoord);
    }

    @Override
    public World getWorld() {
        return jarEntity.getWorldObj();
    }

    @Override
    public GameProfile getOwner() {
        return jarEntity.getOwner();
    }

    @Override
    public Vec3 getBeeFXCoordinates() {
        return Vec3.createVectorHelper(jarEntity.xCoord + 0.5f, jarEntity.yCoord + 0.5f, jarEntity.zCoord + 0.5f);
    }

    @Override
    public IErrorLogic getErrorLogic() {
        return errorLogic;
    }

    @Override
    public IBeeHousingInventory getBeeInventory() {
        return inventory;
    }

    @Override
    public IBeekeepingLogic getBeekeepingLogic() {
        return beekeepingLogic;
    }

    @Override
    public Iterable<IBeeModifier> getBeeModifiers() {
        return beeModifiers;
    }

    @Override
    public Iterable<IBeeListener> getBeeListeners() {
        return beeListeners;
    }

    private static class JarBeeHousingInventory implements IBeeHousingInventory {
        private final TileEntityEffectJar jarEntity;

        public JarBeeHousingInventory(TileEntityEffectJar jarEntity) {
            this.jarEntity = jarEntity;
        }

        @Override
        public ItemStack getQueen() {
            return jarEntity.getStackInSlot(TileEntityEffectJar.QUEEN_SLOT);
        }

        @Override
        public ItemStack getDrone() {
            return null;
        }

        @Override
        public void setQueen(ItemStack itemStack) {
            jarEntity.setInventorySlotContents(TileEntityEffectJar.QUEEN_SLOT, itemStack);
        }

        @Override
        public void setDrone(ItemStack itemstack) {}

        @Override
        public boolean addProduct(ItemStack product, boolean all) {
            return false;
        }
    }

    private static class JarErrorLogic implements IErrorLogic {

        @Override
        public boolean setCondition(boolean condition, IErrorState errorState) {
            return condition;
        }

        @Override
        public boolean contains(IErrorState state) {
            return false;
        }

        @Override
        public boolean hasErrors() {
            return false;
        }

        @Override
        public void clearErrors() {}

        @Override
        public void writeData(DataOutputStream data) throws IOException {}

        @Override
        public void readData(DataInputStream data) throws IOException {}

        @Override
        public ImmutableSet<IErrorState> getErrorStates() {
            return ImmutableSet.of();
        }
    }

    private static class JarHousingModifier implements IBeeModifier {
        @Override
        public float getTerritoryModifier(IBeeGenome genome, float currentModifier) {
            return 0.9f;
        }

        @Override
        public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) {
            return 0f;
        }

        @Override
        public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) {
            return 0f;
        }

        @Override
        public float getProductionModifier(IBeeGenome genome, float currentModifier) {
            return 0f;
        }

        @Override
        public float getFloweringModifier(IBeeGenome genome, float currentModifier) {
            return 0f;
        }

        @Override
        public float getGeneticDecay(IBeeGenome genome, float currentModifier) {
            return 0f;
        }

        @Override
        public boolean isSealed() {
            return true;
        }

        @Override
        public boolean isSelfLighted() {
            return true;
        }

        @Override
        public boolean isSunlightSimulated() {
            return true;
        }

        @Override
        public boolean isHellish() {
            return false;
        }
    }
}
