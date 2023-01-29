package magicbees.bees.allele.effect;

import java.util.List;

import magicbees.bees.AlleleEffect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.genetics.IEffectData;

public class AlleleEffectCure extends AlleleEffect {

    public AlleleEffectCure(String id, boolean isDominant) {
        super(id, isDominant, 200);
    }

    @Override
    public IEffectData validateStorage(IEffectData storedData) {
        if (storedData == null) {
            storedData = new EffectData(1, 0, 0);
        }
        return storedData;
    }

    @Override
    public IEffectData doEffectThrottled(IBeeGenome genome, IEffectData storedData, IBeeHousing housing) {
        List<EntityPlayer> entityList = this.getEntitiesWithinRange(genome, housing, EntityPlayer.class);

        for (EntityPlayer player : entityList) {
            player.curePotionEffects(new ItemStack(Items.milk_bucket));
        }
        storedData.setInteger(0, 0);

        return storedData;
    }
}
