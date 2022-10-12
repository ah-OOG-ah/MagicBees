package magicbees.main.utils.compat;

import cpw.mods.fml.common.Loader;
import magicbees.main.Config;
import magicbees.main.utils.ItemInterface;
import net.minecraft.item.ItemStack;

public class RedstoneArsenalHelper implements IModHelper {
    public enum MiscResource {
        FLUXED_ELECTRUMBLOCK,
        ;
    }

    public enum NuggetType {
        ELECTRUMFLUX,
        ;
    }

    public static ItemStack fluxBlock;
    public static ItemStack fluxNugget;

    public static final String Name = "RedstoneArsenal";
    private static boolean isRedstoneArsenalPresent = false;

    public static boolean isActive() {
        return isRedstoneArsenalPresent;
    }

    public void preInit() {
        if (Loader.isModLoaded(Name) && Config.redstoneArsenalActive) {
            isRedstoneArsenalPresent = true;
        }
    }

    public void init() {
        if (isActive()) {
            getBlocks();
            getItems();
        }
    }

    public void postInit() {
        // if (isActive()) { }
    }

    private static void getBlocks() {
        fluxBlock = ItemInterface.getItemStack(Name, "blockElectrumFlux");
    }

    private static void getItems() {
        fluxNugget = ItemInterface.getItemStack(Name, "nuggetElectrumFlux");
    }
}
