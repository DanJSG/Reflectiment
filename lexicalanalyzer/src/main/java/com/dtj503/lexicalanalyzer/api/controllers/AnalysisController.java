package com.dtj503.lexicalanalyzer.api.controllers;

import com.dtj503.lexicalanalyzer.api.types.AnalysisResponse;
import com.dtj503.lexicalanalyzer.api.types.TextSubmission;
import com.dtj503.lexicalanalyzer.common.parsers.StringParser;
import com.dtj503.lexicalanalyzer.common.types.Document;
import com.dtj503.lexicalanalyzer.common.types.Token;
import com.dtj503.lexicalanalyzer.mood.service.MoodAnalysisService;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredSentence;
import com.dtj503.lexicalanalyzer.reflection.service.ReflectionAnalysisService;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionModifier;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionScoredSentence;
import com.dtj503.lexicalanalyzer.sentiment.service.SentimentAnalysisService;
import com.dtj503.lexicalanalyzer.sentiment.types.SentimentScoredSentence;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Spring REST API controller for handling the text analysis requests.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
@RestController
public class AnalysisController extends RestAPIController {

    @PostMapping(value = "/analyse", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public static ResponseEntity<String> analyse(@RequestBody TextSubmission submission) {

        if(submission == null) {
            return BAD_REQUEST_HTTP_RESPONSE;
        }

        System.out.println("Received request. JSON Received: ");
        System.out.println(submission.writeValueAsString());

        // Parse the submitted text into a set of word tokens with PoS tags
        Document<Token> document = StringParser.parseText(submission.getText());

        if(document == null) {
            return INTERNAL_SERVER_ERROR_HTTP_RESPONSE;
        }

        // Open a new thread pool which uses as many logical processors as it can for executing parallel processing
        ExecutorService threadPool = Executors.newWorkStealingPool();

        // Execute the reflection, mood and sentiment analysis processes on independent threads to improve performance.
        // This appears to have a significant impact on longer pieces of text. However, this optimisation will only work
        // with multiple logical CPU cores, otherwise it will have no impact. When implementing this using 6 logical CPU
        // cores on a virtual machine, the execution time was more than halved (somewhere around a 2.2x improvement).
        CompletableFuture<List<SentimentScoredSentence>> sentimentAnalysisProcess =
                CompletableFuture.supplyAsync(() -> SentimentAnalysisService.analyseSentiment(document), threadPool);
        CompletableFuture<List<MoodScoredSentence>> moodAnalysisProcess =
                CompletableFuture.supplyAsync(() -> MoodAnalysisService.analyseMood(document), threadPool);
        CompletableFuture<List<ReflectionScoredSentence>> reflectionAnalysisProcess =
                CompletableFuture.supplyAsync(() -> ReflectionAnalysisService.analyseReflection(document), threadPool);
        // Wait for all processes to finish executing without any blocking the other
        CompletableFuture.allOf(reflectionAnalysisProcess, moodAnalysisProcess, sentimentAnalysisProcess).join();

        // Generate the response using the results from the analysis processes, if something failed due to the parallel
        // processing then simply run the operation consecutively
        AnalysisResponse response;
        try {
            // Calculate the reflection modifier coefficients based on the sentiment and mood scores
            // For more info see paper mentioned in class Javadoc
            List<ReflectionModifier> reflectionModifiers = ReflectionAnalysisService.getReflectionModifiers(
                    sentimentAnalysisProcess.get(), moodAnalysisProcess.get());
            response = new AnalysisResponse(document.getOriginalText(), sentimentAnalysisProcess.get(),
                    moodAnalysisProcess.get(), reflectionAnalysisProcess.get(), reflectionModifiers);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();

            // Run process consecutively in case of failure
            List<SentimentScoredSentence> sentimentScoredSentences =
                    SentimentAnalysisService.analyseSentiment(document);

            List<MoodScoredSentence> moodScoredSentences =
                    MoodAnalysisService.analyseMood(document);

            List<ReflectionScoredSentence> reflectionScoredSentences =
                    ReflectionAnalysisService.analyseReflection(document);

            List<ReflectionModifier> reflectionModifiers =
                    ReflectionAnalysisService.getReflectionModifiers(sentimentScoredSentences, moodScoredSentences);

            response = new AnalysisResponse(document.getOriginalText(), sentimentScoredSentences,
                    moodScoredSentences, reflectionScoredSentences, reflectionModifiers);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response.writeValueAsString());

    }

}
