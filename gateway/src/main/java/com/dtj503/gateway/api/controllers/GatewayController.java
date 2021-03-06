package com.dtj503.gateway.api.controllers;

import com.dtj503.gateway.api.types.TextSubmission;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * API controller for handling the text submission, distributing the analysis requests, and combining the responses.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
@RestController
public class GatewayController extends RestAPIController {

    /**
     * API analysis endpoint method. Called when a POST request is made to /document.
     *
     * @param submission the text submission to analyze
     */
    @PostMapping(value = "/document", consumes = MediaType.APPLICATION_JSON_VALUE)
    public static void analyze(@RequestBody TextSubmission submission) {
        System.out.println(submission.getText());
    }

}
