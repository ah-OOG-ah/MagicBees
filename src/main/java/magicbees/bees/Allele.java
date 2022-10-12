package magicbees.bees;

import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.IAlleleBeeEffect;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleEffect;
import forestry.api.genetics.IAlleleFlowers;
import forestry.api.genetics.IAlleleRegistry;
import forestry.api.genetics.IChromosomeType;
import forestry.api.genetics.IFlowerProvider;
import magicbees.api.MagicBeesAPI;
import magicbees.bees.allele.effect.AlleleEffectCrumbling;
import magicbees.bees.allele.effect.AlleleEffectCure;
import magicbees.bees.allele.effect.AlleleEffectEmpowering;
import magicbees.bees.allele.effect.AlleleEffectNodeConversion;
import magicbees.bees.allele.effect.AlleleEffectNodeRepair;
import magicbees.bees.allele.effect.AlleleEffectPlaceholder;
import magicbees.bees.allele.effect.AlleleEffectPotion;
import magicbees.bees.allele.effect.AlleleEffectRecharge;
import magicbees.bees.allele.effect.AlleleEffectSpawnMob;
import magicbees.bees.allele.effect.AlleleEffectSpawnMobWeighted;
import magicbees.bees.allele.effect.AlleleEffectSpawnWisp;
import magicbees.bees.allele.effect.AlleleEffectTransmuting;
import magicbees.bees.allele.effect.TransmutationEffectController;
import magicbees.bees.allele.effect.TransmutationEffectLBotaniaLiving;
import magicbees.bees.allele.effect.TransmutationEffectRailcraft;
import magicbees.bees.allele.effect.TransmutationEffectVanilla;
import magicbees.bees.allele.flowerProvider.FlowerProviderArsMagicaFlower;
import magicbees.bees.allele.flowerProvider.FlowerProviderAuraNode;
import magicbees.bees.allele.flowerProvider.FlowerProviderBookshelf;
import magicbees.bees.allele.flowerProvider.FlowerProviderBotania;
import magicbees.bees.allele.flowerProvider.FlowerProviderThaumcraftFlower;
import magicbees.main.utils.LocalizationManager;
import magicbees.main.utils.compat.ArsMagicaHelper;
import magicbees.main.utils.compat.BotaniaHelper;
import magicbees.main.utils.compat.ThaumcraftHelper;
import magicbees.main.utils.compat.ThermalModsHelper;
import net.minecraft.potion.Potion;
import thaumcraft.api.nodes.NodeType;

public class Allele implements IAllele {

    private static final String FLOWER_ARS_MAGICA_PLANT = "flowerArsMagicaPlant";
    private static final String FLOWER_BOTANIA = "flowerBotania";
    private static final String EFFECT_TE_BLIZZY = "TEBlizzy";
    private static final String EFFECT_TE_BLITZ = "TEBlitz";
    private static final String EFFECT_TE_BASALZ = "TEBasalz";
    private static final String EFFECT_AM_WISP = "AMWisp";
    private static final String EFFECT_MANA_DRAIN = "ManaDrain";
    private static final String EFFECT_DREAMING = "Dreaming";
    private static final String EFFECT_WISPY = "Wispy";
    private static final String EFFECT_BATTY = "Batty";
    private static final String EFFECT_BRAINY = "Brainy";
    private static final String EFFECT_NODE_RAVENING = "NodeRavening";
    private static final String EFFECT_NODE_PURIFYING = "NodePurifying";
    private static final String EFFECT_NODE_TAINTING = "NodeTainting";
    private static final String EFFECT_NODE_REPAIR = "NodeRepair";
    private static final String EFFECT_NODE_EMPOWER = "NodeEmpower";
    private static final String EFFECT_VIS_RECHARGE = "VisRecharge";
    private static final String FLOWERS_AURA_NODE = "AuraNode";
    private static final String FLOWERS_THAUMCRAFT_PLANT = "ThaumcraftPlant";

    public static AlleleFloat speedBlinding;

    public static IAlleleBeeEffect forestryBaseEffect;

    public static IAlleleFlowers flowerBookshelf;
    public static IAlleleFlowers flowerThaumcraft;
    public static IAlleleFlowers flowerArsMagica;
    public static IAlleleFlowers flowerAuraNode;
    public static IAlleleFlowers flowerBotania;

