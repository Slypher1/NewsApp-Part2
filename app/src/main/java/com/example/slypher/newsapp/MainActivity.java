package com.example.slypher.newsapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.slypher.newsapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String URL_GUARDIAN_API = "https://content.guardianapis.com/search";
    private static final String SEARCH_KEY = "q";
    private static final String SEARCH_PARAMETER = "mcu";
    private static final String SECTION_KEY = "section";
    private static final String SECTION_PARAMETER = "film";
    private static final String ORDER_BY_KEY = "order-by";
    private static final String MAX_NEWS_KEY = "page-size";
    private static final String TAGS_KEY = "show-tags";
    private static final String TAGS_PARAMETER = "contributor";
    private static final String API_KEY = "api-key";
    private static final String API_PARAMETER = "test";
    private static final int NEWS_LOADER_ID = 1;

    ActivityMainBinding binding;
    NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        // Set my custom toolbar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");

        // Create a new adapter that takes an empty list of news as input
        adapter = new NewsAdapter(this, new ArrayList<News>());

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        binding.newsListView.setEmptyView(binding.emptyTextView);

        // check internet connection
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            getLoaderManager().initLoader(NEWS_LOADER_ID,null, this);
        } else {
            binding.loadingSpinner.setVisibility(View.GONE);
            binding.emptyTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String maxNews = sharedPrefs.getString(getString(R.string.settings_max_news_key), getString(R.string.settings_max_news_default));
        String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(URL_GUARDIAN_API);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value.
        uriBuilder.appendQueryParameter(SEARCH_KEY, SEARCH_PARAMETER);
        uriBuilder.appendQueryParameter(SECTION_KEY, SECTION_PARAMETER);
        uriBuilder.appendQueryParameter(ORDER_BY_KEY, orderBy);
        uriBuilder.appendQueryParameter(MAX_NEWS_KEY, maxNews);
        uriBuilder.appendQueryParameter(TAGS_KEY, TAGS_PARAMETER);
        uriBuilder.appendQueryParameter(API_KEY, API_PARAMETER);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        binding.loadingSpinner.setVisibility(View.GONE);
        binding.emptyTextView.setText(R.string.no_news_found);

        if (news != null && !news.isEmpty()){
            adapter.clear();
            adapter = new NewsAdapter((Activity) binding.newsListView.getContext(), (ArrayList<News>) news);
        }

        updateUI();
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }

    private void updateUI(){

        binding.newsListView.setAdapter(adapter);

        binding.newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uri webpage = Uri.parse(adapter.getItem(i).getWebUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
