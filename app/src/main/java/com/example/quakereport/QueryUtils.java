package com.example.quakereport;

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
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    public static final String LOG_TAG = EarthquakeActivity.class.getSimpleName();

    private QueryUtils() {
    }

    public static ArrayList<Earthquake> extractEarthquakes(String jsonResult) {

        if(TextUtils.isEmpty(jsonResult)){
            return null;
        }

        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(jsonResult);
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            for (int i = 0; i < earthquakeArray.length(); i++) {

                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                Double magnitude = properties.getDouble("mag");

                String location = properties.getString("place");

                long  time = properties.getLong("time");

                String url = properties.getString("url");

                Earthquake earthquake = new Earthquake(magnitude, location, time, url);

                earthquakes.add(earthquake);

            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "problem with parsing data", e);
        }

        return earthquakes;
    }

    public static List<Earthquake> fetchDataFromServer(String requestUrl){
        URL url = createUrl(requestUrl);

        String jsonResult = null;

        try{
            jsonResult = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        return extractEarthquakes(jsonResult);
    }

    private static URL createUrl(String urlString){
        URL url = null;
        try{
            url = new URL(urlString);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error while making the url", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{

        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG, "Error with response code " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem establishing the connection");
        }finally {
            if(urlConnection == null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
            return jsonResponse;
        }
    }

    private static String readFromStream(InputStream inputStream) throws IOException{

        StringBuilder output = new StringBuilder();

        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
