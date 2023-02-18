package magicbees.tileentity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import magicbees.api.bees.IMagicApiaryAuraProvider;
import magicbees.bees.AuraCharge;
import magicbees.bees.BeeManager;
import magicbees.main.CommonProxy;
import magicbees.main.utils.ChunkCoords;
import magicbees.main.utils.ItemStackUtils;
import magicbees.main.utils.net.EventAuraChargeUpdate;
import magicbees.main.utils.net.NetworkEventHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

import com.mojang.authlib.GameProfile;

import forestry.api.apiculture.DefaultBeeListener;
import forestry.api.apiculture.DefaultBeeModifier;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeHousingInventory;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.api.apiculture.IBeekeepingMode;
import forestry.api.apiculture.IHiveFrame;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.core.ForestryAPI;
import forestry.api.core.IErrorLogic;
// import magicbees.api.bees.BeeClassification;

public class TileEntityMagicApiary extends TileEntity
        implements ISidedInventory, IBeeHousing, ITileEntityAuraCharged, IAspectContainer, IEssentiaTransport {

    // Constants
    private static final int AURAPROVIDER_SEARCH_RADIUS = 6;
    public static final String tileEntityName = CommonProxy.DOMAIN + ".magicApiary";

    private GameProfile ownerProfile;
    private IMagicApiaryAuraProvider auraProvider;
    private ChunkCoords auraProviderPosition;
    private BiomeGenBase biome;
    private int breedingProgressPercent = 0;
    private final IBeekeepingLogic beeLogic;
    private final IErrorLogic errorLogic;
    private final IBeeListener beeListener;
    private final IBeeModifier beeModifier;
    private final MagicApiaryInventory inventory;
    private final AuraCharges auraCharges = new AuraCharges();
    public String name = "magicApiary";

    // added directly from the crucible of souls
    public AspectList myAspects = new AspectList();
    public Aspect aspect;
    public int amount = 0;
    public int maxAmount = 64;
    public int incre = 0;

    /*
     * public static final Aspect AIR = new Aspect("aer", 16777086, "e", 1); public static final Aspect EARTH = new
     * Aspect("terra", 5685248, "2", 1); public static final Aspect FIRE = new Aspect("ignis", 16734721, "c", 1); public
     * static final Aspect WATER = new Aspect("aqua", 3986684, "3", 1); public static final Aspect ORDER = new
     * Aspect("ordo", 14013676, "7", 1); public static final Aspect ENTROPY = new Aspect("perditio", 4210752, "8", 771);
     */

    // the commented lines above show how the essentias are labeled, you use the actual name of the essentia to grab it.
    public Aspect AIR = aspect.getAspect("aer");

    // added directly from the crucible of souls
    @Override
    public void setAspects(AspectList aspects) {
        this.myAspects = aspects;
    }

    // added directly from the crucible of souls
    @Override
    public AspectList getAspects() {
        // TODO Auto-generated method stub
        return this.myAspects;
    }

    // added directly from the crucible of souls
    @Override
    public boolean doesContainerAccept(Aspect tag) {
        return false;
    }

    // added directly from the crucible of souls
    @Override
    public int addToContainer(Aspect tag, int am) {
        if (am == 0) {
            return am;
        } else {
            if (this.amount < this.maxAmount && tag == this.aspect || this.amount == 0) {
                this.aspect = tag;
                int added = Math.min(am, this.maxAmount - this.amount);
                this.amount += added;
                am -= added;
            }
            return am;
        }
    }

    // added directly from the crucible of souls
    @Override
    public boolean takeFromContainer(Aspect tag, int amount) {
        if (!this.worldObj.isRemote) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
        if (this.myAspects.getAmount(tag) >= amount) {
            this.myAspects.reduce(tag, amount);
            return true;
        } else {
            return false;
        }
    }

    // added directly from the crucible of souls
    @Override
    public boolean takeFromContainer(AspectList ot) {
        // TODO Auto-generated method stub
        boolean hasIt = true;
        if (!this.worldObj.isRemote) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
        ot.aspects.keySet().iterator();
        Iterator iterator = ot.aspects.keySet().iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (this.myAspects.getAmount((Aspect) next) < ot.getAmount((Aspect) next)) hasIt = false;
        }
        if (hasIt) {
            iterator = ot.aspects.keySet().iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                myAspects.reduce((Aspect) next, ot.getAmount((Aspect) next));
            }
            return true;
        } else {
            return false;
        }
    }

    // added directly from the crucible of souls
    @Override
    public boolean doesContainerContainAmount(Aspect tag, int amount) {
        // TODO Auto-generated method stub
        return (this.myAspects.getAmount(tag) > amount);
    }

    // added directly from the crucible of souls
    @Override
    public boolean doesContainerContain(AspectList ot) {
        boolean hasIt = true;
        ot.aspects.keySet().iterator();
        Iterator iterator = ot.aspects.keySet().iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (this.myAspects.getAmount((Aspect) next) < ot.getAmount((Aspect) next)) hasIt = false;
        }
        return hasIt;
    }

    // added directly from the crucible of souls
    @Override
    public int containerContains(Aspect tag) {
        return this.myAspects.getAmount(tag);
    }

    // added directly from the crucible of souls
    @Override
    public boolean isConnectable(ForgeDirection face) {
        return (face != ForgeDirection.UP);
    }

    // added directly from the crucible of souls
    @Override
    public boolean canInputFrom(ForgeDirection face) {
        return false;
    }

    // added directly from the crucible of souls
    @Override
    public boolean canOutputTo(ForgeDirection face) {
        return (face != ForgeDirection.UP);
    }

    // added directly from the crucible of souls
    @Override
    public void setSuction(Aspect aspect, int amount) {
        // TODO Auto-generated method stub

    }

    // added directly from the crucible of souls
    @Override
    public int takeEssentia(Aspect aspect, int amount, ForgeDirection arg2) {
        if (arg2 != ForgeDirection.UP) {
            if (!this.worldObj.isRemote) {
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
            if (amount > this.myAspects.getAmount(aspect)) {
                int total = this.myAspects.getAmount(aspect);
                this.myAspects.reduce(aspect, total);
                return total;
            } else {
                this.myAspects.reduce(aspect, amount);
                return amount;
            }

        } else {
            return 0;
        }

    }

    // added directly from the crucible of souls
    @Override
    public int getMinimumSuction() {
        // TODO Auto-generated method stub
        return 0;
    }

    // added directly from the crucible of souls
    @Override
    public boolean renderExtendedTube() {
        // TODO Auto-generated method stub
        return false;
        // NEW AFTER THIS LINE
    }

    // added directly from the crucible of souls
    @Override
    public Aspect getSuctionType(ForgeDirection face) {
        return null;
    }

    // added directly from the crucible of souls
    @Override
    public int getSuctionAmount(ForgeDirection face) {
        return 0;
    }

    // added directly from the crucible of souls
    @Override
    public Aspect getEssentiaType(ForgeDirection face) {
        return this.myAspects.size() > 0
                ? this.myAspects.getAspects()[this.worldObj.rand.nextInt(this.myAspects.getAspects().length)]
                : null;
    }

    // added directly from the crucible of souls
    @Override
    public int getEssentiaAmount(ForgeDirection face) {
        return this.myAspects.visSize();
    }

    // added directly from the crucible of souls
    @Override
    public int addEssentia(Aspect arg0, int arg1, ForgeDirection arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    // added directly from the crucible of souls
    private float tagAmount() {
        int amount = 0;
        Iterator iterator = this.myAspects.aspects.keySet().iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            amount += this.myAspects.getAmount((Aspect) next);
        }
        return amount;
    }

    public TileEntityMagicApiary() {
        beeLogic = BeeManager.beeRoot.createBeekeepingLogic(this);
        beeModifier = new MagicApiaryBeeModifier(this);
        beeListener = new MagicApiaryBeeListener(this);
        inventory = new MagicApiaryInventory(this);
        errorLogic = ForestryAPI.errorStateRegistry.createErrorLogic();
    }

    @Override
    public Iterable<IBeeModifier> getBeeModifiers() {
        List<IBeeModifier> beeModifiers = new ArrayList<IBeeModifier>();

        beeModifiers.add(beeModifier);

        for (IHiveFrame frame : inventory.getFrames()) {
            beeModifiers.add(frame.getBeeModifier());
        }

        return beeModifiers;
    }

    @Override
    public Iterable<IBeeListener> getBeeListeners() {
        return Collections.singleton(beeListener);
    }

    @Override
    public IBeeHousingInventory getBeeInventory() {
        return inventory;
    }

    @Override
    public IBeekeepingLogic getBeekeepingLogic() {
        return beeLogic;
    }

    public void setOwner(EntityPlayer player) {
        this.ownerProfile = player.getGameProfile();
    }

    @Override
    public IErrorLogic getErrorLogic() {
        return errorLogic;
    }

    @Override
    public GameProfile getOwner() {
        return this.ownerProfile;
    }

    @Override
    public World getWorld() {
        return worldObj;
    }

    @Override
    public ChunkCoordinates getCoordinates() {
        return new ChunkCoordinates(xCoord, yCoord, zCoord);
    }

    @Override
    public Vec3 getBeeFXCoordinates() {
        return Vec3.createVectorHelper(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
    }

    @Override
    public BiomeGenBase getBiome() {
        if (biome == null) {
            biome = worldObj.getBiomeGenForCoordsBody(xCoord, zCoord);
        }
        return biome;
    }

    @Override
    public EnumTemperature getTemperature() {
        return EnumTemperature.getFromBiome(getBiome(), xCoord, yCoord, zCoord);
    }

    @Override
    public EnumHumidity getHumidity() {
        return EnumHumidity.getFromValue(getExactHumidity());
    }

    @Override
    public int getBlockLightValue() {
        return worldObj.getBlockLightValue(xCoord, yCoord + 1, zCoord);
    }

    @Override
    public boolean canBlockSeeTheSky() {
        return worldObj.canBlockSeeTheSky(xCoord, yCoord + 1, zCoord);
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return inventory.getStackInSlot(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        ItemStack itemStack = getStackInSlot(i);

        if (itemStack != null) {
            if (itemStack.stackSize <= j) {
                setInventorySlotContents(i, null);
            } else {
                itemStack = itemStack.splitStack(j);
                markDirty();
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        ItemStack item = getStackInSlot(i);
        setInventorySlotContents(i, null);
        return item;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        inventory.setInventorySlotContents(i, itemStack);
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return tileEntityName;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return entityPlayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return true;
    }

    public int getHealthScaled(int i) {
        return (breedingProgressPercent * i) / 100;
    }

    public int getTemperatureScaled(int i) {
        return Math.round(i * (getExactTemperature() / 2));
    }

    public int getHumidityScaled(int i) {
        return Math.round(i * getExactHumidity());
    }

    /* Saving and loading */
    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        inventory.writeToNBT(compound);
        beeLogic.writeToNBT(compound);
        ChunkCoords.writeToNBT(auraProviderPosition, compound);
        auraCharges.writeToNBT(compound);

        // soul crucible code below
        NBTTagCompound aspects = new NBTTagCompound();
        Iterator iterator = Aspect.aspects.keySet().iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("Amount", this.myAspects.getAmount(Aspect.getAspect((String) next)));
            aspects.setTag((String) next, tag);
        }
        compound.setTag("Aspects", aspects);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        inventory.readFromNBT(compound);
        beeLogic.readFromNBT(compound);
        auraProviderPosition = ChunkCoords.readFromNBT(compound);
        auraCharges.readFromNBT(compound);

        // soul crucible code below
        AspectList readAspects = new AspectList();
        NBTTagCompound aspects = compound.getCompoundTag("Aspects");
        Iterator iterator = Aspect.aspects.keySet().iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            NBTTagCompound aspect = aspects.getCompoundTag((String) next);
            int amount = aspect.getInteger("Amount");
            if (amount > 0) {
                readAspects.add(Aspect.getAspect((String) next), amount);
            }
        }
        this.myAspects = readAspects;
    }

    @Override
    public Packet getDescriptionPacket() {
        beeLogic.syncToClient();
        EventAuraChargeUpdate event = new EventAuraChargeUpdate(new ChunkCoords(this), auraCharges);

        // soul crucible code below

        NBTTagCompound access = new NBTTagCompound();
        NBTTagCompound aspects = new NBTTagCompound();

        Iterator iterator = Aspect.aspects.keySet().iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("Amount", this.myAspects.getAmount(Aspect.getAspect((String) next)));
            aspects.setTag((String) next, tag);
        }
        access.setTag("Aspects", aspects);

        // this code right here could potentially be an issue though

        return event.getPacket();
    }

    public float getExactTemperature() {
        return getBiome().getFloatTemperature(xCoord, yCoord, zCoord);
    }

    public float getExactHumidity() {
        return getBiome().rainfall;
    }

    @Override
    public void updateEntity() {
        // this allows it to add essentia
        if (incre >= 20) {
            ItemStack bee = this.getBeeInventory().getQueen();
            if (bee != null) {
                String beeName = bee.getDisplayName();
                if ((beeName != null && !beeName.isEmpty())) {
                    String[] parts = beeName.split(" ");
                    String beeType = parts[1];

                    if (beeType.equals("defaultium essentia apis")) {
                        // get fucked
                    }
                }
            }
        }
        incre++;

        if (worldObj.isRemote) {
            updateClientSide();
        } else {
            updateServerSide();
        }
    }

    public void updateClientSide() {
        if (beeLogic.canDoBeeFX() && worldObj.getTotalWorldTime() % 10 == 0) {
            beeLogic.doBeeFX();
        }
    }

    public void updateServerSide() {
        if (this.auraProvider == null) {
            findAuraProvider();
        } else {
            updateAuraProvider();
        }
        tickCharges();

        if (beeLogic.canWork()) {
            beeLogic.doWork();
        }
    }

    public void getGUINetworkData(int i, int j) {
        switch (i) {
            case 0:
                breedingProgressPercent = j;
                break;
        }
    }

    public void sendGUINetworkData(Container container, ICrafting iCrafting) {
        iCrafting.sendProgressBarUpdate(container, 0, beeLogic.getBeeProgressPercent());
    }

    public boolean isProductionBoosted() {
        return auraCharges.isActive(AuraCharge.PRODUCTION);
    }

    public boolean isDeathRateBoosted() {
        return auraCharges.isActive(AuraCharge.DEATH);
    }

    public boolean isMutationBoosted() {
        return auraCharges.isActive(AuraCharge.MUTATION);
    }

    private void updateAuraProvider() {
        if (worldObj.getTotalWorldTime() % 10 != 0) {
            return;
        }
        if (getAuraProvider(auraProviderPosition) == null) {
            this.auraProvider = null;
            this.auraProviderPosition = null;
            return;
        }

        boolean auraChargesChanged = false;
        for (AuraCharge charge : AuraCharge.values()) {
            if (!auraCharges.isActive(charge) && auraProvider.getCharge(charge.type)) {
                auraCharges.start(charge, worldObj);
                auraChargesChanged = true;
            }
        }

        if (auraChargesChanged) {
            NetworkEventHandler.getInstance().sendAuraChargeUpdate(this, auraCharges);
        }
    }

    private void tickCharges() {
        boolean auraChargesChanged = false;

        for (AuraCharge charge : AuraCharge.values()) {
            if (auraCharges.isActive(charge) && auraCharges.isExpired(charge, worldObj)
                    && (auraProvider == null || !auraProvider.getCharge(charge.type))) {
                auraCharges.stop(charge);
                auraChargesChanged = true;
            }
        }

        if (auraChargesChanged) {
            NetworkEventHandler.getInstance().sendAuraChargeUpdate(this, auraCharges);
        }
    }

    private void findAuraProvider() {
        if (worldObj.getTotalWorldTime() % 5 != 0) {
            return;
        }

        if (this.auraProviderPosition == null) {
            List<Chunk> chunks = getChunksInSearchRange();
            for (Chunk chunk : chunks) {
                if (searchChunkForBooster(chunk)) {
                    break;
                }
            }
        } else {
            this.auraProvider = getAuraProvider(auraProviderPosition);
            if (auraProvider == null) {
                this.auraProviderPosition = null;
            }
        }
    }

    private List<Chunk> getChunksInSearchRange() {
        List<Chunk> chunks = new ArrayList<Chunk>(4);
        chunks.add(
                worldObj.getChunkFromBlockCoords(
                        xCoord - AURAPROVIDER_SEARCH_RADIUS,
                        zCoord - AURAPROVIDER_SEARCH_RADIUS));
        Chunk chunk = worldObj
                .getChunkFromBlockCoords(xCoord + AURAPROVIDER_SEARCH_RADIUS, zCoord - AURAPROVIDER_SEARCH_RADIUS);
        if (!chunks.contains(chunk)) {
            chunks.add(chunk);
        }
        chunk = worldObj
                .getChunkFromBlockCoords(xCoord - AURAPROVIDER_SEARCH_RADIUS, zCoord + AURAPROVIDER_SEARCH_RADIUS);
        if (!chunks.contains(chunk)) {
            chunks.add(chunk);
        }
        chunk = worldObj
                .getChunkFromBlockCoords(xCoord + AURAPROVIDER_SEARCH_RADIUS, zCoord + AURAPROVIDER_SEARCH_RADIUS);
        if (!chunks.contains(chunk)) {
            chunks.add(chunk);
        }
        return chunks;
    }

    @SuppressWarnings("unchecked")
    private boolean searchChunkForBooster(Chunk chunk) {
        Vec3 apiaryPos = Vec3.createVectorHelper(xCoord, yCoord, zCoord);
        for (Map.Entry<ChunkPosition, TileEntity> entry : ((Map<ChunkPosition, TileEntity>) chunk.chunkTileEntityMap)
                .entrySet()) {
            TileEntity entity = entry.getValue();
            if (entity instanceof IMagicApiaryAuraProvider) {
                Vec3 tePos = Vec3.createVectorHelper(entity.xCoord, entity.yCoord, entity.zCoord);
                Vec3 result = apiaryPos.subtract(tePos);
                if (result.lengthVector() <= AURAPROVIDER_SEARCH_RADIUS) {
                    saveAuraProviderPosition(entity.xCoord, entity.yCoord, entity.zCoord);
                    this.auraProvider = (IMagicApiaryAuraProvider) entity;
                    return true;
                }
            }
        }
        return false;
    }

    private void saveAuraProviderPosition(int x, int y, int z) {
        auraProviderPosition = new ChunkCoords(worldObj.provider.dimensionId, x, y, z);
    }

    private IMagicApiaryAuraProvider getAuraProvider(ChunkCoords coords) {
        return getAuraProvider(coords.x, coords.y, coords.z);
    }

    private IMagicApiaryAuraProvider getAuraProvider(int x, int y, int z) {
        Chunk chunk = worldObj.getChunkFromBlockCoords(x, z);
        x %= 16;
        z %= 16;
        if (x < 0) {
            x += 16;
        }
        if (z < 0) {
            z += 16;
        }
        ChunkPosition cPos = new ChunkPosition(x, y, z);
        TileEntity entity = (TileEntity) chunk.chunkTileEntityMap.get(cPos);
        if (!(entity instanceof IMagicApiaryAuraProvider)) {
            return null;
        }
        return (IMagicApiaryAuraProvider) entity;
    }

    @Override
    public AuraCharges getAuraCharges() {
        return auraCharges;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int i) {
        return inventory.getAccessibleSlotsFromSide(i);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i1) {
        return inventory.canInsertItem(i, itemStack, i1);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, int i1) {
        return inventory.canExtractItem(i, itemStack, i1);
    }

    private static class MagicApiaryInventory implements IBeeHousingInventory {

        public static final int SLOT_QUEEN = 0;
        public static final int SLOT_DRONE = 1;
        public static final int SLOT_FRAME_START = 2;
        public static final int SLOT_FRAME_COUNT = 3;
        public static final int SLOT_PRODUCTS_START = 5;
        public static final int SLOT_PRODUCTS_COUNT = 7;

        private final TileEntityMagicApiary magicApiary;
        private ItemStack[] items;

        public MagicApiaryInventory(TileEntityMagicApiary magicApiary) {
            this.magicApiary = magicApiary;
            this.items = new ItemStack[12];
        }

        @Override
        public ItemStack getQueen() {
            return magicApiary.getStackInSlot(SLOT_QUEEN);
        }

        @Override
        public ItemStack getDrone() {
            return magicApiary.getStackInSlot(SLOT_DRONE);
        }

        @Override
        public void setQueen(ItemStack itemstack) {
            magicApiary.setInventorySlotContents(SLOT_QUEEN, itemstack);
        }

        @Override
        public void setDrone(ItemStack itemstack) {
            magicApiary.setInventorySlotContents(SLOT_DRONE, itemstack);
        }

        @Override
        public boolean addProduct(ItemStack product, boolean all) {
            int countAdded = ItemStackUtils
                    .addItemToInventory(magicApiary, product, SLOT_PRODUCTS_START, SLOT_PRODUCTS_COUNT);

            if (all) {
                return countAdded == product.stackSize;
            } else {
                return countAdded > 0;
            }
        }

        public int getSizeInventory() {
            return items.length;
        }

        public ItemStack getStackInSlot(int i) {
            return items[i];
        }

        public void setInventorySlotContents(int i, ItemStack itemStack) {
            items[i] = itemStack;

            if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
                itemStack.stackSize = getInventoryStackLimit();
            }
        }

        public int[] getAccessibleSlotsFromSide(int side) {
            if (side == 0 || side == 1) {
                return new int[] { SLOT_QUEEN, SLOT_DRONE };
            } else {
                int[] slots = new int[SLOT_PRODUCTS_COUNT];
                for (int i = 0, slot = SLOT_PRODUCTS_START; i < SLOT_PRODUCTS_COUNT; ++i, ++slot) {
                    slots[i] = slot;
                }
                return slots;
            }
        }

        public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
            if (slot == SLOT_QUEEN && BeeManager.beeRoot.isMember(itemStack)
                    && !BeeManager.beeRoot.isDrone(itemStack)) {
                return true;
            } else if (slot == SLOT_DRONE && BeeManager.beeRoot.isDrone(itemStack)) {
                return true;
            }
            return slot == SLOT_DRONE && BeeManager.beeRoot.isDrone(itemStack);
        }

        public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
            switch (slot) {
                case SLOT_FRAME_START:
                case SLOT_FRAME_START + 1:
                case SLOT_FRAME_START + 2:
                    return false;
                default:
                    return true;
            }
        }

        public int getInventoryStackLimit() {
            return 64;
        }

        public Collection<IHiveFrame> getFrames() {
            Collection<IHiveFrame> hiveFrames = new ArrayList<IHiveFrame>(SLOT_FRAME_COUNT);

            for (int i = SLOT_FRAME_START; i < SLOT_FRAME_START + SLOT_FRAME_COUNT; i++) {
                ItemStack stackInSlot = magicApiary.getStackInSlot(i);
                if (stackInSlot == null) {
                    continue;
                }

                Item itemInSlot = stackInSlot.getItem();
                if (itemInSlot instanceof IHiveFrame) {
                    hiveFrames.add((IHiveFrame) itemInSlot);
                }
            }

            return hiveFrames;
        }

        public void writeToNBT(NBTTagCompound compound) {
            NBTTagList itemsNBT = new NBTTagList();

            for (int i = 0; i < items.length; i++) {
                ItemStack itemStack = items[i];

                if (itemStack != null) {
                    NBTTagCompound item = new NBTTagCompound();
                    item.setByte("Slot", (byte) i);
                    itemStack.writeToNBT(item);
                    itemsNBT.appendTag(item);
                }
            }
            compound.setTag("Items", itemsNBT);
        }

        public void readFromNBT(NBTTagCompound compound) {
            NBTTagList items = compound.getTagList("Items", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < items.tagCount(); i++) {
                NBTTagCompound item = items.getCompoundTagAt(i);
                int slot = item.getByte("Slot");

                if (slot >= 0 && slot < getSizeInventory()) {
                    setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
                }
            }
        }
    }

    private static class MagicApiaryBeeModifier extends DefaultBeeModifier {

        private final TileEntityMagicApiary magicApiary;

        public MagicApiaryBeeModifier(TileEntityMagicApiary magicApiary) {
            this.magicApiary = magicApiary;
        }

        @Override
        public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) {
            float mod = 1.0f;
            if (magicApiary.isMutationBoosted()) {
                mod = mod * 2f;
            }
            return mod;
        }

        @Override
        public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) {
            float mod = 1.0f;
            if (magicApiary.isDeathRateBoosted()) {
                mod = mod / 2f;
            }
            return mod;
        }

        @Override
        public float getProductionModifier(IBeeGenome genome, float currentModifier) {
            float mod = 0.9f;
            if (magicApiary.isProductionBoosted()) {
                mod = mod * 2f;
            }
            return mod;
        }

        @Override
        public float getGeneticDecay(IBeeGenome genome, float currentModifier) {
            return 0.8f;
        }
    }

    // I think this is the updater
    private static class MagicApiaryBeeListener extends DefaultBeeListener {

        private final TileEntityMagicApiary magicApiary;

        public MagicApiaryBeeListener(TileEntityMagicApiary magicApiary) {
            this.magicApiary = magicApiary;
        }

        @Override
        public void wearOutEquipment(int amount) {
            IBeekeepingMode beekeepingMode = BeeManager.beeRoot.getBeekeepingMode(magicApiary.getWorldObj());
            int wear = Math.round(amount * beekeepingMode.getWearModifier());

            for (int i = MagicApiaryInventory.SLOT_FRAME_START; i
                    < MagicApiaryInventory.SLOT_FRAME_START + MagicApiaryInventory.SLOT_FRAME_COUNT; i++) {
                ItemStack hiveFrameStack = magicApiary.getStackInSlot(i);
                if (hiveFrameStack == null) {
                    continue;
                }

                Item hiveFrameItem = hiveFrameStack.getItem();
                if (!(hiveFrameItem instanceof IHiveFrame)) {
                    continue;
                }

                IHiveFrame hiveFrame = (IHiveFrame) hiveFrameItem;

                ItemStack queenStack = magicApiary.getBeeInventory().getQueen();
                IBee queen = BeeManager.beeRoot.getMember(queenStack);
                ItemStack usedFrame = hiveFrame.frameUsed(magicApiary, hiveFrameStack, queen, wear);

                magicApiary.setInventorySlotContents(i, usedFrame);
            }
        }
    }
}
