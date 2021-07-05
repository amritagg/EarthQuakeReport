package com.example.quakereport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Earthquake currentEarthquake = getItem(position);

        TextView magnitudeView = listItemView.findViewById(R.id.magnitude);
        assert currentEarthquake != null;
        String fomattedMagnitude = foramteMagnitude(currentEarthquake.getMagnitude());
        magnitudeView.setText(fomattedMagnitude);

        GradientDrawable magnitudeCircle = (GradientDrawable)magnitudeView.getBackground();
        int magnitudeColor = magnitudeColor(currentEarthquake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        TextView locationView = listItemView.findViewById(R.id.location);
        String formattedLocation = locationFormatter(currentEarthquake.getLocation());
        locationView.setText(formattedLocation);

        TextView dateView = listItemView.findViewById(R.id.date);
        String formattedDate = formateDate(currentEarthquake.getDate());
        dateView.setText(formattedDate);

        TextView timeView = listItemView.findViewById(R.id.time);
        String formattedTime = formateTime(currentEarthquake.getDate());
        timeView.setText(formattedTime);

        return listItemView;
    }

    private String foramteMagnitude(double magnitude){
        DecimalFormat magnitudeFormt = new DecimalFormat("0.0");
        return magnitudeFormt.format(magnitude);
    }

    private int magnitudeColor(double magnitude){
        int magnitudeColorId;
        int magnitudeFloor = (int)Math.floor(magnitude);
        switch (magnitudeFloor){
            case 0:
            case 1:
                magnitudeColorId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorId = R.color.magnitude9;
                break;
            default:
                magnitudeColorId = R.color.magnitude10plus;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorId);
    }

    private String locationFormatter(String location){

        String locationSeparator = " of ";
        String locationSeparator2 = " near the ";

        if(location.contains(locationSeparator)){
            int position = location.indexOf(locationSeparator);
            return location.substring(position + 4);

        }else if(location.contains(locationSeparator2)){
            int position = location.indexOf(locationSeparator);
            return location.substring(position + 10);

        }else{
            return location;
        }
    }

    private String formateDate(long time){

        Date date = new Date(time);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatted = new SimpleDateFormat("LLL dd, yyyy");
        formatted.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        return formatted.format(date);
    }

    private String formateTime(long time){
        Date date = new Date(time);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatted = new SimpleDateFormat("HH:mm:ss");
        formatted.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        return formatted.format(date);
    }

}
