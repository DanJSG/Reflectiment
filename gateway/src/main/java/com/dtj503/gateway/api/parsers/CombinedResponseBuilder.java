package com.dtj503.gateway.api.parsers;

import com.dtj503.gateway.api.types.CombinedAnalysedSentence;
import com.dtj503.gateway.api.types.CombinedResponse;
import com.dtj503.gateway.api.types.AnalysisScores;
import com.dtj503.gateway.api.types.ReflectionScore;
import com.dtj503.gateway.api.types.AnalysisResponse;
import com.dtj503.gateway.api.types.ReflectionModifier;

import java.util.ArrayList;
import java.util.List;

public class CombinedResponseBuilder {

    public static CombinedResponse buildCombinedResponse(AnalysisResponse lexicalResponse, AnalysisResponse mlResponse) {

        List<CombinedAnalysedSentence> sentences = new ArrayList<>();

        for(int i = 0; i < lexicalResponse.getSentences().size(); i++) {
            ReflectionModifier modifier = lexicalResponse.getSentences().get(i).getReflectionModifier();
            ReflectionScore score = lexicalResponse.getSentences().get(i).getReflectionScores();
            float updatedScore = modifier.getCombinedAppraisalModifier() * score.getScore();
            ReflectionScore modifiedScore = new ReflectionScore(updatedScore, score.getCategoryScores());
            AnalysisScores lexicalScores = new AnalysisScores(lexicalResponse.getSentences().get(i).getSentimentScores(),
                    lexicalResponse.getSentences().get(i).getMoodScores(), modifiedScore);
            AnalysisScores mlScores = new AnalysisScores(mlResponse.getSentences().get(i).getSentimentScores(),
                    mlResponse.getSentences().get(i).getMoodScores(), mlResponse.getSentences().get(i).getReflectionScores());
            CombinedAnalysedSentence combinedSentence =
                    new CombinedAnalysedSentence(lexicalResponse.getSentences().get(i).getText(), lexicalScores, mlScores);
            sentences.add(combinedSentence);
        }

        return new CombinedResponse(lexicalResponse.getFullText(), sentences);

    }

}
