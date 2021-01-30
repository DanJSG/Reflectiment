package com.dtj503.offlinedevelopment.types;

import java.util.List;

public class Document {

    private String originalText;
    private List<Sentence> sentences;

    public Document(String text, List<Sentence> sentences) {
        this.originalText = text;
        this.sentences = sentences;
    }

    public List<Sentence> getSentences() {
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
