package com.dtj503.gateway.libs.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * HTTP response object created from a HTTP URL connection. Sends the request upon creation.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class HttpResponse {

    private final int status;
    private final String message;
    private final String method;
    private final Map<String, List<String>> headers;
    private final String url;
    private final String queryString;
    private String body;

    /**
     * Create a HTTP response from a valid HttpURLConnection.
     *
     * @param connection the connection to get the response from
     * @throws Exception if the request fails
     */
    public HttpResponse(HttpURLConnection connection) throws Exception {
        status = connection.getResponseCode();
        message = connection.getResponseMessage();
        headers = connection.getHeaderFields();
        method = connection.getRequestMethod();
        URL requestUrl = connection.getURL();
        url = requestUrl.toString();
        queryString = requestUrl.getQuery();
        readResponseBody(connection);
    }

    /**
     * Read the body of the HTTP response.
     *
     * @param connection the connection to get the body from
     * @throws IOException exception if the connection fails
     */
    private void readResponseBody(HttpURLConnection connection) throws IOException {
        InputStream inputStream;
        if(status > 299) {
            inputStream  = connection.getErrorStream();
        } else {
            inputStream = connection.getInputStream();
        }
        if(inputStream == null) {
            return;
        }
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer bodyContent = new StringBuffer();
        String bodyLine;
        while ((bodyLine = inputReader.readLine()) != null) {
            bodyContent.append(bodyLine);
        }
        body = bodyContent.toString();
        inputReader.close();
        connection.disconnect();
    }

    /**
     * Get the body of the response.
     *
     * @return response body as a <code>String</code>
     */
    public String getBody() {
        return body;
    }

    /**
     * Get the HTTP response status code.
     *
     * @return HTTP status code
     */
    public int getStatus() {
        return status;
    }

    /**
     * Get the HTTP response status message.
     *
     * @return HTTP response status message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the HTTP method the request was sent with.
     *
     * @return HTTP method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Get the headers the request was sent with.
     *
     * @return the request headers
     */
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * Get a specific header that the request was sent with.
     *
     * @param header the name of the header to get
     * @return the value of the specified header or null
     */
    public List<String> getHeader(String header) {
        return headers.get(header);
    }

    /**
     * Get the URL the request was sent to.
     *
     * @return the requested URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Get the query string of parameters attached to the URL.
     *
     * @return the query string
     */
    public String getQueryString() {
        return queryString;
    }

}
