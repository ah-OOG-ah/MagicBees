package magicbees.main.utils.compat.botania;

import java.util.List;

import magicbees.bees.BeeManager;
import magicbees.main.utils.compat.BotaniaHelper;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileGenerating;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;

public class SubTileBeegonia extends SubTileGenerating {

    public static final String NAME = "beegonia";

    private static final int DRONE_BURN_TIME_BASE = 100;
    private static final int ITEM_STACK_AGE_THRESHOLD = 60;
    private static final int RANGE = 3;

    private int burnTimeLeft = 0;

    private void searchForDroneStack() {
        if (!supertile.getWorldObj().isRemote && mana < getMaxMana()) {
            boolean needsSync = false;

            @SuppressWarnings("unchecked")
            List<EntityItem> items = supertile.getWorldObj().getEntitiesWithinAABB(
                    EntityItem.class,
                    AxisAlignedBB.getBoundingBox(
                            supertile.xCoord - RANGE,
                            supertile.yCoord - RANGE,
                            supertile.zCoord - RANGE,
                            supertile.xCoord + RANGE + 1,
                            supertile.yCoord + RANGE + 1,
                            supertile.zCoord + RANGE + 1));
            for (EntityItem item : items) {
                if (item.age >= ITEM_STACK_AGE_THRESHOLD && !item.isDead) {
                    ItemStack stack = item.getEntityItem();
                    if (BeeManager.beeRoot.isDrone(stack)) {
                        IBee bee = BeeManager.beeRoot.getMember(stack);

                        if (stack.stackSize > 0) {
                            stack.stackSize--;
                            burnTimeLeft = (int) (DRONE_BURN_TIME_BASE
                                    * getSpeciesScaledComplexity(bee.getGenome().getPrimary())
                                    * BotaniaHelper.beegoniaManaMultiplier);
                            needsSync = true;

                            supertile.getWorldObj().playSoundEffect(
                                    supertile.xCoord,
                                    supertile.yCoord,
                                    supertile.zCoord,
                                    "botania:endoflame",
                                    0.2F,
                                    1F);
                        }

                        if (stack.stackSize <= 0) {
                            item.setDead();
                        }
                    }
                }
            }

            if (needsSync) {
                sync();
            }
        }
    }

    private int getSpeciesScaledComplexity(IAlleleBeeSpecies species) {
        if (species == null) {
            return 0;
        } else {
            try {
                int halfComplexity = species.getComplexity() / 2;
                return (halfComplexity >= 1) ? halfComplexity : 1;
            } catch (NoSuchMethodError error) {
                return 1;
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (linkedCollector != null) {
            if (burnTimeLeft > 0) {
                burnTimeLeft--;
            } else {
                searchForDroneStack();
            }
        }
    }

    @Override
    public void readFromPacketNBT(NBTTagCompound cmp) {
        super.readFromPacketNBT(cmp);

        burnTimeLeft = cmp.getInteger("burnTimeLeft");
    }

    @Override
    public void writeToPacketNBT(NBTTagCompound cmp) {
        super.writeToPacketNBT(cmp);

        cmp.setInteger("burnTimeLeft", burnTimeLeft);
    }

    @Override
    public int getMaxMana() {
        return 600;
    }

    @Override
    public int getValueForPassiveGeneration() {
        return 1;
    }

    @Override
    public boolean canGeneratePassively() {
        return burnTimeLeft > 0;
    }

    @Override
    public int getDelayBetweenPassiveGeneration() {
        return 1;
    }

    @Override
    public int getColor() {
        return 0xFFFF96;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon() {
        return BotaniaAPI.getSignatureForName(NAME).getIconForStack(null);
    }
}
