
package com.finalapp.bestappnews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

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

public final class NewsQueryUtils {


    private static final int MAX_READ_TIMEOUT = 10000;
    private static final int MAX_CONNECTION_TIMEOUT = 15000;/* milliseconds */


    private static final String LOG_TAG = "NewsQueryUtils";

    private NewsQueryUtils() {
    }

    public static List<NewsArticle> fetchArticleData(String requestUrl) {

        URL murl = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(murl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link NewsArticle}s
        List<NewsArticle> ArticleNews = extractFeatureFromJson(jsonResponse);
        return ArticleNews;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL.", e);
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            Log.v(LOG_TAG, "jsonResponse: " + jsonResponse);
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(MAX_READ_TIMEOUT /* milliseconds */);
            urlConnection.setConnectTimeout(MAX_CONNECTION_TIMEOUT /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                Log.v(LOG_TAG, "inputStream != null. Closing input stream.");
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readStream(InputStream inputStream) throws IOException {
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

    private static List<NewsArticle> extractFeatureFromJson(String articleJSON) {
        String webSectionName;
        String webPublicationDate;
        String webTitle;
        String webUrl;
        String webTrailText;
        String byLine;
        String thumbnail;

        if (TextUtils.isEmpty(articleJSON)) {
            Log.v(LOG_TAG, "The JSON string is empty or null. Returning early.");
            return null;
        }

        List<NewsArticle> newsArticles = new ArrayList<>();


        try {

            JSONObject jsonObjectRat = new JSONObject(articleJSON);


            JSONObject jsonObjectResponse = jsonObjectRat.getJSONObject("response");

            JSONArray jsonArrayResults = jsonObjectResponse.getJSONArray("results");

            for (int p = 0; p < jsonArrayResults.length(); p++) {


                JSONObject currentArticle = jsonArrayResults.getJSONObject(p);


                JSONObject jsonObjectFields = currentArticle.getJSONObject("fields");



                webSectionName = currentArticle.optString("sectionName");
                webPublicationDate = currentArticle.optString("webPublicationDate");
                webTitle = jsonObjectFields.getString("headline");
                webTrailText = jsonObjectFields.optString("trailText");
                webUrl = jsonObjectFields.getString("shortUrl");
                byLine = jsonObjectFields.optString("byline");
                thumbnail = jsonObjectFields.optString("thumbnail");


                newsArticles.add(new NewsArticle(
                        webSectionName,
                        webPublicationDate,
                        webTitle,
                        html2txt(webTrailText),
                        webUrl,
                        byLine,
                        downloadBitmap(thumbnail)
                ));
            }

        } catch (JSONException e) {

            Log.e(LOG_TAG, "NewsQueryUtils: Problem parsing the article JSON results", e);

            Toast.makeText(MyApplication.takeappContext(), R.string.no_json_ok_response, Toast.LENGTH_LONG).show();
        }


        return newsArticles;
    }

    public static String html2txt(String html)
    {

        return Jsoup.parse(html).text();
    }
    public static boolean isConnected(Context context) {
        boolean connected = false;
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            connected = true;
        }
        return connected;
    }


    private static Bitmap downloadBitmap(String originalUrl) {
        Bitmap bitmap = null;

        if (!"".equals(originalUrl)) {
            String newUrl = originalUrl.replace
                    (originalUrl.substring(originalUrl.lastIndexOf("/")), "/1000.jpg");
            try {

                InputStream inputStream = new URL(newUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                try {
                    InputStream inputStream = new URL(originalUrl).openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (Exception ignored) {
                }
            }
        }
        return bitmap;
    }



}