    public static IAlleleEffect effectCleansing;
    public static IAlleleEffect effectDigSpeed;
    public static IAlleleEffect effectMoveSpeed;
    public static IAlleleEffect effectSlowSpeed;
    public static IAlleleEffect effectWithering;
    public static IAlleleEffect effectVisRecharge;
    public static IAlleleEffect effectNodeEmpower;
    public static IAlleleEffect effectNodeRepair;
    public static IAlleleEffect effectNodeConversionTaint;
    public static IAlleleEffect effectNodeConversionPure;
    public static IAlleleEffect effectNodeConversionHungry;
    public static IAlleleEffect effectTransmuting;
    public static IAlleleEffect effectCrumbling;
    public static IAlleleEffect effectInvisibility;
    public static IAlleleEffect effectDreaming;

    public static IAlleleEffect spawnWolf;
    public static IAlleleEffect spawnBats;
    public static IAlleleEffect spawnCow;
    public static IAlleleEffect spawnChicken;
    public static IAlleleEffect spawnPig;
    public static IAlleleEffect spawnSheep;
    public static IAlleleEffect spawnCat;
    public static IAlleleEffect spawnHorse;
    public static IAlleleEffect spawnGhast;
    public static IAlleleEffect spawnSpider;
    public static IAlleleEffect spawnBlaze;
    public static IAlleleEffect spawnBrainyZombie;
    public static IAlleleEffect spawnWisp;
    public static IAlleleEffect spawnBlizz;
    public static IAlleleEffect spawnBlitz;
    public static IAlleleEffect spawnBasalz;
    public static IAlleleEffect spawnManaDrainer;
    public static IAlleleEffect spawnWispOrHecate;

