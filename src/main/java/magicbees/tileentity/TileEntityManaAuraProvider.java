package magicbees.tileentity;

import cpw.mods.fml.common.Optional;
import java.util.HashMap;
import java.util.Map;
import magicbees.api.bees.AuraChargeType;
import magicbees.api.bees.IMagicApiaryAuraProvider;
import magicbees.bees.AuraCharge;
import magicbees.main.CommonProxy;
import magicbees.main.utils.compat.BotaniaHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.api.mana.IManaReceiver;

@Optional.InterfaceList({
    @Optional.Interface(iface = "vazkii.botania.api.mana.IManaReceiver", modid = BotaniaHelper.Name)
})
public class TileEntityManaAuraProvider extends TileEntity implements IMagicApiaryAuraProvider, IManaReceiver {

    public static final String tileEntityName = CommonProxy.DOMAIN + ".manaAuraProvider";

    private static final int MAX_CHARGES = 4;
    private static final int MANA_PER_CHARGE = 4000;
    private static final int MAX_MANA = MANA_PER_CHARGE * 4;

    private static class ManaAuraCharge {
        int charges;
        int mana;

        public int[] toArray() {
            return new int[] {charges, mana};
        }

        public void fromArray(int[] array) {
            if (array.length == 2) {
                charges = array[0];
                mana = array[1];
            }
        }
    }

    private final Map<AuraCharge, ManaAuraCharge> currentCharges;
    private int currentMana;

    public TileEntityManaAuraProvider() {
        super();

        AuraCharge[] auraChargeTypes = AuraCharge.values();
        currentCharges = new HashMap<AuraCharge, ManaAuraCharge>(auraChargeTypes.length);
        for (AuraCharge auraChargeType : auraChargeTypes) {
            currentCharges.put(auraChargeType, new ManaAuraCharge());
        }
    }

    @Override
    public void updateEntity() {
        long tick = worldObj.getTotalWorldTime();
        int manaForCharge = currentMana / 5;

        if (manaForCharge > 0) {
            for (Map.Entry<AuraCharge, ManaAuraCharge> currentCharge : currentCharges.entrySet()) {
                AuraCharge type = currentCharge.getKey();
                ManaAuraCharge auraCharge = currentCharge.getValue();

                if (auraCharge.charges < MAX_CHARGES && (tick % type.tickRate) == 0 && currentMana > 0) {
                    auraCharge.mana += manaForCharge;
                    if (auraCharge.mana > MANA_PER_CHARGE) {
                        auraCharge.mana -= MANA_PER_CHARGE;
                        auraCharge.charges++;
                    }
                }
            }
        }
    }

    @Override
    public boolean getMutationCharge() {
        return getCharge(AuraChargeType.MUTATION);
    }

    @Override
    public boolean getDeathRateCharge() {
        return getCharge(AuraChargeType.DEATH);
    }

    @Override
    public boolean getProductionCharge() {
        return getCharge(AuraChargeType.PRODUCTION);
    }

    @Override
    public boolean getCharge(AuraChargeType auraChargeType) {
        ManaAuraCharge charge = currentCharges.get(AuraCharge.fromChargeType(auraChargeType));
        if (charge == null) {
            return false;
        }
        if (charge.charges > 0) {
            charge.charges--;
            return true;
        }
        return false;
    }

    @Override
    @Optional.Method(modid = BotaniaHelper.Name)
    public int getCurrentMana() {
        return currentMana;
    }

    @Override
    @Optional.Method(modid = BotaniaHelper.Name)
    public boolean canRecieveManaFromBursts() {
        return true;
    }

    @Override
    @Optional.Method(modid = BotaniaHelper.Name)
    public boolean isFull() {
        return currentMana == MAX_MANA;
    }

    @Override
    @Optional.Method(modid = BotaniaHelper.Name)
    public void recieveMana(int amount) {
        currentMana = (currentMana + amount > MAX_MANA) ? MAX_MANA : currentMana + amount;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        for (Map.Entry<AuraCharge, ManaAuraCharge> charge : currentCharges.entrySet()) {
            String auraName = charge.getKey().toString();
            int[] array = tag.getIntArray(auraName);
            ManaAuraCharge auraCharge = charge.getValue();
            auraCharge.fromArray(array);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        for (Map.Entry<AuraCharge, ManaAuraCharge> charge : currentCharges.entrySet()) {
            String auraName = charge.getKey().toString();
            ManaAuraCharge auraCharge = charge.getValue();
            int[] array = auraCharge.toArray();
            tag.setIntArray(auraName, array);
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, compound);
    }
}
