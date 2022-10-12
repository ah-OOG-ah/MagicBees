package magicbees.bees.allele.effect;

import magicbees.api.bees.ITransmutationEffectLogic;
import magicbees.main.utils.compat.BotaniaHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.oredict.OreDictionary;

public class TransmutationEffectLBotaniaLiving implements ITransmutationEffectLogic {

    @Override
    public boolean tryTransmutation(World world, BiomeGenBase biome, ItemStack sourceBlock, int x, int y, int z) {
        int[] oreIDs = OreDictionary.getOreIDs(sourceBlock);
        for (int oreId : oreIDs) {
            if (oreId == OreDictionary.getOreID("logWood")) {
                world.setBlock(x, y, z, BotaniaHelper.blockLivingWood);
                return true;
            } else if (oreId == OreDictionary.getOreID("stone")) {
                world.setBlock(x, y, z, BotaniaHelper.blockLivingRock);
                return true;
            } else if (oreId == OreDictionary.getOreID("livingwood")) {
                world.setBlock(x, y, z, BotaniaHelper.blockDreamWood);
                return true;
            }
        }
        return false;
    }
}