    public static void setupAdditionalAlleles() {
        forestryBaseEffect = (IAlleleBeeEffect) getBaseAllele("effectNone");
        IFlowerProvider vanillaFlowers = ((IAlleleFlowers) getBaseAllele("flowersVanilla")).getProvider();

        speedBlinding = new AlleleFloat("speedBlinding", 2f, false, EnumBeeChromosome.SPEED);
        flowerBookshelf = new AlleleFlower("Bookshelf", new FlowerProviderBookshelf(), true);

        effectCleansing = new AlleleEffectCure("Curative", false);
        effectDigSpeed = new AlleleEffectPotion("DigSpeed", Potion.digSpeed, 15, false);
        effectMoveSpeed = new AlleleEffectPotion("MoveSpeed", Potion.moveSpeed, 10, false);
        effectSlowSpeed = new AlleleEffectPotion("SlowSpeed", Potion.moveSlowdown, 3, false).setMalicious();
        effectWithering = new AlleleEffectPotion("Withering", Potion.wither, 10, false).setMalicious();

        TransmutationEffectController controller =
                new TransmutationEffectController(new TransmutationEffectRailcraft(), new TransmutationEffectVanilla());
        MagicBeesAPI.transmutationEffectController = controller;
        effectTransmuting = new AlleleEffectTransmuting("Transmuting", true, controller, 200);
        effectCrumbling = new AlleleEffectCrumbling("Crumbling", true);

        effectInvisibility = new AlleleEffectPotion("Invisibility", Potion.invisibility, 10, false);

        spawnGhast = new AlleleEffectSpawnMob("Ghastly", false, "Ghast", "mob.ghast.moan")
                .setThrottle(2060)
                .setChanceToSpawn(10)
                .setMaxMobsInSpawnZone(1);
        spawnSpider = new AlleleEffectSpawnMob("Spidery", false, "Spider", "mob.spider.step")
                .setThrottle(400)
                .setChanceToSpawn(70)
                .setMaxMobsInSpawnZone(4);
        spawnBlaze = new AlleleEffectSpawnMob("Ablaze", false, "Blaze", "mob.blaze.breathe")
                .setThrottle(800)
                .setChanceToSpawn(60)
                .setMaxMobsInSpawnZone(2);
        spawnWolf = new AlleleEffectSpawnMob("Canine", false, "Wolf", "mob.wolf.panting")
                .setThrottle(650)
                .setChanceToSpawn(40)
                .setMaxMobsInSpawnZone(2);
        spawnPig = new AlleleEffectSpawnMob("Porcine", true, "Pig", "mob.pig.say")
                .setThrottle(350)
                .setMaxMobsInSpawnZone(4);
        spawnCow = new AlleleEffectSpawnMob("Bovine", true, "Cow", "mob.cow.say")
                .setThrottle(640)
                .setMaxMobsInSpawnZone(3);
        spawnChicken = new AlleleEffectSpawnMob("Chicken", true, "Chicken", "mob.chicken.hurt")
                .setThrottle(20)
                .setMaxMobsInSpawnZone(20);
        spawnSheep = new AlleleEffectSpawnMob("Sheep", false, "Sheep", "mob.sheep.say")
                .setThrottle(450)
                .setMaxMobsInSpawnZone(5);
        spawnCat = new AlleleEffectSpawnMob("Catty", false, "Ozelot", "mob.cat.meow")
                .setThrottle(702)
                .setChanceToSpawn(60)
                .setMaxMobsInSpawnZone(2);
        spawnHorse = new AlleleEffectSpawnMob("Horse", false, "EntityHorse", "mob.horse.idle")
                .setThrottle(450)
                .setChanceToSpawn(59)
                .setMaxMobsInSpawnZone(2);

        if (ThaumcraftHelper.isActive()) {
            flowerThaumcraft = new AlleleFlower(FLOWERS_THAUMCRAFT_PLANT, new FlowerProviderThaumcraftFlower(), false);
            flowerAuraNode = new AlleleFlower(FLOWERS_AURA_NODE, new FlowerProviderAuraNode(), true);

            effectVisRecharge = new AlleleEffectRecharge(EFFECT_VIS_RECHARGE, false);
            effectNodeEmpower = new AlleleEffectEmpowering(EFFECT_NODE_EMPOWER, false);
            effectNodeRepair = new AlleleEffectNodeRepair(EFFECT_NODE_REPAIR, false);
            effectNodeConversionTaint =
                    new AlleleEffectNodeConversion(EFFECT_NODE_TAINTING, NodeType.TAINTED, false, 250);
            effectNodeConversionPure = new AlleleEffectNodeConversion(EFFECT_NODE_PURIFYING, NodeType.PURE, false, 250);
            effectNodeConversionHungry =
                    new AlleleEffectNodeConversion(EFFECT_NODE_RAVENING, NodeType.HUNGRY, false, 2);

            spawnBrainyZombie = new AlleleEffectSpawnMob(
                            EFFECT_BRAINY, false, ThaumcraftHelper.Entity.BRAINY_ZOMBIE.entityID)
                    .setAggrosPlayerOnSpawn()
                    .setThrottle(800)
                    .setSpawnsOnPlayerNear(null)
                    .setMaxMobsInSpawnZone(2);

            spawnBats = new AlleleEffectSpawnMob(EFFECT_BATTY, false, ThaumcraftHelper.Entity.FIREBAT.entityID)
                    .setThrottle(300)
                    .setSpawnsOnPlayerNear("Bat");

            spawnWisp = new AlleleEffectSpawnWisp(
                            EFFECT_WISPY, false, ThaumcraftHelper.Entity.WISP.entityID, "thaumcraft.wisplive")
                    .setThrottle(1800)
                    .setChanceToSpawn(79);
        } else {
            spawnBats = new AlleleEffectSpawnMob(EFFECT_BATTY, false, "Bat").setThrottle(150);

            flowerThaumcraft = new AlleleFlower(FLOWERS_THAUMCRAFT_PLANT, vanillaFlowers, false);
            flowerAuraNode = new AlleleFlower(FLOWERS_AURA_NODE, vanillaFlowers, true);
            Allele.effectVisRecharge = new AlleleEffectRecharge(EFFECT_VIS_RECHARGE, false);
            effectNodeEmpower = new AlleleEffectPlaceholder(EFFECT_NODE_EMPOWER, false);
            effectNodeRepair = new AlleleEffectPlaceholder(EFFECT_NODE_REPAIR, false);
            effectNodeConversionTaint = new AlleleEffectPlaceholder(EFFECT_NODE_TAINTING, false);
            effectNodeConversionPure = new AlleleEffectPlaceholder(EFFECT_NODE_PURIFYING, false);
            effectNodeConversionHungry = new AlleleEffectPlaceholder(EFFECT_NODE_RAVENING, false);
            spawnBrainyZombie = new AlleleEffectPlaceholder(EFFECT_BRAINY, false);
            spawnWisp = new AlleleEffectPlaceholder(EFFECT_WISPY, false);
        }

        if (BotaniaHelper.isActive()) {
            flowerBotania = new AlleleFlower(FLOWER_BOTANIA, new FlowerProviderBotania(), true);
            effectDreaming = new AlleleEffectTransmuting(
                    EFFECT_DREAMING,
                    false,
                    new TransmutationEffectController(new TransmutationEffectLBotaniaLiving()),
                    100);
        } else {
            flowerBotania = new AlleleFlower(FLOWER_BOTANIA, vanillaFlowers, true);
            effectDreaming = new AlleleEffectPlaceholder(EFFECT_DREAMING, false);
        }

        if (ArsMagicaHelper.isActive()) {
            flowerArsMagica = new AlleleFlower(FLOWER_ARS_MAGICA_PLANT, new FlowerProviderArsMagicaFlower(), false);
            spawnManaDrainer = new AlleleEffectSpawnMobWeighted(
                    EFFECT_MANA_DRAIN,
                    true,
                    20,
                    new String[] {ArsMagicaHelper.Name + ".MobManaCreeper", ArsMagicaHelper.Name + ".ManaVortex"},
                    new int[] {60, 2});

            spawnWispOrHecate = new AlleleEffectSpawnMobWeighted(
                    EFFECT_AM_WISP,
                    true,
                    20,
                    new String[] {ArsMagicaHelper.Name + ".MobWisp", ArsMagicaHelper.Name + ".MobHecate"},
                    new int[] {40, 3});
        } else {
            flowerArsMagica = new AlleleFlower(FLOWER_ARS_MAGICA_PLANT, vanillaFlowers, false);
            spawnManaDrainer = new AlleleEffectPlaceholder(EFFECT_MANA_DRAIN, true);
            spawnWispOrHecate = new AlleleEffectPlaceholder(EFFECT_AM_WISP, true);
        }

        // TODO TE spawning
        if (ThermalModsHelper.isActive()) {
            spawnBlizz = new AlleleEffectSpawnMob(
                            EFFECT_TE_BLIZZY,
                            true,
                            ThermalModsHelper.Entity.BLIZZ.entityID,
                            ThermalModsHelper.Entity.BLIZZ.soundName)
                    .setThrottle(100)
                    .setChanceToSpawn(80);
            spawnBlitz = new AlleleEffectSpawnMob(
                            EFFECT_TE_BLITZ,
                            true,
                            ThermalModsHelper.Entity.BLITZ.entityID,
                            ThermalModsHelper.Entity.BLITZ.soundName)
                    .setThrottle(100)
                    .setChanceToSpawn(80);
            spawnBasalz = new AlleleEffectSpawnMob(
                            EFFECT_TE_BASALZ,
                            true,
                            ThermalModsHelper.Entity.BASALZ.entityID,
                            ThermalModsHelper.Entity.BASALZ.soundName)
                    .setThrottle(100)
                    .setChanceToSpawn(80);
        } else {
            spawnBlizz = new AlleleEffectPlaceholder(EFFECT_TE_BLIZZY, true);
            spawnBlitz = new AlleleEffectPlaceholder(EFFECT_TE_BLITZ, true);
            spawnBasalz = new AlleleEffectPlaceholder(EFFECT_TE_BASALZ, true);
        }
    }

