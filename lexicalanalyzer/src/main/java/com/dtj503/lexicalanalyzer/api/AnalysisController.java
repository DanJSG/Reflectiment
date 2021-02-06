package com.dtj503.lexicalanalyzer.api;

import com.dtj503.lexicalanalyzer.mood.service.MoodAnalysisService;
import com.dtj503.lexicalanalyzer.sentiment.service.SentimentAnalysisService;
import com.dtj503.lexicalanalyzer.sentiment.types.SentimentScoredSentence;
import com.dtj503.lexicalanalyzer.types.AnalysisResponse;
import com.dtj503.lexicalanalyzer.types.TextSubmission;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
/**
 * Spring REST API controller for handling the text analysis requests.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class AnalysisController extends RestAPIController {

    @PostMapping(value = "/analyse", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public static ResponseEntity<String> analyse(@RequestBody TextSubmission submission) {

        if(submission == null) {
            return BAD_REQUEST_HTTP_RESPONSE;
        }

        System.out.println("Received request. JSON Received: ");
        System.out.println(submission.writeValueAsString());

        MoodAnalysisService.analyseMood(submission.getText());

        List<SentimentScoredSentence> sentimentScoredSentences = SentimentAnalysisService.analyseSentiment(submission.getText());
        AnalysisResponse response = new AnalysisResponse(sentimentScoredSentences);

        return ResponseEntity.status(HttpStatus.OK).body(response.writeValueAsString());

    }

}
