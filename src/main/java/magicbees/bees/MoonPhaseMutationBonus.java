package magicbees.bees;

import net.minecraft.world.World;

import forestry.api.core.IClimateProvider;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IMutationCondition;

import magicbees.main.utils.LocalizationManager;
import magicbees.main.utils.MoonPhase;

// There is a bonus to this mutation during moon phases...
public class MoonPhaseMutationBonus implements IMutationCondition {
	private final MoonPhase moonPhaseStart;
	private final MoonPhase moonPhaseEnd;
	private final float mutationBonus;

	public MoonPhaseMutationBonus(MoonPhase phase, float bonus) {
		this.moonPhaseStart = phase;
		this.moonPhaseEnd = phase;
		this.mutationBonus = bonus;
	}

	public MoonPhaseMutationBonus(MoonPhase start, MoonPhase end, float bonus) {
		this.moonPhaseStart = start;
		this.moonPhaseEnd = end;
		this.mutationBonus = bonus;
	}

	@Override
	public float getChance(World world, int x, int y, int z, IAllele allele0, IAllele allele1, IGenome genome0, IGenome genome1, IClimateProvider climate) {
		if (MoonPhase.getMoonPhase(world).isBetween(this.moonPhaseStart, this.moonPhaseEnd)) {
			return this.mutationBonus;
		}
		return 1;
	}

	@Override
	public String getDescription() {
		if (this.moonPhaseStart != this.moonPhaseEnd) {
			return String.format(LocalizationManager.getLocalizedString("research.bonusPhase"),
					moonPhaseStart.getLocalizedNameAlt(), moonPhaseEnd.getLocalizedNameAlt());
		} else {
			return String.format(LocalizationManager.getLocalizedString("research.bonusPhaseSingle"),
					moonPhaseStart.getLocalizedName());
		}
	}
}
