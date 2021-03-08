package com.dtj503.gateway.api.types;

import com.dtj503.gateway.analysis.types.AnalysisScores;
import com.dtj503.gateway.analysis.types.ReflectionScore;
import com.dtj503.gateway.analysis.types.AnalysisResponse;
import com.dtj503.gateway.analysis.types.ReflectionModifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for the combined response, which uses multiple analysis responses and combines them together, returning a
 * <code>CombinedResponse</code> object.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class CombinedResponseBuilder {

    /**
     * Builds a <code>CombinedResponse</code> object by combining the lexical and ML analysis responses.
     *
     * @param lexicalResponse the lexical analysis response
     * @param mlResponse the ML analysis response
     * @return the <code>CombinedResponse</code> object
     */
    public static CombinedResponse buildCombinedResponse(AnalysisResponse lexicalResponse, AnalysisResponse mlResponse) {

        List<CombinedSentence> sentences = new ArrayList<>();

        // Loop over each response item, calculate the modified reflection scores and then combine the lexical and ML
        // responses
        for(int i = 0; i < lexicalResponse.getSentences().size(); i++) {
            ReflectionModifier modifier = lexicalResponse.getSentences().get(i).getReflectionModifier();
            ReflectionScore score = lexicalResponse.getSentences().get(i).getReflectionScores();
            float updatedScore = modifier.getCombinedAppraisalModifier() * score.getScore();
            ReflectionScore modifiedScore = new ReflectionScore(updatedScore, score.getCategoryScores());
            AnalysisScores lexicalScores = new AnalysisScores(lexicalResponse.getSentences().get(i).getSentimentScores(),
                    lexicalResponse.getSentences().get(i).getMoodScores(), modifiedScore);
            AnalysisScores mlScores = new AnalysisScores(mlResponse.getSentences().get(i).getSentimentScores(),
                    mlResponse.getSentences().get(i).getMoodScores(), mlResponse.getSentences().get(i).getReflectionScores());
            CombinedSentence combinedSentence =
                    new CombinedSentence(lexicalResponse.getSentences().get(i).getText(), lexicalScores, mlScores);
            sentences.add(combinedSentence);
        }

        return new CombinedResponse(lexicalResponse.getFullText(), sentences);
    }

}
