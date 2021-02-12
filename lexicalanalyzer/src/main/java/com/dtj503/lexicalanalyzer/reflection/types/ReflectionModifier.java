package com.dtj503.lexicalanalyzer.reflection.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class representing the appraisal modifiers which modify the reflection scoring of a sentence. For more information
 * on where the scores came from, see paper:
 *  Assessing reflective writing: Analysis of reflective writing in an engineering design course
 *  by C Reidsema & P Mort
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
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
