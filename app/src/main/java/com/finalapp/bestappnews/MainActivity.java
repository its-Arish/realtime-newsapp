
package com.finalapp.bestappnews;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsArticle>> {

    private static final int ARTICLE_LOADER_ID = 1;

    private ListView articlelist;

    private NewsArticleAdapter newsAdapter;
    private TextView emptytextView;
    private SwipeRefreshLayout containerswipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        containerswipe = findViewById(R.id.swipeContainer);
        containerswipe.setRefreshing(true);

        articlelist = findViewById(R.id.list);

        emptytextView = findViewById(R.id.feedback_view);
        articlelist.setEmptyView(emptytextView);
        emptytextView.setText("");

        newsAdapter = new NewsArticleAdapter(this, new ArrayList<NewsArticle>());

        articlelist.setAdapter(newsAdapter);
        articlelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                NewsArticle currentNewsArticle = newsAdapter.getItem(position);

                Uri articleUri = Uri.parse(currentNewsArticle.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                startActivity(websiteIntent);
            }
        });
        loadData();
        containerswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadData();
            }
        });

        containerswipe.setColorSchemeResources(
                R.color.backgroundColor,
                R.color.primaryHilight);
    }

    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int i, Bundle bundle) {
        // Get User Preferences or Defaults from Settings
        String SECTION_CHOICE = getPreferenceStringValue(R.string.pref_topic_key, R.string.pref_topic_default);
        String ORDER_BY = getPreferenceStringValue(R.string.pref_order_by_key, R.string.pref_order_by_default);
        boolean PREF_THUMBNAIL = getPreferenceBooleanValue(R.string.pref_thumbnail_key, R.bool.pref_thumbnail_default);
        TextView SectionTitle = findViewById(R.id.toolbar_subtitle);
        SectionTitle.setText(HashMapper.urlToLabel(SECTION_CHOICE));

        String GUARDIAN_SECTION = UrlConstructor.constructUrl(SECTION_CHOICE, ORDER_BY);

        return new NewsArticleLoader(this, GUARDIAN_SECTION, PREF_THUMBNAIL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsArticle>> loader, List<NewsArticle> newsArticles) {
        containerswipe.setRefreshing(false);


        emptytextView.setText(R.string.no_articles);
        newsAdapter.clear();

        if (newsArticles != null && !newsArticles.isEmpty()) {
            newsAdapter.addAll(newsArticles);
        } else {
            if (NewsQueryUtils.isConnected(getBaseContext())) {
                emptytextView.setText(R.string.no_articles);
            } else {
                Toast.makeText(getApplicationContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsArticle>> loader) {
        newsAdapter.clear();
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                containerswipe.setRefreshing(true);
                loadData();
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadData() {
        if (NewsQueryUtils.isConnected(getBaseContext())) {
            getLoaderManager().destroyLoader(1);
            emptytextView.setText("");
            getLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this);

        } else {
            containerswipe.setRefreshing(false);
            Toast.makeText(MyApplication.takeappContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
    }
    public boolean getPreferenceBooleanValue(int key, int defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean(
                getString(key),
                getResources().getBoolean(defaultValue)
        );
    }
    public String getPreferenceStringValue(int key, int defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(
                getString(key),
                getString(defaultValue)
        );
    }


}