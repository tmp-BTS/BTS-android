package com.example.bts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bts.BeaconUtils.HistoryManager;

import org.json.JSONException;
import org.json.JSONObject;

public class SubmitActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private TextView txt_date, btn_submit;
    private String year, month, day;
    private EditText edt_store_name;
    private ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);


        txt_date = findViewById(R.id.txt_date);
        timePicker = findViewById(R.id.timePicker);
        edt_store_name = findViewById(R.id.edt_store_name);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new ButtonClickListener());
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new ButtonClickListener2());

        Intent getIntent = getIntent();
        year = getIntent.getStringExtra("year");
        month = getIntent.getStringExtra("month");
        day = getIntent.getStringExtra("day");

        txt_date.setText(year + "년 " + month + "월 " + day +"일");
    }


    class ButtonClickListener2 implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(SubmitActivity.this, TodayHistoryActivity.class);
            intent.putExtra("year",year);
            intent.putExtra("month",month);
            intent.putExtra("day",day);
            startActivity(intent);
        }
    }

    class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {


            int hour, min;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                hour = timePicker.getHour();
                min = timePicker.getMinute();
            }
            else{
                hour = timePicker.getCurrentHour();
                min = timePicker.getCurrentMinute();
            }

            try{
                JSONObject object = new JSONObject();
                object.accumulate("name", edt_store_name.getText().toString());
                object.accumulate("date", "2020-" + month + "-" + day + " " +String.format("%02d",hour) + ":" + String.format("%02d",min) + ":" + "00");
                object.accumulate("temperature", "직접등록");
                Toast.makeText(SubmitActivity.this,"저장되었습니다.",Toast.LENGTH_SHORT).show();
                HistoryManager.pushJsonArray(getApplicationContext(), "history", object);

                Intent intent = new Intent(SubmitActivity.this, TodayHistoryActivity.class);
                intent.putExtra("year",year);
                intent.putExtra("month",month);
                intent.putExtra("day",day);
                startActivity(intent);

            }catch (JSONException e){

            }
        }
    }
}