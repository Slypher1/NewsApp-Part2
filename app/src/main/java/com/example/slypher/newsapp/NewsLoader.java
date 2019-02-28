package com.example.slypher.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    String urlQuery;

    public NewsLoader(Context context, String url) {
        super(context);
        if(url == null){
            return;
        }
        urlQuery = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        return Utils.fetchNewsData(urlQuery);
    }
}
