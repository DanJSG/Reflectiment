package com.dtj503.gateway.api.controllers;

import com.dtj503.gateway.api.types.TextSubmission;
import com.dtj503.gateway.libs.http.HttpRequestBuilder;
import com.dtj503.gateway.libs.http.HttpResponse;
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

/**
 * API controller for handling the text submission, distributing the analysis requests, and combining the responses.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
@RestController
public class GatewayController extends RestAPIController {

    private static final String LEXICAL_URI = "http://localhost:8081/api/v1/document";
    private static final String ML_URI = "http://localhost:8082/api/v1/document";

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
        System.out.println(submission.getText());
        String lexicalResponseJson = getLexicalAnalysis(submission.writeValueAsString(), Arrays.asList(sTagParam, mTagParam, rTagParam));
        if(lexicalResponseJson == null) {
            return INTERNAL_SERVER_ERROR_HTTP_RESPONSE;
        }
        return ResponseEntity.status(HttpStatus.OK).body(lexicalResponseJson);
    }

    /**
     * Sends a HTTP request to the lexical analyzer microservice to get the lexical analysis results and returns the
     * JSON string.
     *
     * @param requestJson the JSON body of the request
     * @param params the tag parameters in the order of [sTag, mTag, rTag]
     * @return the JSON response from the lexical service in <code>String</code> format, or <code>null</code>
     */
    public static String getLexicalAnalysis(String requestJson, List<String> params) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder(LEXICAL_URI, HttpMethod.POST);
        requestBuilder.setBody(requestJson);
        requestBuilder.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        requestBuilder = addParameters(requestBuilder, params);
        try {
            HttpResponse response = new HttpResponse(requestBuilder.toHttpURLConnection());
            return response.getBody();
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

}
