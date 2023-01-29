package magicbees.bees.allele.effect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import magicbees.api.bees.ITransmutationEffectController;
import magicbees.api.bees.ITransmutationEffectLogic;
import magicbees.main.utils.LogHelper;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class TransmutationEffectController implements ITransmutationEffectController {

    private List<ITransmutationEffectLogic> logicObjects;

    public TransmutationEffectController(ITransmutationEffectLogic... effectLogic) {
        logicObjects = new ArrayList<ITransmutationEffectLogic>();
        for (ITransmutationEffectLogic logic : effectLogic) {
            logicObjects.add(logic);
        }
    }

    public void attemptTransmutations(World world, BiomeGenBase biome, ItemStack sourceBlock, int x, int y, int z) {
        Collections.shuffle(logicObjects);
        for (ITransmutationEffectLogic logic : logicObjects) {
            try {
                if (logic.tryTransmutation(world, biome, sourceBlock, x, y, z)) {
                    break;
                }
            } catch (Exception e) {
                LogHelper.warn(
                        String.format(
                                "Magic Bees encountered an issue with an ITransmutationEffectLogic provider %s. Debug information follows.",
                                logic.getClass().getName()));
                LogHelper.info(e.getMessage());
            }
        }
    }

    @Override
    public void addEffectLogic(ITransmutationEffectLogic logic) {
        if (logic != null && !logicObjects.contains(logic)) {
            logicObjects.add(logic);
        }
    }
}
