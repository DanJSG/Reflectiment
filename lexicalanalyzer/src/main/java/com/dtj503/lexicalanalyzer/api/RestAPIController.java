package com.dtj503.lexicalanalyzer.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/api/v1/")
/**
 * Abstract class for a Spring Boot REST Controller, containing common HTTP responses. Maps every inheriting class
 * endpoints underneath /api/v1/.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public abstract class RestAPIController {

    // Set of common HTTP response constants
    protected static final ResponseEntity<String>
            UNAUTHORIZED_HTTP_RESPONSE = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null),
            BAD_REQUEST_HTTP_RESPONSE = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null),
            INTERNAL_SERVER_ERROR_HTTP_RESPONSE = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null),
            NO_CONTENT_HTTP_RESPONSE = ResponseEntity.status(HttpStatus.NO_CONTENT).body(null),
            EMPTY_OK_HTTP_RESPONSE = ResponseEntity.status(HttpStatus.OK).body(null),
            METHOD_NOT_ALLOWED_HTTP_RESPONSE = ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(null),
            NOT_FOUND_HTTP_RESPONSE = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

}
