package com.dtj503.lexicalanalyzer.parsers;

import java.util.HashMap;
import java.util.Map;

public class PartOfSpeechReducer {

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

    public static String simplifyPartOfSpeechTag(String posTag) {
        return POS_TAG_MAPPING.containsKey(posTag) ? POS_TAG_MAPPING.get(posTag) : null;
    }

}
