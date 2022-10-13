package magicbees.bees.allele.effect;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.genetics.IEffectData;
import java.util.List;
import magicbees.bees.AlleleEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AlleleEffectPotion extends AlleleEffect {
    private int potionId;
    private int duration;
    private boolean isMalicious;

    public AlleleEffectPotion(String name, Potion potionApplied, int effectDuration, boolean isDominant) {
        super(name, isDominant, 200);
        this.potionId = potionApplied.id;
        this.duration = 20 * effectDuration;
        this.isMalicious = false;
    }

    public AlleleEffectPotion setMalicious() {
        this.isMalicious = true;

        return this;
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
        List<EntityLivingBase> entityList = this.getEntitiesWithinRange(genome, housing, EntityLivingBase.class);

        for (EntityLivingBase e : entityList) {
            if (this.isMalicious) {
                int armorPieces = BeeManager.armorApiaristHelper.wearsItems(e, getUID(), true);
                int finalDuration = this.duration / 4 * (4 - armorPieces);
                if (finalDuration > 0) {
                    e.addPotionEffect(new PotionEffect(this.potionId, finalDuration, 0));
                }
            } else {
                if (e instanceof EntityPlayer) {
                    e.addPotionEffect(new PotionEffect(this.potionId, this.duration, 0));
                }
            }
        }
        storedData.setInteger(0, 0);

        return storedData;
    }
}
