package com.agrejus.netherendingenergy.common.reactor.attributes;

public class AcidAttributes extends PotionAttributes {

    private final float spatial;
    private final int spatialAmount;

    public AcidAttributes(float strength, float efficiency, float stability, float response, int uses, float spatial, int spatialAmount) {
        super(strength,efficiency, stability, response, uses);
        this.spatial = spatial;
        this.spatialAmount = spatialAmount;
    }

    public float getSpatial() {
        return spatial;
    }

    public int getSpatialAmount() {
        return spatialAmount;
    }
}
