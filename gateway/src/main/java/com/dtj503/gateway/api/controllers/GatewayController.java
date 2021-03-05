package com.dtj503.gateway.api.controllers;

import com.dtj503.gateway.api.types.TextSubmission;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController extends RestAPIController {

    @PostMapping(value = "/document", consumes = MediaType.APPLICATION_JSON_VALUE)
    public static void analyze(@RequestBody TextSubmission submission) {
        System.out.println(submission.getText());
    }

}
