package magicbees.tileentity;

import java.util.HashMap;
import java.util.Map;

import magicbees.api.bees.AuraChargeType;
import magicbees.api.bees.IMagicApiaryAuraProvider;
import magicbees.bees.AuraCharge;
import magicbees.main.CommonProxy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.visnet.VisNetHandler;

public class TileEntityVisAuraProvider extends TileEntity implements IMagicApiaryAuraProvider {

    public static final String tileEntityName = CommonProxy.DOMAIN + ".visAuraProvider";

    private static final int MAX_CHARGES = 10;
    private static final int VIS_PER_CHARGE = 8;
    private static Map<AuraChargeType, Aspect> aspectMap;

    static {
        aspectMap = new HashMap<AuraChargeType, Aspect>(AuraChargeType.values().length);
        aspectMap.put(AuraChargeType.MUTATION, Aspect.WATER);
        aspectMap.put(AuraChargeType.DEATH, Aspect.ENTROPY);
        aspectMap.put(AuraChargeType.PRODUCTION, Aspect.AIR);
    }

    private static class VisAuraCharge {

        int charges;
        int vis;

        public int[] toArray() {
            return new int[] { charges, vis };
        }

        public void fromArray(int[] array) {
            if (array.length == 2) {
                charges = array[0];
                vis = array[1];
            }
        }
    }

    private final Map<AuraCharge, VisAuraCharge> currentCharges;

    public TileEntityVisAuraProvider() {
        super();

        AuraCharge[] auraChargeTypes = AuraCharge.values();
        currentCharges = new HashMap<AuraCharge, VisAuraCharge>(auraChargeTypes.length);
        for (AuraCharge auraChargeType : auraChargeTypes) {
            currentCharges.put(auraChargeType, new VisAuraCharge());
        }
    }

    @Override
    public void updateEntity() {

        long tick = worldObj.getTotalWorldTime();

        for (Map.Entry<AuraCharge, VisAuraCharge> currentCharge : currentCharges.entrySet()) {
            AuraCharge type = currentCharge.getKey();
            VisAuraCharge auraCharge = currentCharge.getValue();

            if (auraCharge.charges < MAX_CHARGES && (tick % type.tickRate) == 0) {
                auraCharge.vis += getVisFromNet(aspectMap.get(type.type));
                if (auraCharge.vis >= VIS_PER_CHARGE) {
                    auraCharge.vis -= VIS_PER_CHARGE;
                    auraCharge.charges++;
                }
            }
        }
    }

    private int getVisFromNet(Aspect aspect) {
        return VisNetHandler.drainVis(worldObj, xCoord, yCoord, zCoord, aspect, 1);
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
        VisAuraCharge auraCharge = currentCharges.get(AuraCharge.fromChargeType(auraChargeType));
        if (auraCharge == null) {
            return false;
        }
        if (auraCharge.charges > 0) {
            auraCharge.charges--;
            return true;
        }

        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        for (Map.Entry<AuraCharge, VisAuraCharge> charge : currentCharges.entrySet()) {
            String auraName = charge.getKey().toString();
            int[] array = tag.getIntArray(auraName);
            VisAuraCharge auraCharge = charge.getValue();
            auraCharge.fromArray(array);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        for (Map.Entry<AuraCharge, VisAuraCharge> charge : currentCharges.entrySet()) {
            String auraName = charge.getKey().toString();
            VisAuraCharge auraCharge = charge.getValue();
            int[] array = auraCharge.toArray();
            tag.setIntArray(auraName, array);
        }
    }
}