    public static void registerDeprecatedAlleleReplacements() {
        IAlleleRegistry registry = AlleleManager.alleleRegistry;

        registry.registerDeprecatedAlleleReplacement("magicbees.effectNodeAttract", effectNodeEmpower);
        registry.registerDeprecatedAlleleReplacement("magicbees.effectNodePurify", effectNodeConversionPure);
        registry.registerDeprecatedAlleleReplacement("magicbees.effectNodeFlux", effectNodeConversionTaint);
        registry.registerDeprecatedAlleleReplacement("magicbees.effectNodeCharge", effectNodeEmpower);
        registry.registerDeprecatedAlleleReplacement(
                "magicbees.speciesTCAttractive", BeeSpecies.TC_EMPOWERING.getSpecies());
        registry.registerDeprecatedAlleleReplacement(
                "magicbees.speciesBMBloodyBMBloody", BeeSpecies.EARTHY.getSpecies());
        registry.registerDeprecatedAlleleReplacement(
                "magicbees.speciesBMBloodyBMBound", BeeSpecies.ARCANE.getSpecies());
    }

    public static IAlleleBeeSpecies getBaseSpecies(String name) {
        return (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele(
                (new StringBuilder()).append("forestry.species").append(name).toString());
    }

    public static IAlleleBeeSpecies getExtraSpecies(String name) {
        return (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele((new StringBuilder())
                .append("extrabees.species.")
                .append(name.toLowerCase())
                .toString());
    }

    public static IAllele getBaseAllele(String name) {
        return AlleleManager.alleleRegistry.getAllele("forestry." + name);
    }

    public static IAllele getAllele(String name) {
        return AlleleManager.alleleRegistry.getAllele(name);
    }

    private String uid;
    private boolean dominant;

    public Allele(String id, boolean isDominant, IChromosomeType... chromosomeTypes) {
        this.uid = "magicbees." + id;
        this.dominant = isDominant;
        AlleleManager.alleleRegistry.registerAllele(this, chromosomeTypes);
    }

    @Override
    public String getUID() {
        return this.uid;
    }

    @Override
    public boolean isDominant() {
        return this.dominant;
    }

    @Override
    public String getName() {
        return LocalizationManager.getLocalizedString(getUID());
    }

    @Override
    public String getUnlocalizedName() {
        return this.uid;
    }
}
