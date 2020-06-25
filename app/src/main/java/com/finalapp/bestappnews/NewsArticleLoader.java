
package com.finalapp.bestappnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsArticleLoader extends AsyncTaskLoader<List<NewsArticle>> {

    private String mUrl;
    public static boolean mThumbnail;


    public NewsArticleLoader(Context context, String url, Boolean prefThumbnail) {
        super(context);
        mUrl = url;
        mThumbnail = prefThumbnail;
    }

    @Override
    protected void onStartLoading() {

        forceLoad();
    }

    @Override
    public List<NewsArticle> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<NewsArticle> newspaperArticle = NewsQueryUtils.fetchArticleData(mUrl);
        return newspaperArticle;
    }
}