package com.example.quakereport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private EarthquakeAdapter mAdapter;

    private int LoaderManger_ID = 1;

    private final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=";

    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView earthquakelistView = findViewById(R.id.list_item);

        emptyView = findViewById(R.id.empty_view);
        earthquakelistView.setEmptyView(emptyView);

        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        earthquakelistView.setAdapter(mAdapter);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = LoaderManager.getInstance(this);
            loaderManager.initLoader(LoaderManger_ID, null, this);
        }else{
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);

            emptyView.setText(R.string.no_internet);
        }

        earthquakelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Earthquake currentEarthquake = mAdapter.getItem(position);

                assert currentEarthquake != null;
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                Intent website = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                startActivity(website);
            }
        });

    }

    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle bundle) {

        Bundle fromStartActivity = getIntent().getExtras();
        assert fromStartActivity != null;
        ArrayList<String> arrayList = fromStartActivity.getStringArrayList("list_of_array");
        assert arrayList != null;
        return new EarthquakeLoader(this, USGS_REQUEST_URL, arrayList);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> data) {

        View loading = findViewById(R.id.loading_spinner);
        loading.setVisibility(View.GONE);

        emptyView.setText(R.string.no_earthquake);

        mAdapter.clear();

        if(data != null && !data.isEmpty()){
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquake>> loader) {
        mAdapter.clear();
    }

}
