package magicbees.bees;

import forestry.api.core.IClimateProvider;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IMutationCondition;
import magicbees.main.utils.LocalizationManager;
import magicbees.main.utils.MoonPhase;
import net.minecraft.world.World;

public class MoonPhaseMutationRestriction implements IMutationCondition {
    private final MoonPhase moonPhaseStart;
    private final MoonPhase moonPhaseEnd;

    public MoonPhaseMutationRestriction(MoonPhase phase) {
        this.moonPhaseStart = phase;
        this.moonPhaseEnd = phase;
    }

    public MoonPhaseMutationRestriction(MoonPhase start, MoonPhase end) {
        this.moonPhaseStart = start;
        this.moonPhaseEnd = end;
    }

    @Override
    public float getChance(
            World world,
            int x,
            int y,
            int z,
            IAllele allele0,
            IAllele allele1,
            IGenome genome0,
            IGenome genome1,
            IClimateProvider climate) {
        if (MoonPhase.getMoonPhase(world).isBetween(this.moonPhaseStart, this.moonPhaseEnd)) {
            return 1;
        }
        return 0;
    }

    @Override
    public String getDescription() {
        if (moonPhaseStart != moonPhaseEnd) {
            return String.format(
                    LocalizationManager.getLocalizedString("research.requiresPhase"),
                    moonPhaseStart.getLocalizedNameAlt(),
                    moonPhaseEnd.getLocalizedNameAlt());
        } else {
            return String.format(
                    LocalizationManager.getLocalizedString("research.requiresPhaseSingle"),
                    moonPhaseStart.getLocalizedName());
        }
    }
}
