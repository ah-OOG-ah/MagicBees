package magicbees.main.utils.compat;

import magicbees.bees.BeeManager;
import magicbees.item.types.PollenType;
import magicbees.main.Config;
import magicbees.main.utils.BlockInterface;
import magicbees.main.utils.ItemInterface;
import magicbees.main.utils.compat.botania.BotaniaAPIDistanceHelper;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import vazkii.botania.api.mana.ManaItemHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BotaniaHelper implements IModHelper {

    @SideOnly(Side.CLIENT)
    public static IIcon subtileIcons[];

    public static final String LEXICON_ENTRY_MANA_GEAR = "manaGear";
    public static final String LEXICON_ENTRY_ELF_RESOURCES = "elfResources";

    public enum ManaResource {
        MANASTEEL,
        MANAPEARL,
        MANADIAMOND,
        LIVINGWOOD_TWIG,
        TERRASTEEL,
        LIFE_ESSENCE,
        REDSTONE_ROOT,
        ELEMENTIUM,
        PIXIE_DUST,
        DRAGONSTONE,
        PRISMARINE_SHARD,
        PLACEHOLDER,
        RED_STRING,
        DREAMWOOD_TWIG,
        GAIA_INGOT,
        ENDER_AIR_BOTTLE,
        MANA_STRING,
        MANASTEEL_NUGGET,
        TERRASTEEL_NUGGET,
        ELEMENTIUM_NUGGET,
        ROOT,
        PEBBLE,
        MANAWEAVE_CLOTH,
        MANA_POWDER;
    }

    public enum PastureSeed {
        GRASS,
        PODZOL,
        MYCELIUM,;
    }

    public enum Rune {
        WATER,
        FIRE,
        EARTH,
        AIR,
        SPRING,
        SUMMER,
        AUTUMN,
        WINTER,
        MANA,
        LUST,
        GLUTTONY,
        GREED,
        SLOTH,
        WRATH,
        ENVY,
        PRIDE,;
    }

    public static Block blockMysticalFlower;
    public static Block blockLivingRock;
    public static Block blockLivingWood;
    public static Block blockDreamWood;

    public static Item itemPetal;
    public static Item itemManaResource;
    public static Item itemPastureSeed;
    public static Item itemRune;

    public static ItemStack[] mysticalFlowerVariants;

    public static IRecipe manasteelGrafterRecipe;
    public static IRecipe manasteelScoopRecipe;
    public static IRecipe manaBoosterRecipe;

    private static boolean isBotaniaActive = false;
    public static final String Name = "Botania";

    public static double beegoniaManaMultiplier;
    public static double hiveacynthManaMultiplier;
    public static double hiveacynthRainResistRate;
    public static double hiveacynthPrincessSpawnRate;
    public static double hiveacynthPristineRate;
    public static double hibeescusTicksMultiplier;
    public static double hibeescusManaCostMultiplier;

    public static boolean isActive() {
        return isBotaniaActive;
    }

    public void preInit() {
        if (Loader.isModLoaded(Name) && Config.botaniaActive) {
            isBotaniaActive = true;
        }
    }

    public void init() {
        if (isActive()) {
            getBlocks();
            getItems();

            BotaniaAPIDistanceHelper.registerSubtiles();
        }
    }

    public void postInit() {
        if (isActive()) {
            // Hiveacynth would appreciate it if this list existed.
            BeeManager.populateSpeciesListRarity();

            GameRegistry.addRecipe(
                    new ItemStack(Config.manasteelScoop),
                    new Object[] { "twt", "tmt", " t ", 'm',
                            new ItemStack(itemManaResource, 1, ManaResource.MANASTEEL.ordinal()), 'w', Blocks.wool, 't',
                            Items.stick });
            manasteelScoopRecipe = (IRecipe) CraftingManager.getInstance().getRecipeList()
                    .get(CraftingManager.getInstance().getRecipeList().size() - 1);

            GameRegistry.addRecipe(
                    new ItemStack(Config.manasteelGrafter),
                    new Object[] { "  m", " t ", "t  ", 'm',
                            new ItemStack(itemManaResource, 1, ManaResource.MANASTEEL.ordinal()), 't', Items.stick });
            manasteelGrafterRecipe = (IRecipe) CraftingManager.getInstance().getRecipeList()
                    .get(CraftingManager.getInstance().getRecipeList().size() - 1);

            GameRegistry.addRecipe(
                    new ItemStack(Config.manaAuraProvider),
                    new Object[] { "lsl", "pmp", "sss", 's', blockLivingRock, 'm',
                            new ItemStack(itemManaResource, 1, ManaResource.MANASTEEL.ordinal()), 'p',
                            Config.pollen.getStackForType(PollenType.UNUSUAL), 'l',
                            new ItemStack(BotaniaHelper.itemManaResource, 1, ManaResource.MANA_POWDER.ordinal()) });
            manaBoosterRecipe = (IRecipe) CraftingManager.getInstance().getRecipeList()
                    .get(CraftingManager.getInstance().getRecipeList().size() - 1);

            BotaniaAPIDistanceHelper.setupCraftingAndLexicon();
        }
    }

    public static void getBlocks() {
        blockMysticalFlower = BlockInterface.getBlock(Name, "flower");
        blockLivingRock = BlockInterface.getBlock(Name, "livingrock");
        blockLivingWood = BlockInterface.getBlock(Name, "livingwood");
        blockDreamWood = BlockInterface.getBlock(Name, "dreamwood");

        // Generate 16-colour flowers.
        mysticalFlowerVariants = new ItemStack[16];
        for (int i = 0; i < 16; i++) {
            mysticalFlowerVariants[i] = new ItemStack(blockMysticalFlower, 1, i);
        }
    }

    public static void getItems() {
        itemPetal = ItemInterface.getItem(Name, "petal");
        itemManaResource = ItemInterface.getItem(Name, "manaResource");
        itemPastureSeed = ItemInterface.getItem(Name, "grassSeeds");
        itemRune = ItemInterface.getItem(Name, "rune");
    }

    public static void doBotaniaModuleConfigs(Configuration configuration) {
        Property p;

        p = configuration.get(Config.CATEGORY_BOTANIA, "beegoniaManaMultiplier", 1.0);
        p.comment = "Multiplier for the Beegonia's mana generation. Default: 1.0 (Affects duration, not throughput)";
        beegoniaManaMultiplier = p.getDouble();

        p = configuration.get(Config.CATEGORY_BOTANIA, "hiveacynthManaMultiplier", 1.0);
        p.comment = "Multiplier for the Hiveacynth's mana consumption. Default: 1.0";
        hiveacynthManaMultiplier = p.getDouble();

        p = configuration.get(Config.CATEGORY_BOTANIA, "hiveacynthRainResistRate", 0.1);
        p.comment = "Rate at which the Hiveacynth applies rain resist to spawned bees. Default: 0.1 Setting to 0 will disable.";
        hiveacynthRainResistRate = p.getDouble();

        p = configuration.get(Config.CATEGORY_BOTANIA, "hiveacynthPrincessSpawnRate", 0.09);
        p.comment = "Rate at which the Hiveacynth will spawn a Princess instead of a Drone. Default: 0.09. Setting to 0 will disable.";
        hiveacynthPrincessSpawnRate = p.getDouble();

        p = configuration.get(Config.CATEGORY_BOTANIA, "hiveacynthPristineRate", 0.15);
        p.comment = "Rate at which the Hiveacynth will produce a Pristine Princess, when it produces a princess. Default: 0.15. Setting to 0 will disable, setting to 1 will make every Princess produced pristine..";
        hiveacynthPristineRate = p.getDouble();

        p = configuration.get(Config.CATEGORY_BOTANIA, "hibeescusManaCostMultiplier", 1.0);
        p.comment = "Multiplier on Hibeescus mana cost, base 10,000. Default 1.0. Setting to 0 makes you a huge cheater. <3";
        hibeescusManaCostMultiplier = p.getDouble();

        p = configuration.get(Config.CATEGORY_BOTANIA, "hibeescusTicksMultiplier", 1.0);
        p.comment = "Multiplier for Hibeescus operation tick time. Multiplied against 1.5 Minecraft days with some extra randomness. Default: 1.0. Setting to 0 makes you a huge cheater. <3";
        hibeescusTicksMultiplier = p.getDouble();
    }

    public static boolean requestMana(ItemStack stack, EntityPlayer player, int manaPerDamage, int charges) {
        return ManaItemHandler.requestManaExactForTool(stack, player, manaPerDamage * charges, true);
    }

    public static ItemStack getRune(Rune rune, int quantity) {
        return new ItemStack(itemRune, quantity, rune.ordinal());
    }
}
