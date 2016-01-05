/**
 Copyright 2016 Udey Rishi
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.udeyrishi.androidelasticsearchdatamanager.elasticsearchhelpers;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.udeyrishi.androidelasticsearchdatamanager.Preconditions;
import com.udeyrishi.simplehttpclient.HttpClient;
import com.udeyrishi.simplehttpclient.HttpResponse;
import com.udeyrishi.simplehttpclient.HttpStatusCode;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * A set for helped APIs for interacting with Elastic Search server.
 * Created by rishi on 15-11-12.
 */
public class ElasticSearchHelper {
    private static final String LOG_TAG = "HTTPDataManager";

    private final HttpClient client;

    /**
     * Creates an instance of {@link ElasticSearchHelper}.
     *
     * @param elasticSearchRootUrl ElasticSearch root URL.
     */
    public ElasticSearchHelper(String elasticSearchRootUrl) {
        try {
            client = new HttpClient(Preconditions.checkNotNullOrWhitespace(elasticSearchRootUrl, "rootUrl"));
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage());
            throw new ElasticSearchHelperInitializationException(e.getMessage());
        }
    }

    /**
     * Makes an HTTP GET request at the URL formed with the provided suffix, and returns the
     * received JSON String if an HTTP OK is received. If HTTP NOT-FOUND is received,
     * {@link android.content.res.Resources.NotFoundException} is thrown.
     *
     * @param suffix The suffix to be used for making the request.
     * @return The retrieved JSON string if the HTTP OK is received.
     * @throws IOException Thrown if the network communication fails.
     */
    public String getJson(String suffix) throws IOException {
        HttpResponse response = client.makeGetRequest(suffix);

        if (response.getResponseCode() == HttpStatusCode.NOT_FOUND.getStatusCode()) {
            throw new Resources.NotFoundException(suffix);
        }
        if (response.getResponseCode() == HttpStatusCode.OK.getStatusCode()) {
            return extractSourceFromElasticSearchHttpResponse(response);
        }

        throw new RuntimeException(String.format("Dev note: Unexpected response '%d' from the GET Elastic Search endpoint.",
                response.getResponseCode()));
    }

    /**
     * Makes an HTTP PUT request at the URL formed with the provided suffix, and checks for the
     * response code to be a successful one.
     *
     * @param json   The JSON string to be sent.
     * @param suffix The suffix to be used for making the request.
     * @return The response to the request.
     * @throws IOException Thrown if the network communication fails.
     */
    public String putJson(String json, String suffix) throws IOException {
        byte[] requestContents = json.getBytes();
        HttpResponse response = client.makePutRequest(suffix, requestContents);
        return extractResponseString(response);
    }

    /**
     * Makes an HTTP POST request at the URL formed with the provided suffix, and checks for the
     * response code to be a successful one.
     *
     * @param json   The JSON string to be sent.
     * @param suffix The suffix to be used for making the request.
     * @return The response to the request.
     * @throws IOException Thrown if the network communication fails.
     */
    public String postJson(String json, String suffix) throws IOException {
        byte[] requestContents = json.getBytes();
        HttpResponse response = client.makePostRequest(suffix, requestContents);
        return extractResponseString(response);
    }

    /**
     * Makes an HTTP GET request at the path formed with provided suffix, and checks for the response
     * to see if the path exists.
     *
     * @param suffix The suffix to be used for making the request.
     * @return If the HTTP response is OK, returns true. If the response is NOT-FOUND, returns false.
     * @throws IOException Thrown if the network communication fails.
     */
    public boolean checkPathExists(String suffix) throws IOException {
        HttpResponse response = client.makeGetRequest(suffix);

        if (response.getResponseCode() == HttpStatusCode.OK.getStatusCode()) {
            return true;
        }
        if (response.getResponseCode() == HttpStatusCode.NOT_FOUND.getStatusCode()) {
            return false;
        }

        throw new RuntimeException(String.format("Dev note: Unexpected response '%d' from the GET Elastic Search endpoint.",
                response.getResponseCode()));
    }

    /**
     * Sends an HTTP DELETE request at the path formed with the provided suffix.
     *
     * @param suffix The suffix to be used for making the request.
     * @return True, if the response is OK (i.e., path existed). False, if the response is NOT-FOUND
     * (i.e., path didn't exist).
     * @throws IOException Thrown if the network communication fails.
     */
    public boolean sendDeleteRequestAtPath(String suffix) throws IOException {
        HttpResponse response = client.makeDeleteRequest(suffix);
        int statusCode = response.getResponseCode();

        if (statusCode == HttpStatusCode.OK.getStatusCode()) {
            return true;
        }
        if (statusCode == HttpStatusCode.NOT_FOUND.getStatusCode()) {
            return false;
        }

        throw new RuntimeException(String.format("Dev note: Unexpected response '%d' from the DELETE Elastic Search endpoint.",
                response.getResponseCode()));
    }

    private String extractSourceFromElasticSearchHttpResponse(HttpResponse response) {
        String responseContents = new String(response.getContents());
        JsonParser jp = new JsonParser();
        JsonElement responseContentsJSON = jp.parse(responseContents);
        return responseContentsJSON.getAsJsonObject().getAsJsonObject("_source").toString();
    }


    @NonNull
    private String extractResponseString(HttpResponse response) {
        String requestResponse = new String(response.getContents());
        if (response.getResponseCode() != HttpStatusCode.OK.getStatusCode() &&
                response.getResponseCode() != HttpStatusCode.CREATED.getStatusCode()) {
            throw new RuntimeException(String.format("Dev note: Unexpected response '%d' from the POST/PUT Elastic Search endpoint.: %s",
                    response.getResponseCode(), new String(response.getContents())));
        }

        return requestResponse;
    }
}
