package magicbees.main.utils.compat;

import magicbees.item.types.DropType;
import magicbees.main.Config;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

public class ThermalModsHelper implements IModHelper {

    public enum MiscResource {
        EnderiumBlock,
        ElectrumBlock,
        BronzeBlock,
        FluidCoal,
        FluidRedstone,
        FluidGlowstone,
        FluidEnder,;
    }

    public enum NuggetType {
        ENDERIUM,
        ELECTRUM,
        BRONZE,
        INVAR,
        NICKEL,
        PLATINUM,;
    }

    public enum Entity {

        BLIZZ("Blizz", "ThermalFoundation:mobBlizzBreathe0"),
        BLITZ("Blitz", "ThermalFoundation:mobBlitzBreathe1"),
        BASALZ("Basalz", "ThermalFoundation:mobBasalzBreathe0"),;

        public final String entityID;
        public final String soundName;

        private Entity(String id, String sound) {
            this.entityID = id;
            soundName = sound;
        }
    }

    // ---ThermalExpansion Items ---
    public static ItemStack enderiumBlock;
    public static ItemStack electrumBlock;
    public static ItemStack invarBlock;
    public static ItemStack nickelBlock;
    public static ItemStack platinumBlock;
    public static ItemStack bronzeBlock;
    public static ItemStack signalumBlock;
    public static ItemStack lumiumBlock;

    public static ItemStack enderiumNugget;
    public static ItemStack invarNugget;
    public static ItemStack electrumNugget;
    public static ItemStack nickelNugget;
    public static ItemStack platinumNugget;
    public static ItemStack signalumNugget;
    public static ItemStack lumiumNugget;

    public static ItemStack dustCryotheum;
    public static ItemStack dustBlizz;
    public static ItemStack dustPyrotheum;
    public static ItemStack dustBlitz;
    public static ItemStack dustAerotheum;
    public static ItemStack dustBasalz;
    public static ItemStack dustPetrotheum;
    public static ItemStack dustSulfur;
    public static ItemStack dustPlatinum;

    public static FluidStack fluidGlowstone;
    public static FluidStack fluidCoal;
    public static FluidStack fluidRedstone;
    public static FluidStack fluidEnder;

    public static final String Name = "ThermalFoundation";
    private static boolean isThermalFoundationPresent = false;

    public static boolean isActive() {
        return isThermalFoundationPresent;
    }

    public void preInit() {
        if (Loader.isModLoaded(Name) && Config.thermalFoundationActive) {
            isThermalFoundationPresent = true;
        }
    }

    public void init() {
        if (isActive()) {
            getBlocks();
            getItems();
            getFluids();
            setupCrafting();
        }
    }

    public void postInit() {
        // if (isActive()) { }
    }

    private static void getFluids() {
        fluidGlowstone = FluidRegistry.getFluidStack("glowstone", 50);
        fluidCoal = FluidRegistry.getFluidStack("coal", 50);
        fluidRedstone = FluidRegistry.getFluidStack("redstone", 50);
        fluidEnder = FluidRegistry.getFluidStack("ender", 50);
    }

    private static void getBlocks() {
        enderiumBlock = GameRegistry.findItemStack(Name, "blockEnderium", 1);
        electrumBlock = GameRegistry.findItemStack(Name, "blockElectrum", 1);
        invarBlock = GameRegistry.findItemStack(Name, "blockInvar", 1);
        nickelBlock = GameRegistry.findItemStack(Name, "blockNickel", 1);
        platinumBlock = GameRegistry.findItemStack(Name, "blockPlatinum", 1);
        bronzeBlock = GameRegistry.findItemStack(Name, "blockBronze", 1);
        signalumBlock = GameRegistry.findItemStack(Name, "blockSignalum", 1);
        lumiumBlock = GameRegistry.findItemStack(Name, "blockLumium", 1);
    }

    private static void getItems() {
        enderiumNugget = GameRegistry.findItemStack(Name, "nuggetEnderium", 1);
        invarNugget = GameRegistry.findItemStack(Name, "nuggetInvar", 1);
        electrumNugget = GameRegistry.findItemStack(Name, "nuggetElectrum", 1);
        nickelNugget = GameRegistry.findItemStack(Name, "nuggetNickel", 1);
        platinumNugget = GameRegistry.findItemStack(Name, "nuggetPlatinum", 1);
        signalumNugget = GameRegistry.findItemStack(Name, "nuggetSignalum", 1);
        lumiumNugget = GameRegistry.findItemStack(Name, "nuggetLumium", 1);

        dustCryotheum = GameRegistry.findItemStack(Name, "dustCryotheum", 1);
        dustBlizz = GameRegistry.findItemStack(Name, "dustBlizz", 1);
        dustPyrotheum = GameRegistry.findItemStack(Name, "dustPyrotheum", 1);
        dustBlitz = GameRegistry.findItemStack(Name, "dustBlitz", 1);
        dustAerotheum = GameRegistry.findItemStack(Name, "dustAerotheum", 1);
        dustBasalz = GameRegistry.findItemStack(Name, "dustBasalz", 1);
        dustPetrotheum = GameRegistry.findItemStack(Name, "dustPetrotheum", 1);
        dustSulfur = GameRegistry.findItemStack(Name, "dustSulfur", 1);
        dustPlatinum = GameRegistry.findItemStack(Name, "dustPlatinum", 1);
    }

    private static void setupCrafting() {
        NBTTagCompound toSend = new NBTTagCompound();
        toSend.setInteger("energy", 4000);
        toSend.setTag("input", new NBTTagCompound());
        toSend.setTag("output", new NBTTagCompound());
        ItemStack carbonDrop = Config.drops.getStackForType(DropType.CARBON);
        carbonDrop.writeToNBT(toSend.getCompoundTag("input"));
        fluidCoal.writeToNBT(toSend.getCompoundTag("output"));
        FMLInterModComms.sendMessage("ThermalExpansion", "CrucibleRecipe", toSend);

        toSend = new NBTTagCompound();
        toSend.setInteger("energy", 4000);
        toSend.setTag("input", new NBTTagCompound());
        toSend.setTag("output", new NBTTagCompound());
        ItemStack destabilizedDrop = Config.drops.getStackForType(DropType.DESTABILIZED);
        destabilizedDrop.writeToNBT(toSend.getCompoundTag("input"));
        fluidRedstone.writeToNBT(toSend.getCompoundTag("output"));
        FMLInterModComms.sendMessage("ThermalExpansion", "CrucibleRecipe", toSend);

        toSend = new NBTTagCompound();
        toSend.setInteger("energy", 4000);
        toSend.setTag("input", new NBTTagCompound());
        toSend.setTag("output", new NBTTagCompound());
        ItemStack endearingDrop = Config.drops.getStackForType(DropType.ENDEARING);
        endearingDrop.writeToNBT(toSend.getCompoundTag("input"));
        fluidEnder.writeToNBT(toSend.getCompoundTag("output"));
        FMLInterModComms.sendMessage("ThermalExpansion", "CrucibleRecipe", toSend);

        toSend = new NBTTagCompound();
        toSend.setInteger("energy", 4000);
        toSend.setTag("input", new NBTTagCompound());
        toSend.setTag("output", new NBTTagCompound());
        ItemStack luxDrop = Config.drops.getStackForType(DropType.LUX);
        luxDrop.writeToNBT(toSend.getCompoundTag("input"));
        fluidGlowstone.writeToNBT(toSend.getCompoundTag("output"));
        FMLInterModComms.sendMessage("ThermalExpansion", "CrucibleRecipe", toSend);
    }
}
