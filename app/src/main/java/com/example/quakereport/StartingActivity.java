package com.example.quakereport;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import static android.view.View.GONE;

public class StartingActivity extends AppCompatActivity {

    public StartingActivity(){}

    DatePicker datePicker;

    int start_end = 0;

    int start_date = 0;
    int start_month = 0;
    int start_year = 0;

    int end_date = 0;
    int end_month = 0;
    int end_year = 0;

    public String starting_date = "";
    public String ending_date = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starting_activity);

        datePicker = findViewById(R.id.calender);
        datePicker.setVisibility(GONE);

        final Button start_button = findViewById(R.id.start_button);
        final Button end_button = findViewById(R.id.end_button);
        final Button find_quake = findViewById(R.id.find_detail);

        final TextView notice = findViewById(R.id.notice);
        final TextView header = findViewById(R.id.header);
        notice.setVisibility(View.VISIBLE);
        header.setVisibility(View.VISIBLE);

        final Button last = findViewById(R.id.last_button);
        last.setVisibility(GONE);

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.VISIBLE);
                start_end = 1;
                last.setVisibility(View.VISIBLE);
            }
        });

        end_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.VISIBLE);
                start_end = 2;
                last.setVisibility(View.VISIBLE);
            }
        });

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(GONE);
                last.setVisibility(View.GONE);
            }
        });

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                if(start_end == 1){

                    start_date = datePicker.getDayOfMonth();
                    start_month = datePicker.getMonth() + 1;
                    start_year = datePicker.getYear();
                    start_button.setText(getCurrentDate());

                }else if(start_end == 2){

                    end_date = datePicker.getDayOfMonth();
                    end_month = datePicker.getMonth() + 1;
                    end_year = datePicker.getYear();
                    end_button.setText(getCurrentDate());
                }
            }
        });

        find_quake.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText magnitude_text = findViewById(R.id.enter_magnitude);
                Editable earthquake_magnitude = magnitude_text.getText();

                String magnitude = earthquake_magnitude.toString().trim();

                starting_date = start_year + "-" + start_month + "-" + start_date;
                ending_date = end_year + "-" + end_month + "-" + end_date;

                final ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(magnitude);
                arrayList.add(starting_date);
                arrayList.add(ending_date);

                boolean checkDate;
                boolean mag = true;

                if(start_year < end_year) {
                    checkDate = true;
                }else if(start_year == end_year){
                    if(start_month < end_month){
                        checkDate = true;
                    }else if(start_month == end_month){
                        checkDate = start_date < end_date;
                    }else {
                        checkDate = false;
                    }
                }else {
                    checkDate = false;
                }

                if(magnitude.isEmpty()){
                    mag = false;
                }

                if(checkDate && mag){

                    Intent intent = new Intent(StartingActivity.this, EarthquakeActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("list_of_array", arrayList);

                    intent.putExtras(bundle);

                    startActivity(intent);

                    start_end = 0;
                }else if(!checkDate && mag){
                    notice.setText("Date should be proper");
                    header.setText("Start Date should be less than End Date");
                    checkDate = true;
                }
                else if(!mag && checkDate){
                    notice.setText("Magnitude Should be entered");
                    mag = true;
                    header.setVisibility(GONE);
                }else{
                    notice.setText("Date should be proper");
                    header.setText("Magnitude should be entered");
                }
            }
        });
    }

    public String getCurrentDate(){
        StringBuilder builder = new StringBuilder();
        builder.append(datePicker.getDayOfMonth()+"/");
        builder.append((datePicker.getMonth() + 1)+"/");//month is 0 based
        builder.append(datePicker.getYear());
        return builder.toString();
    }

}
