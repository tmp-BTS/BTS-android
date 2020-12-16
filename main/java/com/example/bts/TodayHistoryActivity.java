package com.example.bts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bts.Adapter.HistoryListAdapater;
import com.example.bts.BeaconUtils.HistoryManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TodayHistoryActivity extends AppCompatActivity {
    private TextView txt_date;
    private ListView listview;
    private HistoryListAdapater adapter;
    private String year, month, day;
    private ImageView btn_addHistory, btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_history);


        Intent getIntent = getIntent();
        year = getIntent.getStringExtra("year");
        month = getIntent.getStringExtra("month");
        day = getIntent.getStringExtra("day");



        txt_date = findViewById(R.id.txt_date);
        listview = findViewById(R.id.listview);
        listview.setDivider(null);
        btn_addHistory = findViewById(R.id.btn_addHistory);
        btn_back = findViewById(R.id.btn_back);
        btn_addHistory.setOnClickListener(new ButtonClickListener());
        btn_back.setOnClickListener(new ButtonClickListener());


        adapter = new HistoryListAdapater(getApplication());


        listview.setAdapter(adapter);


        txt_date.setText(year + "년 "+ month + "월" + day + "일");

        String stringJsonArray = HistoryManager.getJsonArray(getApplicationContext(), "history");
        if(!stringJsonArray.equals("")){
            try{
                JSONArray jsonArray = new JSONArray(stringJsonArray);
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = new JSONObject(jsonArray.get(i).toString());
                    String date = object.getString("date");
                    String name = object.getString("name");
                    String temperature = object.getString("temperature");
                    if(isToday(date))
                        adapter.addItem(convertDateFormmat(date),name,temperature);
                }
            }catch (JSONException e){

            }
        }
    }


    private boolean isToday(String date){
        String selectDate = year + "-" + month + "-" + day;
        if(selectDate.equals(date.split(" ")[0])){
            return true;
        }
        else return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = null;
        intent = new Intent(TodayHistoryActivity.this, CalendarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    
    private String convertDateFormmat(String date){
        return date.split(" ")[1].split(":")[0] + ":" + date.split(" ")[1].split(":")[1];
    }

    class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()){
                case R.id.btn_addHistory :
                    intent = new Intent(TodayHistoryActivity.this, SubmitActivity.class);
                    intent.putExtra("year",year);
                    intent.putExtra("month",month);
                    intent.putExtra("day",day);
                    break;
                case R.id.btn_back :
                    intent = new Intent(TodayHistoryActivity.this, CalendarActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    break;
            }
            startActivity(intent);
        }
    }
}