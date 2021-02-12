package com.dtj503.lexicalanalyzer.reflection.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReflectionModifier {

    @JsonProperty
    private final float judgementAppreciationModifier;

    @JsonProperty
    private final float affectModifier;

    @JsonProperty
    private final float combinedAppraisalModifier;

    public ReflectionModifier(float sentimentModifier, float moodModifier) {
        this.judgementAppreciationModifier = sentimentModifier;
        this.affectModifier = moodModifier;
        this.combinedAppraisalModifier = sentimentModifier * moodModifier;
    }


}
