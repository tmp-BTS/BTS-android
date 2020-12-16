package com.example.bts;

import android.content.Intent;
import android.media.tv.TvContentRating;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bts.Adapter.HistoryListAdapater;
import com.example.bts.BeaconUtils.BeaconUtils;
import com.example.bts.BeaconUtils.HistoryManager;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity implements BeaconConsumer {
    CalendarView calendarView;
    TextView myDate;


    public static BeaconManager beaconManager;
    private static final int  MIN_DISTANCE_TO_BEACON_DETECT = 4;
    private static final int  MAX_DISTANCE_TO_BEACON_DETECT = 4;

    private ListView listview;
    private HistoryListAdapater adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        beaconManager = BeaconManager.getInstanceForApplication(CalendarActivity.this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.setForegroundScanPeriod(100);
        beaconManager.bind(CalendarActivity.this);

        calendarView =(CalendarView) findViewById(R.id.calendarView);
        myDate=(TextView) findViewById(R.id.myDate);
        listview = findViewById(R.id.listview);
        listview.setDivider(null);


        myDate.setText(getTiem());

        adapter = new HistoryListAdapater(getApplication());

        listview.setAdapter(adapter);

        String stringJsonArray = HistoryManager.getJsonArray(getApplicationContext(), "history");
        if(!stringJsonArray.equals("")){
            try{
                JSONArray jsonArray = new JSONArray(stringJsonArray);
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = new JSONObject(jsonArray.get(i).toString());
                    String date = object.getString("date");
                    String name = object.getString("name");
                    String temperature = object.getString("temperature");
                    if (changeCompareformat(getTiem()).equals(changeDataForm(date))){
                        adapter.addItem(convertDateFormmat(date),name,temperature);
                    }
                }
            }catch (JSONException e){

            }
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = i+"년 "+(i1+1)+"월 "+i2+"일";

                Intent intent = new Intent(CalendarActivity.this, TodayHistoryActivity.class);
                intent.putExtra("year",i+"");
                intent.putExtra("month",(i1+1)+"");
                intent.putExtra("day",i2+"");
                startActivity(intent);
            }
        });
    };


    private String convertDateFormmat(String date){
        return date.split(" ")[1].split(":")[0] + ":" + date.split(" ")[1].split(":")[1];
    }
    private String changeCompareformat(String date){
        String result;
        String tmp1 = date.split(" ")[0].split("년")[0];
        String tmp2 = date.split(" ")[1].split("월")[0];
        String tmp3 = date.split(" ")[2].split("일")[0];
        result = tmp1 + "-" + tmp2 + "-" + tmp3;
        return result;
    }
    private String changeDataForm(String date){
        return date.split(" ")[0];
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.removeAllMonitorNotifiers();


        beaconManager.setRangeNotifier(new RangeNotifier()
        {
            @Override
            public void didRangeBeaconsInRegion(Collection beacons, Region region)
            {
                if (beacons.size() > 0)
                {
                    Iterator<Beacon> iterator = beacons.iterator();
                    Beacon beacon = iterator.next();
                    if(beacon.getBluetoothName() != null){
                        String uuid = beacon.getId1().toUuid().toString();
                        String name = beacon.getBluetoothName();
                        Double distance = beacon.getDistance();
                        Log.i("beacon","MainActivity - name : " + name + " --  uuid : " + uuid + " -- distance : " + beacon.getDistance());

                        if(name.contains("BTS")){
                            if(BeaconUtils.getDetectBeaconUUID().equals("")){
                                if(distance < MIN_DISTANCE_TO_BEACON_DETECT){
                                    Intent popup = new Intent(getApplicationContext(), PopupActivity.class);
                                    popup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    popup.putExtra("name", name);
                                    startActivity(popup);
                                    BeaconUtils.setDetectBeacon(uuid, name, distance);
                                }
                            }
                            else{
                                if(distance > MAX_DISTANCE_TO_BEACON_DETECT) {
                                    BeaconUtils.clearDetectBeacon();
                                }
                            }
                        }
                    }
                }
            }
        });

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {}

            @Override
            public void didExitRegion(Region region) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                Log.i("log", "beacons are no longer detected. -> unBind Service & Init");
                                BeaconUtils.clearDetectBeacon();
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
//                Log.i("seo", "I have just switched from seeing/not seeing beacons: "+state);
            }

        });


        try {
            Identifier mIdentifier = null;

            beaconManager.startMonitoringBeaconsInRegion(new Region("beacon", mIdentifier, null, null));
            beaconManager.startRangingBeaconsInRegion(new Region("beacon", mIdentifier, null, null));
        }
        catch (RemoteException e) {}
    }


    private boolean isDuplication(Beacon beacon){
        ArrayList<Beacon> list = BeaconUtils.beaconScanList;
        for(int index = 0; index < list.size(); index++){
            String listAddress = list.get(index).getBluetoothAddress();
            String scanAddress = beacon.getBluetoothAddress();
            if(listAddress.equals(scanAddress))
                return true;
        }
        return false;
    }


    private String getTiem(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("YYYY년 MM월 dd일");
        String formatDate = sdfNow.format(date);
        return formatDate;
    }



}