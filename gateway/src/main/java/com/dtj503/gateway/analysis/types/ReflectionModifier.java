package com.dtj503.gateway.analysis.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Reflection modifier values. Float multipliers used for modification of reflection scores.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class ReflectionModifier {

    @JsonProperty
    private float judgementAppreciationModifier;

    @JsonProperty
    private float affectModifier;

    @JsonProperty
    private float combinedAppraisalModifier;

    /**
     * Get the combined reflection modifier.
     *
     * @return a <code>float</code> of the combined reflection modifier
     */
    public float getCombinedAppraisalModifier() {
        return combinedAppraisalModifier;
    }
}
