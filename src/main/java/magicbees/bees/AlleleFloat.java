package magicbees.bees;

import forestry.api.genetics.IAlleleFloat;
import forestry.api.genetics.IChromosomeType;

public class AlleleFloat extends Allele implements IAlleleFloat {

    private float value;

    public AlleleFloat(String id, float val, boolean isDominant, IChromosomeType... chromosomeTypes) {
        super(id, isDominant, chromosomeTypes);
        this.value = val;
    }

    @Override
    public float getValue() {
        return this.value;
    }
}
