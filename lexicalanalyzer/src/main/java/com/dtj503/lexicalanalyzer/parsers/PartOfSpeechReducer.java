package com.dtj503.lexicalanalyzer.parsers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to handle the reduction of the CoreNLP Part of Speech tagging to tags for nouns, verbs, adverbs and adjectives.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class PartOfSpeechReducer {

    // Set up the tag mapping
    private static final Map<String, String> POS_REDUCER_MAPPING;
    private static final Map<String, Integer> POS_INDEX_MAPPING;
    private static final String[] POS_TAG_MAPPING = new String[]{"v", "n", "r", "a"};
    static {

        POS_INDEX_MAPPING = new HashMap<>();
        POS_INDEX_MAPPING.put("v", 0);
        POS_INDEX_MAPPING.put("n", 1);
        POS_INDEX_MAPPING.put("r", 2);
        POS_INDEX_MAPPING.put("a", 3);

        POS_REDUCER_MAPPING = new HashMap<>();
        // Adverbs
        POS_REDUCER_MAPPING.put("JJ", "a");
        POS_REDUCER_MAPPING.put("JJR", "a");
        POS_REDUCER_MAPPING.put("JJS", "a");

        // Nouns
        POS_REDUCER_MAPPING.put("NN", "n");
        POS_REDUCER_MAPPING.put("NNS", "n");
        POS_REDUCER_MAPPING.put("NNP", "n");
        POS_REDUCER_MAPPING.put("NNPS", "n");
        POS_REDUCER_MAPPING.put("PRP", "n");
        POS_REDUCER_MAPPING.put("PRP$", "n");
        POS_REDUCER_MAPPING.put("WP", "n");
        POS_REDUCER_MAPPING.put("WP$", "n");

        // Verbs
        POS_REDUCER_MAPPING.put("MD", "v");
        POS_REDUCER_MAPPING.put("VB", "v");
        POS_REDUCER_MAPPING.put("VBD", "v");
        POS_REDUCER_MAPPING.put("VBG", "v");
        POS_REDUCER_MAPPING.put("VBN", "v");
        POS_REDUCER_MAPPING.put("VBP", "v");
        POS_REDUCER_MAPPING.put("VBZ", "v");

        // Adverbs
        POS_REDUCER_MAPPING.put("RB", "r");
        POS_REDUCER_MAPPING.put("RBR", "r");
        POS_REDUCER_MAPPING.put("RBS", "r");
        POS_REDUCER_MAPPING.put("WRB", "r");

    }

    /**
     * Method to reduce the part of speech tag down to noun, verb, adverb or adjective (n, v, r, a).
     *
     * @param posTag the original PoS tag
     * @return the updated tag, or <code>null</code> if the PoS is not one of the desired categories
     */
    public static String simplifyPartOfSpeechTag(String posTag) {
        return POS_REDUCER_MAPPING.containsKey(posTag) ? POS_REDUCER_MAPPING.get(posTag) : null;
    }

    public static int getPartOfSpeechIndex(String posTag) {
        return POS_INDEX_MAPPING.containsKey(posTag) ? POS_INDEX_MAPPING.get(posTag) : -1;
    }

    public static String getPartOfSpeechTag(int posIndex) {
        return POS_TAG_MAPPING[posIndex];
    }

}
