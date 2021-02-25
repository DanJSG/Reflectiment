package com.dtj503.lexicalanalyzer.api.controllers;

import com.dtj503.lexicalanalyzer.api.types.AnalysisResponse;
import com.dtj503.lexicalanalyzer.api.types.TextSubmission;
import com.dtj503.lexicalanalyzer.common.parsers.StringParser;
import com.dtj503.lexicalanalyzer.common.sql.MySQLRepository;
import com.dtj503.lexicalanalyzer.common.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.common.sql.SQLRepository;
import com.dtj503.lexicalanalyzer.common.sql.SQLTable;
import com.dtj503.lexicalanalyzer.common.types.DictionaryTag;
import com.dtj503.lexicalanalyzer.common.types.DictionaryTagBuilder;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
public class DocumentController extends RestAPIController {

    @PostMapping(value = "/document", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public static ResponseEntity<String> analyse(@RequestBody final TextSubmission submission,
                                                 @RequestParam(name = "sTag", required = false) final String sTagParam,
                                                 @RequestParam(name = "mTag", required = false) final String mTagParam,
                                                 @RequestParam(name = "rTag", required = false) final String rTagParam) {
        if(submission == null) {
            return BAD_REQUEST_HTTP_RESPONSE;
        }

        String[] tags = checkTagParams(sTagParam, mTagParam, rTagParam);
        String sTag = tags[0];
        String mTag = tags[1];
        String rTag = tags[2];

        System.out.println(sTag);
        System.out.println(mTag);
        System.out.println(rTag);

        System.out.println("Received request. JSON Received: ");
        System.out.println(submission.writeValueAsString());
        // Parse the submitted text into a set of word tokens with PoS tags
        Document<Token> document = StringParser.parseText(submission.getText());
        if(document == null) {
            return INTERNAL_SERVER_ERROR_HTTP_RESPONSE;
        }
        // Generate the response using the results from the analysis processes, if something failed due to the parallel
        // processing then simply run the operation consecutively
        AnalysisResponse response;
        try {
            response = analyseConcurrently(document, sTag, mTag, rTag);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            response = analyseConsecutively(document, sTag, mTag, rTag);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response.writeValueAsString());
    }

    private static AnalysisResponse analyseConcurrently(Document<Token> document, String sTag, String mTag, String rTag) throws ExecutionException, InterruptedException {
        // Open a new thread pool which uses as many logical processors as it can for executing parallel processing
        ExecutorService threadPool = Executors.newWorkStealingPool();
        // Execute the reflection, mood and sentiment analysis processes on independent threads to improve performance.
        // This appears to have a significant impact on longer pieces of text. However, this optimisation will only work
        // with multiple logical CPU cores, otherwise it will have no impact. When implementing this using 6 logical CPU
        // cores on a virtual machine, the execution time was more than halved (somewhere around a 2.2x improvement).
        CompletableFuture<List<SentimentScoredSentence>> sentimentAnalysisProcess =
                CompletableFuture.supplyAsync(() -> SentimentAnalysisService.analyseSentiment(document, sTag), threadPool);
        CompletableFuture<List<MoodScoredSentence>> moodAnalysisProcess =
                CompletableFuture.supplyAsync(() -> MoodAnalysisService.analyseMood(document, mTag, sTag), threadPool);
        CompletableFuture<List<ReflectionScoredSentence>> reflectionAnalysisProcess =
                CompletableFuture.supplyAsync(() -> ReflectionAnalysisService.analyseReflection(document, rTag, sTag), threadPool);
        // Wait for all processes to finish executing without any blocking the other
        CompletableFuture.allOf(reflectionAnalysisProcess, moodAnalysisProcess, sentimentAnalysisProcess).join();
        // Calculate the reflection modifier coefficients based on the sentiment and mood scores
        // For more info see paper mentioned in class Javadoc
        List<ReflectionModifier> reflectionModifiers = ReflectionAnalysisService.getReflectionModifiers(
                sentimentAnalysisProcess.get(), moodAnalysisProcess.get(), reflectionAnalysisProcess.get());
        return new AnalysisResponse(document.getOriginalText(), sentimentAnalysisProcess.get(),
                moodAnalysisProcess.get(), reflectionAnalysisProcess.get(), reflectionModifiers);
    }

    private static AnalysisResponse analyseConsecutively(Document<Token> document, String sTag, String mTag, String rTag) {
        AnalysisResponse response;
        // Run process consecutively in case of failure
        List<SentimentScoredSentence> sentimentScoredSentences =
                SentimentAnalysisService.analyseSentiment(document, sTag);
        List<MoodScoredSentence> moodScoredSentences =
                MoodAnalysisService.analyseMood(document, mTag, sTag);
        List<ReflectionScoredSentence> reflectionScoredSentences =
                ReflectionAnalysisService.analyseReflection(document, rTag, sTag);
        List<ReflectionModifier> reflectionModifiers =
                ReflectionAnalysisService.getReflectionModifiers(sentimentScoredSentences, moodScoredSentences, reflectionScoredSentences);
        response = new AnalysisResponse(document.getOriginalText(), sentimentScoredSentences,
                moodScoredSentences, reflectionScoredSentences, reflectionModifiers);
        return response;
    }

    private static String[] checkTagParams(String sTagParam, String mTagParam, String rTagParam) {
        String[] tagParams = {sTagParam, mTagParam, rTagParam};
        String[] tags = {"sentiwords", "nrc", "ullman_ext"};
        int editedCount = 0;
        for(int i = 0; i < tags.length; i++) {
            if(tagParams[i] == null) {
                editedCount++;
            }
        }
        if(editedCount == 3) {
            return tags;
        }
        SQLRepository<DictionaryTag> repo = new MySQLRepository<>(SQLTable.TAGS);
        List<DictionaryTag> fetchedTags = repo.findWhereEqualAndOr(SQLColumn.TAG, SQLColumn.TBL_IDX, Arrays.asList(tagParams), Arrays.asList(0, 1, 2), new DictionaryTagBuilder());
        if(fetchedTags == null) {
            return tags;
        }
        fetchedTags.sort(Comparator.comparingInt(DictionaryTag::getIndex));
        for(int i = 0; i < tagParams.length; i++) {
            tags[i] = fetchedTags.contains(new DictionaryTag(tagParams[i], i)) ? tagParams[i] : tags[i];
        }
        return tags;
    }

}
