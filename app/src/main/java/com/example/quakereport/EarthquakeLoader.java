package com.example.quakereport;

import android.content.Context;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    private final String mUrl;

    public EarthquakeLoader(Context context, String url, ArrayList<String> arrayList){
        super(context);
        mUrl = url + arrayList.get(0) + "&starttime=" + arrayList.get(1) + "&endtime=" + arrayList.get(2);
        Log.e(LOG_TAG, "the value of url is " + mUrl + " and the base url is " + url);

    }

    @Override
    public  List<Earthquake> loadInBackground() {
        if(mUrl == null){
            return null;
        }
        return QueryUtils.fetchDataFromServer(mUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
