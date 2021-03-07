package com.dtj503.gateway.api.parsers;

import com.dtj503.gateway.api.types.CombinedAnalysedSentence;
import com.dtj503.gateway.api.types.CombinedResponse;
import com.dtj503.gateway.api.types.LexicalScores;
import com.dtj503.gateway.api.types.lexical.LexicalAnalysedSentence;
import com.dtj503.gateway.api.types.lexical.LexicalReflectionScore;
import com.dtj503.gateway.api.types.lexical.LexicalResponse;
import com.dtj503.gateway.api.types.lexical.ReflectionModifier;

import java.util.ArrayList;
import java.util.List;

public class CombinedResponseBuilder {

    public static CombinedResponse buildCombinedResponse(LexicalResponse lexicalResponse, LexicalResponse mlResponse) {

        List<CombinedAnalysedSentence> sentences = new ArrayList<>();

        for(int i = 0; i < lexicalResponse.getSentences().size(); i++) {
            ReflectionModifier modifier = lexicalResponse.getSentences().get(i).getReflectionModifier();
            LexicalReflectionScore score = lexicalResponse.getSentences().get(i).getReflectionScores();
            float updatedScore = modifier.getCombinedAppraisalModifier() * score.getScore();
            LexicalReflectionScore modifiedScore = new LexicalReflectionScore(updatedScore, score.getCategoryScores());
            LexicalScores lexicalScores = new LexicalScores(lexicalResponse.getSentences().get(i).getSentimentScores(),
                    lexicalResponse.getSentences().get(i).getMoodScores(), modifiedScore);
            LexicalScores mlScores = new LexicalScores(mlResponse.getSentences().get(i).getSentimentScores(),
                    mlResponse.getSentences().get(i).getMoodScores(), mlResponse.getSentences().get(i).getReflectionScores());
            CombinedAnalysedSentence combinedSentence =
                    new CombinedAnalysedSentence(lexicalResponse.getSentences().get(i).getText(), lexicalScores, mlScores);
            sentences.add(combinedSentence);
        }

        return new CombinedResponse(lexicalResponse.getFullText(), sentences);

    }

}
