package com.dtj503.lexicalanalyzer.api.controllers;

import com.dtj503.lexicalanalyzer.api.types.AnalysisResponse;
import com.dtj503.lexicalanalyzer.api.types.TextSubmission;
import com.dtj503.lexicalanalyzer.mood.service.MoodAnalysisService;
import com.dtj503.lexicalanalyzer.mood.types.MoodScoredSentence;
import com.dtj503.lexicalanalyzer.reflection.service.ReflectionAnalysisService;
import com.dtj503.lexicalanalyzer.reflection.types.ReflectionCategory;
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
import java.util.concurrent.*;

@RestController
/**
 * Spring REST API controller for handling the text analysis requests.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class AnalysisController extends RestAPIController {

    @PostMapping(value = "/analyse", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public static ResponseEntity<String> analyse(@RequestBody TextSubmission submission) {

        if(submission == null) {
            return BAD_REQUEST_HTTP_RESPONSE;
        }

        System.out.println("Received request. JSON Received: ");
        System.out.println(submission.writeValueAsString());

        // Open a new thread pool which uses as many logical processors as it can for executing parallel processing
        ExecutorService threadPool = Executors.newWorkStealingPool();

        // Execute the reflection, mood and sentiment analysis processes on independent threads to improve performance.
        // This appears to have a significant impact on longer pieces of text. However, this optimisation will only work
        // with multiple logical CPU cores, otherwise it will have no impact. When implementing this using 6 logical CPU
        // cores on a virtual machine, the execution time was more than halved (somewhere around a 2.2x improvement).
        CompletableFuture<List<ReflectionScoredSentence>> reflectionAnalysisProcess =
                CompletableFuture.supplyAsync(() -> ReflectionAnalysisService.analyseReflection(submission.getText()), threadPool);
        CompletableFuture<List<MoodScoredSentence>> moodAnalysisProcess =
                CompletableFuture.supplyAsync(() -> MoodAnalysisService.analyseMood(submission.getText()), threadPool);
        CompletableFuture<List<SentimentScoredSentence>> sentimentAnalysisProcess =
                CompletableFuture.supplyAsync(() -> SentimentAnalysisService.analyseSentiment(submission.getText()), threadPool);

        // Wait for all processes to finish executing without any blocking the other
        CompletableFuture.allOf(reflectionAnalysisProcess, moodAnalysisProcess, sentimentAnalysisProcess).join();

        // Generate the response using the results from the analysis processes, if something failed due to the parallel
        // processing then simply run the operation consecutively
        AnalysisResponse response = null;
        try {
            response = new AnalysisResponse(submission.getText(), sentimentAnalysisProcess.get(),
                    moodAnalysisProcess.get(), reflectionAnalysisProcess.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();

            List<ReflectionScoredSentence> reflectionScoredSentences =
                ReflectionAnalysisService.analyseReflection(submission.getText());

            List<MoodScoredSentence> moodScoredSentences =
                    MoodAnalysisService.analyseMood(submission.getText());

            List<SentimentScoredSentence> sentimentScoredSentences =
                SentimentAnalysisService.analyseSentiment(submission.getText());

            response = new AnalysisResponse(submission.getText(), sentimentScoredSentences,
                    moodScoredSentences, reflectionScoredSentences);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response.writeValueAsString());

    }

}
