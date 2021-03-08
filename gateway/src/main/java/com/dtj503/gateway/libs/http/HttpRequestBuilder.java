package com.dtj503.gateway.libs.http;

import org.springframework.http.HttpMethod;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder for a HTTP request, which can have parameters, headers, URL, method and body set. This can then be converted
 * to a <code>HttpURLConnection</code> object for fetching the response.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class HttpRequestBuilder {

    private List<String> parameters;
    private Map<String, String> headers;
    private String url;
    private String method;
    private String body;

    /**
     * Create a new, blank request builder.
     */
    public HttpRequestBuilder() {
        parameters = new ArrayList<>();
        headers = new HashMap<>();
    }

    /**
     * Create a new request builder with the given URL.
     * @param url the URL to make the request to
     */
    public HttpRequestBuilder(String url) {
        this();
        setUrl(url);
    }

    /**
     * Create a new request builder with the given URL and HTTP request method.
     *
     * @param url the URL to make the request to
     * @param method the method of the HTTP request
     */
    public HttpRequestBuilder(String url, HttpMethod method) {
        this(url);
        setRequestMethod(method);
    }

    /**
     * Set the body content of the request.
     *
     * @param body the body value
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Set the URL to make the request to.
     *
     * @param url the URL as a <code>String</code>
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Add a request parameter to the URL.
     *
     * @param name the name of the parameter
     * @param value the value of the parameter
     * @param <V> the type of the parameter value
     */
    public <V> void addParameter(String name, V value) {
        parameters.add(name + "=" + value);
    }

    /**
     * Add a header to the request. Headers include things such as Content-Type and User-Agent.
     *
     * @param header the name of the header to add
     * @param value the value of the header
     */
    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    /**
     * Set the HTTP request method. Valid options include:
     *  HEAD, GET, POST, PUT, UPDATE, DELETE, CONNECT, OPTIONS, TRACE, and PATCH.
     * @param method the HTTP method to use
     */
    public void setRequestMethod(HttpMethod method) {
        this.method = method.toString();
    }

    /**
     * Build a HTTP URL connection using the parameters set in the builder.
     *
     * @return a HTTP URL connection
     * @throws Exception
     */
    public HttpURLConnection toHttpURLConnection() throws Exception {
        if(this.url == null || this.method == null) {
            throw new Exception();
        }
        // Add parameters to URL
        StringBuilder urlBuilder = new StringBuilder(url);
        boolean firstParamAdded = false;
        if(parameters != null && parameters.size() > 0) {
            for(String param : parameters) {
                if(!firstParamAdded) {
                    urlBuilder.append("?").append(param);
                    firstParamAdded = true;
                } else {
                    urlBuilder.append("&").append(param);
                }
            }
        }
        URL urlWithParams = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) urlWithParams.openConnection();
        // Set properties for the HTTP request
        conn.setRequestMethod(method);
        conn.setConnectTimeout(0);
        conn.setReadTimeout(0);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(true);
        conn.setInstanceFollowRedirects(true);
        // Set the headers
        if(headers != null && headers.size() > 0) {
            headers.forEach(conn::setRequestProperty);
        }
        // Set the body if present
        if (body != null) {
            OutputStream out = conn.getOutputStream();
            byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
            out.write(bodyBytes, 0, bodyBytes.length);
        }
        return conn;
    }

}
