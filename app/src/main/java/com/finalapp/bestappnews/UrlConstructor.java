package com.finalapp.bestappnews;

import android.support.annotation.Nullable;
import android.util.Log;

public final class UrlConstructor {

    private static final String URL_EXTRAS = "&show-fields=headline,trailText,shortUrl,thumbnail,byline";
    private static final String apiKey = BuildConfig.ApiKey;
    private static final String URL_API_KEY = "&api-key=" + apiKey;
    private static final String LOG_TAG = "UrlConstructor";

    private static final String URL_BASE = "https://content.guardianapis.com/search?";



    public static String constructUrl(@Nullable String section, @Nullable String orderBy) {

        StringBuilder getstring = new StringBuilder();
        getstring.append(URL_BASE);

        if (section != null) {
            getstring.append(section);
        } else {
            getstring.append(MyApplication.takeappContext().getResources().getString(R.string.pref_topic_0_label_value));
        }

        if (orderBy != null) {
            getstring.append("&order-by="
                    + orderBy);
        } else {
            getstring.append("&order-by="
                    + MyApplication.takeappContext().getResources().getString(R.string.pref_order_by_default));
        }

        getstring.append(URL_EXTRAS);

        getstring.append(URL_API_KEY);

        Log.i(LOG_TAG, "API GUARDIAN_REQUEST_URL: " + getstring.toString());

        return getstring.toString();
    }
}
