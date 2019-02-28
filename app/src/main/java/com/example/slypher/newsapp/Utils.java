package com.example.slypher.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news data from Guardian API.
 */

public class Utils {
    /** Tag for the log messages */
    private static final String LOG_TAG = Utils.class.getSimpleName();
    private static final String JSONOBJECT_ROOT = "response";
    private static final String JSONARRAY_RESULT = "results";
    private static final String FIELD_TITLE = "webTitle";
    private static final String FIELD_SECTION = "sectionName";
    private static final String FIELD_WEB_URL = "webUrl";
    private static final String FIELD_PUBLICATION_DATE = "webPublicationDate";
    private static final String JSONARRAY_TAGS = "tags";
    private static final String FIELD_FIRST_NAME = "firstName";
    private static final String FIELD_LAST_NAME = "lastName";

    /**
     * private constructor because no one should ever create a {@link Utils} object.
     */
    private Utils() {
    }

    public static List<News> fetchNewsData(String requestUrl){

        // Create URL object
        URL url = createUrl(requestUrl);

        // HTTP request, receive JSON response

        String jsonResponse = null;

        try{
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request. ", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}s
        List<News> listNews = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return listNews;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * HTTP request
     * @param url
     * @return JSON response
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an ArrayList for news
        List<News> listNews = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject rootJsonResponse = new JSONObject(newsJSON);
            JSONObject responseJSON = rootJsonResponse.optJSONObject(JSONOBJECT_ROOT);

            JSONArray newsArray = responseJSON.getJSONArray(JSONARRAY_RESULT);

            for (int i = 0; i < newsArray.length(); i++) {

                // Get any information that interests us
                JSONObject currentNews = newsArray.optJSONObject(i);
                String webTitle = currentNews.optString(FIELD_TITLE);
                String sectionName = currentNews.optString(FIELD_SECTION);
                String webUrl = currentNews.optString(FIELD_WEB_URL);
                String publicationDate = currentNews.optString(FIELD_PUBLICATION_DATE);
                JSONArray tags = currentNews.optJSONArray(JSONARRAY_TAGS);
                // See News.java and NewsAdapater.java for the management of the presence of authors
                String firstName = "";
                String lastName = "";
                if (tags != null) {
                    for (int j = 0; j < tags.length(); j++) {
                        JSONObject currentTag = tags.optJSONObject(j);
                        firstName = firstToUpercase(currentTag.optString(FIELD_FIRST_NAME));
                        lastName = firstToUpercase(currentTag.optString(FIELD_LAST_NAME));
                    }
                }
                /**
                 * Create a new {@link News} object
                 */
                News news = new News(webTitle, sectionName, publicationDate, firstName, lastName, webUrl);

                listNews.add(news);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }

        return listNews;
    }

    private static String firstToUpercase(String word){

        String result = "";

        if(!word.isEmpty() && !word.equals("")){
            result =  word.substring(0, 1).toUpperCase() + word.substring(1);
        }

        return result;
    }
}
