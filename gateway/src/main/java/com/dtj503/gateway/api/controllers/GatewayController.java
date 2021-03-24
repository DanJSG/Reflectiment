package com.dtj503.gateway.api.controllers;

import com.dtj503.gateway.api.types.CombinedResponseBuilder;
import com.dtj503.gateway.api.types.CombinedResponse;
import com.dtj503.gateway.analysis.types.AnalysisResponse;
import com.dtj503.gateway.api.types.TextSubmission;
import com.dtj503.gateway.libs.http.HttpRequestBuilder;
import com.dtj503.gateway.libs.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

/**
 * API controller for handling the text submission, distributing the analysis requests, and combining the responses.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
@RestController
public class GatewayController extends RestAPIController {

    private static final String LEXICAL_URI = "http://localhost:8081/api/v1/document";
    private static final String ML_URI = "http://localhost:8082/api/v1/document";

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayController.class);

    /**
     * API analysis endpoint method. Called when a POST request is made to /document.
     *
     * @param submission the text submission to analyze
     * @return
     */
    @PostMapping(value = "/document", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public static ResponseEntity<String> analyze(@RequestBody TextSubmission submission,
                                                 @RequestParam(name = "sTag", required = false) final String sTagParam,
                                                 @RequestParam(name = "mTag", required = false) final String mTagParam,
                                                 @RequestParam(name = "rTag", required = false) final String rTagParam) {
        LOGGER.info("Request received: \"" + submission.getText() + "\"");
        if(!isSubmissionTextValid(submission.getText())) {
            return BAD_REQUEST_HTTP_RESPONSE;
        }
        AnalysisResponse lexicalResponse = getAnalysisFromUrl(LEXICAL_URI, submission.writeValueAsString(),
                Arrays.asList(sTagParam, mTagParam, rTagParam), AnalysisResponse.class);
        AnalysisResponse mlResponse = getAnalysisFromUrl(ML_URI, submission.writeValueAsString(),
                Arrays.asList(sTagParam, mTagParam, rTagParam), AnalysisResponse.class);
        if(lexicalResponse == null || mlResponse == null) {
            return INTERNAL_SERVER_ERROR_HTTP_RESPONSE;
        }
        CombinedResponse response = CombinedResponseBuilder.buildCombinedResponse(lexicalResponse, mlResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response.writeValueAsString());
    }

    /**
     * Sends a HTTP request to one of the analyzer microservices to get the analysis results and return the JSON string.
     *
     * @param requestJson the JSON body of the request
     * @param params the tag parameters in the order of [sTag, mTag, rTag]
     * @param responseClass the Java class to build the JSON response as
     * @return the JSON response from the lexical service in <code>String</code> format, or <code>null</code>
     */
    public static <T> T getAnalysisFromUrl(String url, String requestJson, List<String> params, Class<T> responseClass) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder(url, HttpMethod.POST);
        requestBuilder.setBody(requestJson);
        requestBuilder.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        requestBuilder = addParameters(requestBuilder, params);
        try {
            HttpResponse response = new HttpResponse(requestBuilder.toHttpURLConnection());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getBody(), responseClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds tag parameters to a HTTP request if they are present.
     *
     * @param request the HTTP request builder to add the parameters to
     * @param params the tag parameters
     * @return the updated HTTP request
     */
    public static HttpRequestBuilder addParameters(HttpRequestBuilder request, List<String> params) {
        String[] paramNames = {"sTag", "mTag", "rTag"};
        for(int i = 0; i < params.size(); i++) {
            if(params.get(i) == null) {
                continue;
            }
            request.addParameter(paramNames[i], params.get(i));
        }
        return request;
    }

    /**
     * Checks that submission contains alphanumeric characters and does not start with sentence ending punctuation.
     * @param text
     * @return
     */
    public static boolean isSubmissionTextValid(String text) {
        Pattern alphaNumericPattern = Pattern.compile("[a-zA-Z0-9].*");
        Pattern leadingPunctuationPattern = Pattern.compile("^[.!?]");
        return alphaNumericPattern.matcher(text).find() && !leadingPunctuationPattern.matcher(text).find();
    }

}
