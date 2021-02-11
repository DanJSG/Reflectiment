package com.dtj503.lexicalanalyzer.common.pools;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
/**
 * Pool for retrieving a StanfordCoreNLP pipeline for annotating text. Contains two models: a fast, lightweight model
 * which can only perform tokenization and Part of Speech (PoS) tagging; and a fuller model which also allows entity
 * detection.
 *
 * If the software will not run because it is running out of memory, it is likely due to the full model. Try commenting
 * this out below and re-running. With both models running the application needs around 2.5 GB of RAM to run.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 *
 */
public final class PipelinePool {

    private static StanfordCoreNLP[] pipeline;

    static {
        // Create a CoreNLP pipeline for tokenize, splitting sentences, tagging parts of speech and finding entities
        // TODO only one pipeline is actually currently present - review if more are needed moving forward and if not
        //      then simplify this class and delete the PipelineType enum
        // Use two separate pipelines - one which is faster and uses less memory but can only be used for tokenization
        // and PoS tagging, and another which can be used for extracting entities such as the subject of a sentence.
        Properties fastProperties = new Properties();
        fastProperties.setProperty("useSUTime", "false");
        fastProperties.setProperty("applyNumericClassifiers", "false");
        fastProperties.setProperty("annotators", "tokenize, ssplit, pos");
        pipeline = new StanfordCoreNLP[1];
        pipeline[0] = new StanfordCoreNLP(fastProperties);
    }

    /**
     * Get a StanfordCoreNLP pipeline from the pool, and fetch either the fast or full model.
     *
     * @param type the model type to fetch
     * @return a StanfordCoreNLP pipeline
     */
    public static StanfordCoreNLP get(PipelineType type) {
        return type == PipelineType.FAST ? pipeline[0] : pipeline[1];
    }

}