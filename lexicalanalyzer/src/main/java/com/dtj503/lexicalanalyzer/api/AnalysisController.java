package com.dtj503.lexicalanalyzer.api;

import com.dtj503.lexicalanalyzer.sentiment.service.SentimentAnalysisService;
import com.dtj503.lexicalanalyzer.types.TextSubmission;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
/**
 * Spring REST API controller for handling the text analysis requests.
 */
public class AnalysisController extends RestAPIController {

    @PostMapping(value = "/analyse", consumes = MediaType.APPLICATION_JSON_VALUE)
    public static ResponseEntity<String> analyse(@RequestBody TextSubmission submission) {

        if(submission == null) {
            return BAD_REQUEST_HTTP_RESPONSE;
        }

        System.out.println("Received request. JSON Received: ");
        System.out.println(submission.writeValueAsString());

        SentimentAnalysisService.analyseSentiment(submission.getText());

        return EMPTY_OK_HTTP_RESPONSE;

    }

}
