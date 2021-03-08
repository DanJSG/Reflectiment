package com.dtj503.gateway.libs.http;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestBuilder {

    private List<String> parameters;
    private Map<String, String> headers;
    private String url;
    private String method;
    private String body;

    public HttpRequestBuilder() {
        parameters = new ArrayList<>();
        headers = new HashMap<>();
    }

    public HttpRequestBuilder(String url) {
        this();
        setUrl(url);
    }

    public HttpRequestBuilder(URL url) {
        setUrl(url.toString());
    }

    public HttpRequestBuilder(String url, HttpMethod method) {
        this(url);
        setRequestMethod(method);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public <V> void addParameter(String name, V value) {
        parameters.add(name + "=" + value);
    }

    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    public void setRequestMethod(HttpMethod method) {
        this.method = method.toString();
    }

    public HttpURLConnection toHttpURLConnection() throws Exception {
        if(this.url == null || this.method == null)
            throw new Exception();
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
        HttpURLConnection conn = (HttpURLConnection)urlWithParams.openConnection();
        conn.setRequestMethod(method);
        int timeouts = 0;
        conn.setConnectTimeout(timeouts);
        conn.setReadTimeout(timeouts);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(true);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        if(headers != null && headers.size() > 0) {
            headers.forEach(conn::setRequestProperty);
        }
        if (body != null) {
            OutputStream out = conn.getOutputStream();
            byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
            out.write(bodyBytes, 0, bodyBytes.length);
        }
        return conn;
    }

}
