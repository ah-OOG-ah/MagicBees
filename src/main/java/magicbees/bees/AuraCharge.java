package magicbees.bees;

import magicbees.api.bees.AuraChargeType;

public enum AuraCharge {
    MUTATION(AuraChargeType.MUTATION, 850, 11),
    DEATH(AuraChargeType.DEATH, 400, 5),
    PRODUCTION(AuraChargeType.PRODUCTION, 300, 3),
    ;

    public final AuraChargeType type;
    public final int duration;
    /** The number of ticks to count between draining vis from the environment **/
    public final int tickRate;

    public final int flag;

    AuraCharge(AuraChargeType chargeType, int duration, int tickRate) {
        this.type = chargeType;
        this.duration = duration;
        this.tickRate = tickRate;
        flag = 1 << ordinal();
    }

    public static AuraCharge fromChargeType(AuraChargeType type) {
        for (AuraCharge charge : values()) {
            if (type == charge.type) {
                return charge;
            }
        }

        return null;
    }
}
