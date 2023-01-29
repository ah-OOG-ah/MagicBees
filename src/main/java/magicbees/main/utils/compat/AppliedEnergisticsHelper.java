package magicbees.main.utils.compat;

import magicbees.main.Config;

import net.minecraft.block.Block;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class AppliedEnergisticsHelper implements IModHelper {

    public static Block skystone;

    public static final String Name = "appliedenergistics2";
    private static boolean isModActive = false;

    public static boolean isActive() {
        return isModActive;
    }

    public void preInit() {
        if (Loader.isModLoaded(Name) && Config.ae2Active) {
            isModActive = true;
        }
    }

    public void init() {
        if (isActive()) {
            skystone = GameRegistry.findBlock(Name, "tile.BlockSkyStone");
        }
    }

    public void postInit() {
        /*
         * if (isActive()) { }
         */
    }
}
