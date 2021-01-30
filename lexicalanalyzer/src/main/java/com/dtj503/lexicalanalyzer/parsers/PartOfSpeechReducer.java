package com.dtj503.lexicalanalyzer.parsers;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to handle the reduction of the CoreNLP Part of Speech tagging to tags for nouns, verbs, adverbs and adjectives.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class PartOfSpeechReducer {

    // Set up the tag mapping
    private static final Map<String, String> POS_TAG_MAPPING;
    static {
        POS_TAG_MAPPING = new HashMap<>();
        // Adverbs
        POS_TAG_MAPPING.put("JJ", "a");
        POS_TAG_MAPPING.put("JJR", "a");
        POS_TAG_MAPPING.put("JJS", "a");

        // Nouns
        POS_TAG_MAPPING.put("NN", "n");
        POS_TAG_MAPPING.put("NNS", "n");
        POS_TAG_MAPPING.put("NNP", "n");
        POS_TAG_MAPPING.put("NNPS", "n");
        POS_TAG_MAPPING.put("PRP", "n");
        POS_TAG_MAPPING.put("PRP$", "n");
        POS_TAG_MAPPING.put("WP", "n");
        POS_TAG_MAPPING.put("WP$", "n");

        // Verbs
        POS_TAG_MAPPING.put("MD", "v");
        POS_TAG_MAPPING.put("VB", "v");
        POS_TAG_MAPPING.put("VBD", "v");
        POS_TAG_MAPPING.put("VBG", "v");
        POS_TAG_MAPPING.put("VBN", "v");
        POS_TAG_MAPPING.put("VBP", "v");
        POS_TAG_MAPPING.put("VBZ", "v");

        // Adverbs
        POS_TAG_MAPPING.put("RB", "r");
        POS_TAG_MAPPING.put("RBR", "r");
        POS_TAG_MAPPING.put("RBS", "r");
        POS_TAG_MAPPING.put("WRB", "r");

    }

    /**
     * Method to reduce the part of speech tag down to noun, verb, adverb or adjective (n, v, r, a).
     *
     * @param posTag the original PoS tag
     * @return the updated tag, or <code>null</code> if the PoS is not one of the desired categories
     */
    public static String simplifyPartOfSpeechTag(String posTag) {
        return POS_TAG_MAPPING.containsKey(posTag) ? POS_TAG_MAPPING.get(posTag) : null;
    }

}
