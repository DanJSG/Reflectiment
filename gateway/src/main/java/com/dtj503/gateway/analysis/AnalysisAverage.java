package com.dtj503.gateway.analysis;

import com.dtj503.gateway.analysis.types.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Used for calculating the averages between two analysed sentences.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class AnalysisAverage {

    /**
     * Calculate the averages of the sentiment, mood and reflection analysis between two analysed sentences. Return this
     * average as an analysed scores object.
     *
     * @param a an analysed sentence
     * @param b the same sentence as a with different analysis scores
     * @return the average analysis scores
     */
    public static AnalysisScores averageScores(AnalysedSentence a, AnalysedSentence b) {
        SentimentScore sentimentScore = averageSentimentScores(a.getSentimentScores(), b.getSentimentScores());
        MoodScore moodScore = averageMoodScores(a.getMoodScores(), b.getMoodScores());
        ReflectionScore reflectionScore = averageReflectionScore(a.getReflectionScores(), b.getReflectionScores());
        return new AnalysisScores(sentimentScore, moodScore, reflectionScore);
    }

    /**
     * Calculates the average mood scores of a sentence.
     *
     * @param a the first mood score
     * @param b the second mood score
     * @return the average of the two mood scores
     */
    private static MoodScore averageMoodScores(MoodScore a, MoodScore b) {

        // Get the mood score maps
        Map<String, Float> aMixedScores = a.getMixedScores();
        Map<String, Float> bMixedScores = b.getMixedScores();

        // Initialise new mood score variables
        Map<String, Float> newMixedScores = new HashMap<>();
        String label = "none";
        float highestScore = 0;

        // Calculate the average for each value in the maps and track the highest score and associated label
        for(String key : aMixedScores.keySet()) {
            float average = (aMixedScores.get(key) + bMixedScores.get(key)) / 2;
            label = (average > highestScore) ? key : label;
            highestScore = Math.max(average, highestScore);
            newMixedScores.put(key, average);
        }

        return new MoodScore(highestScore, label, newMixedScores);
    }

    /**
     * Calculate the average sentiment score of a sentence.
     * @param a the first sentiment score
     * @param b the second sentiment score
     * @return the average of the two scores, as a <code>SentimentScore</code> object
     */
    private static SentimentScore averageSentimentScores(SentimentScore a, SentimentScore b) {
        float average = (a.getScore() + b.getScore()) / 2;
        String label = average <= -0.33 ? "negative" : average < 0.33 ? "neutral" : "positive";
        return new SentimentScore(average, label);
    }

    /**
     * Calculate the average reflection score of a sentence.
     * @param a the first reflection score
     * @param b the second reflection score
     * @return the average of the two scores, as a <code>ReflectionScore</code> object
     */
    private static ReflectionScore averageReflectionScore(ReflectionScore a, ReflectionScore b) {
        float average = (a.getScore() + b.getScore()) / 2;
        return new ReflectionScore(average);
    }

}
