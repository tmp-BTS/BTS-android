package com.example.bts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bts.BeaconUtils.HistoryManager;
import com.example.bts.Bluno.BlunoLibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PopupActivity extends BlunoLibrary {
    private TextView storeName, time, temperature, txt_message;
    private Button btn_ok, btn_cancel, btn_exit, btn_finish;
    private LinearLayout layout_prompt, layout_alert,layout_finish_alert;
    private String getStoreName;
    private boolean isDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        Intent intent = getIntent();
        getStoreName = "올리브영 인하대점";
        onCreateProcess();
        serialBegin(115200);

        storeName = findViewById(R.id.txt_store_name);
        time = findViewById(R.id.txt_time);
        temperature = findViewById(R.id.txt_temperature);
        txt_message = findViewById(R.id.txt_message);
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_exit = findViewById(R.id.btn_exit);
        btn_finish = findViewById(R.id.btn_finish);
        layout_finish_alert = findViewById(R.id.layout_finish_alert);
        layout_prompt  = findViewById(R.id.layout_prompt);
        layout_alert  = findViewById(R.id.layout_alert);
        isDone = false;
        storeName.setText(getStoreName);
        time.setText(getTiem());

        btn_ok.setOnClickListener(new ButtonClickListener());
        btn_cancel.setOnClickListener(new ButtonClickListener());
        btn_exit.setOnClickListener(new ButtonClickListener());
        btn_finish.setOnClickListener(new ButtonClickListener());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){


                case R.id.btn_ok :
                    txt_message.setText("BLE 기기를 찾고있습니다.");
                    buttonScanOnClickProcess();
                    txt_message.setText("온도를 측정중입니다.");
                    break;


                case R.id.btn_cancel :
                    finish();
                    break;


                case R.id.btn_exit :
                    try{
                        JSONObject object = new JSONObject();
                        object.accumulate("name", getStoreName);
                        object.accumulate("date", getTiem2());
                        object.accumulate("temperature", temperature.getText().toString());
                        if(Double.parseDouble(temperature.getText().toString().split("도")[0]) < 37.5) {
                            Toast.makeText(PopupActivity.this,"저장되었습니다.",Toast.LENGTH_SHORT).show();
                            HistoryManager.pushJsonArray(getApplicationContext(), "history", object);
                            txt_message.setText("입장 가능합니다.");

                        }
                        else{

                            txt_message.setText("입장 불가능합니다.");

                        }
                        layout_finish_alert.setVisibility(View.VISIBLE);
                        layout_alert.setVisibility(View.INVISIBLE);
                    }catch (JSONException e){

                    }
                    isDone = true;

                    break;
                case R.id.btn_finish:
                    finish();
                    break;
            }
        }
    }

    protected void onResume(){
        super.onResume();
        System.out.println("BlUNOActivity onResume");
        onResumeProcess();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultProcess(requestCode, resultCode, data);
        Log.i("log","onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();
    }

    protected void onStop() {
        super.onStop();
        onStopProcess();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyProcess();
    }

    @Override
    public void onConectionStateChange(connectionStateEnum theconnectionStateEnum) {

    }

    @Override
    public void onSerialReceived(String theString) {
        if(isDone) return;
        txt_message.setText("온도가 측정되었습니다.");
        layout_prompt.setVisibility(View.GONE);
        layout_alert.setVisibility(View.VISIBLE);
        temperature.setText(theString + "도");
    }


    private String getTiem(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH시 mm분 ss초");
        String formatDate = sdfNow.format(date);
        return formatDate;
    }

    private String getTiem2(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String formatDate = sdfNow.format(date);
        return formatDate;
    }
}