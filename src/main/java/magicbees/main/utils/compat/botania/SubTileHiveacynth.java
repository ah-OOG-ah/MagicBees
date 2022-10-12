package magicbees.main.utils.compat.botania;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IBee;
import java.util.Random;
import magicbees.bees.BeeManager;
import magicbees.main.utils.compat.BotaniaHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileFunctional;

public class SubTileHiveacynth extends SubTileFunctional {

    public static final String NAME = "hiveacynth";
    private static final int RANGE = 3;
    private static final int COST = 15000;

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (supertile.getWorldObj().isRemote) {
            return;
        }

        if (readyToProduceProduct()) {
            mana -= getFinalCost();
            Random r = supertile.getWorldObj().rand;
            EnumBeeType beeType = EnumBeeType.DRONE;

            IBee bee = BeeManager.getBeeFromSpecies(
                    BeeManager.getRandomWorldgenSpecies(r), r.nextDouble() < BotaniaHelper.hiveacynthRainResistRate);

            if (r.nextDouble() < BotaniaHelper.hiveacynthPrincessSpawnRate) {
                beeType = EnumBeeType.PRINCESS;

                if (BotaniaHelper.hiveacynthPristineRate < r.nextDouble()) {
                    bee.setIsNatural(false);
                }
            }

            ItemStack stack = BeeManager.beeRoot.getMemberStack(bee, beeType.ordinal());

            EntityItem entity = new EntityItem(
                    supertile.getWorldObj(),
                    supertile.xCoord - RANGE + r.nextInt(RANGE * 2 + 1),
                    supertile.yCoord + 1,
                    supertile.zCoord - RANGE + r.nextInt(RANGE * 2 + 1),
                    stack);
            entity.motionX = 0;
            entity.motionY = 0;
            entity.motionZ = 0;
            supertile.getWorldObj().spawnEntityInWorld(entity);
        }
    }

    private boolean readyToProduceProduct() {
        return redstoneSignal == 0 && ticksExisted % 200 == 0 && mana >= getFinalCost();
    }

    private int getFinalCost() {
        return (int) (COST * BotaniaHelper.hiveacynthManaMultiplier);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon() {
        return BotaniaAPI.getSignatureForName(NAME).getIconForStack(null);
    }

    @Override
    public boolean acceptsRedstone() {
        return true;
    }

    @Override
    public int getColor() {
        return 0x0071C6;
    }

    @Override
    public int getMaxMana() {
        return getFinalCost();
    }
}
