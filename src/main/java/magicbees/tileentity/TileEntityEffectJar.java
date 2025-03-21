package magicbees.tileentity;

import magicbees.main.CommonProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import com.mojang.authlib.GameProfile;

import forestry.api.apiculture.IBee;
import forestry.api.genetics.IEffectData;

public class TileEntityEffectJar extends TileEntity implements IInventory {

    public static final String tileEntityName = CommonProxy.DOMAIN + ".effectJar";
    private static final int SLOT_COUNT = 2;
    public static final int DRONE_SLOT = 0;
    public static final int QUEEN_SLOT = SLOT_COUNT - 1;

    private GameProfile ownerName;
    private ItemStack[] beeSlots;
    private IEffectData[] effectData = new IEffectData[2];
    private int throttle;
    public int currentBeeHealth;
    public int currentBeeColour;

    public TileEntityEffectJar() {
        super();
        this.beeSlots = new ItemStack[SLOT_COUNT];
    }

    public void setOwner(EntityPlayer player) {
        this.ownerName = player.getGameProfile();
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    public GameProfile getOwner() {
        return this.ownerName;
    }

    @Override
    public int getSizeInventory() {
        // Last slot reserved for "queen"
        return this.beeSlots.length - 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return beeSlots[slot];
    }

    @Override
    public void updateEntity() {
        if (hasQueen()) {
            tickQueen();
        } else if (hasDrone()) {
            createQueenFromDrone();
        }
    }

    private void createQueenFromDrone() {
        ItemStack droneStack = this.beeSlots[DRONE_SLOT];
        if (magicbees.bees.BeeManager.beeRoot.isDrone(droneStack)) {
            IBee bee = magicbees.bees.BeeManager.beeRoot.getMember(droneStack);
            if (droneStack.stackSize == 1) {
                this.beeSlots[DRONE_SLOT] = null;
            } else {
                droneStack.stackSize--;
            }

            this.beeSlots[QUEEN_SLOT] = droneStack.copy();
            this.beeSlots[QUEEN_SLOT].stackSize = 1;

            int current = bee.getHealth();
            int max = bee.getMaxHealth();
            currentBeeHealth = (current * 100) / max;
            currentBeeColour = bee.getGenome().getPrimary().getIconColour(0);
        }
        this.markDirty();
    }

    private boolean hasDrone() {
        return this.beeSlots[DRONE_SLOT] != null;
    }

    private void tickQueen() {
        IBee queen = magicbees.bees.BeeManager.beeRoot.getMember(this.beeSlots[QUEEN_SLOT]);

        currentBeeHealth = (queen.getHealth() * 100) / queen.getMaxHealth();
        currentBeeColour = queen.getGenome().getPrimary().getIconColour(0);

        EffectJarHousing housingLogic = new EffectJarHousing(this);
        this.effectData = queen.doEffect(this.effectData, housingLogic);
        if (this.worldObj.getWorldTime() % 5 == 0) {
            this.effectData = queen.doFX(this.effectData, housingLogic);
        }

        // run the queen
        if (throttle > 550) {
            throttle = 0;
            queen.age(this.worldObj, 0.26f);

            if (queen.getHealth() == 0) {
                this.beeSlots[QUEEN_SLOT] = null;
                currentBeeHealth = 0;
                currentBeeColour = 0x0ffffff;
            } else {
                queen.writeToNBT(this.beeSlots[QUEEN_SLOT].stackTagCompound);
            }
            this.markDirty();
        } else {
            throttle++;
        }
    }

    private boolean hasQueen() {
        return this.beeSlots[QUEEN_SLOT] != null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        ItemStack value = null;
        if (this.beeSlots[slot] != null) {
            value = this.beeSlots[slot].copy();
            value.stackSize = Math.min(count, this.beeSlots[slot].stackSize);
            this.beeSlots[slot].stackSize -= value.stackSize;
            if (this.beeSlots[slot].stackSize == 0) {
                this.beeSlots[slot] = null;
            }
        }
        markDirty();
        return value;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack value = null;
        if (this.beeSlots[slot] != null) {
            value = this.beeSlots[slot];
            this.beeSlots[slot] = null;
        }
        markDirty();
        return value;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagRoot) {
        super.readFromNBT(tagRoot);

        NBTTagList nbttaglist = tagRoot.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        this.beeSlots = new ItemStack[SLOT_COUNT];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.beeSlots.length) {
                this.beeSlots[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        this.currentBeeHealth = tagRoot.getInteger("currentBeeHealth");
        this.throttle = tagRoot.getInteger("throttle");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagRoot) {
        super.writeToNBT(tagRoot);

        NBTTagList inventory = new NBTTagList();
        for (int i = 0; i < this.beeSlots.length; ++i) {
            if (this.beeSlots[i] != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte) i);
                this.beeSlots[i].writeToNBT(itemTag);
                inventory.appendTag(itemTag);
            }
        }
        tagRoot.setTag("Items", inventory);
        tagRoot.setInteger("currentBeeHealth", this.currentBeeHealth);
        tagRoot.setInteger("throttle", this.throttle);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        this.beeSlots[slot] = itemStack;
        if (this.beeSlots[slot] != null) {
            this.beeSlots[slot].stackSize = Math.min(
                    this.beeSlots[slot].stackSize,
                    this.beeSlots[slot].getItem().getItemStackLimit(this.beeSlots[slot]));
        }
        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return "container.effectJar";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}
}
