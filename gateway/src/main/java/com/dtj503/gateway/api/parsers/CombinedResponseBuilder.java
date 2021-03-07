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

    public static CombinedResponse buildCombinedResponse(LexicalResponse lexicalResponse) {

        List<CombinedAnalysedSentence> sentences = new ArrayList<>();

        for(LexicalAnalysedSentence sentence : lexicalResponse.getSentences()) {

            ReflectionModifier modifier = sentence.getReflectionModifier();
            LexicalReflectionScore score = sentence.getReflectionScores();
            float updatedScore = modifier.getCombinedAppraisalModifier() * score.getScore();
            LexicalReflectionScore modifiedScore = new LexicalReflectionScore(updatedScore, score.getCategoryScores());
            LexicalScores sentenceScores = new LexicalScores(sentence.getSentimentScores(), sentence.getMoodScores(), modifiedScore);

            CombinedAnalysedSentence combinedSentence = new CombinedAnalysedSentence(sentence.getText(), sentenceScores);
            sentences.add(combinedSentence);

        }

        return new CombinedResponse(lexicalResponse.getFullText(), sentences);

    }

}
