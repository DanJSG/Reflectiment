package com.dtj503.lexicalanalyzer.common.types;

import java.util.List;

/**
 * Class representing a document of text. Made up of the original document text and a list of tokenized sentences.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class Document<T extends Token> {

    private String originalText;
    private List<Sentence<T>> sentences;

    public Document(String text, List<Sentence<T>> sentences) {
        this.originalText = text;
        this.sentences = sentences;
    }

    public List<Sentence<T>> getSentences() {
        return sentences;
    }

    @Override
    public String toString() {
        return "Document{" +
                "originalText='" + originalText + '\'' +
                ", sentences=" + sentences +
                '}';
    }

    public String getOriginalText() {
        return originalText;
    }
}
