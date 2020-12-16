package com.example.bts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bts.BeaconUtils.BeaconUtils;
import com.example.bts.BeaconUtils.HistoryManager;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MainActivity extends AppCompatActivity {
    private int MY_PERMISSIONS_REQUEST_LOCATION = 2000;
    private static final int _PERMISSIONS_REQUEST_CODE = 20002;
    private boolean isPermissionsGranted = false;
    private int count = 0;
    private Timer timer;
    private Intent intent ;

    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(MainActivity.this, LoginActivity.class);


        if(!checkPermissions()) {
            Log.i("log", "MainActivity - onCreate: permission denied, require all permission");
        }
        else{
            Log.i("log", "MainActivity - permission ok");


            timer = new Timer();
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    if(count > 1) {
                        timer.cancel();

                        startActivity(intent);
                    }
                    else
                        count++;
                }

            };
            timer.schedule(tt, 0, 1000);
        }

    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    _PERMISSIONS_REQUEST_CODE);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        isPermissionsGranted = false;
        switch(requestCode) {
            case _PERMISSIONS_REQUEST_CODE: {
                if(grantResults.length > 0) {
                    for (int i = 0; i<grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            isPermissionsGranted = false;
                            Toast.makeText(this, "사용권한 승인 필요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    isPermissionsGranted = true;
                }
            }
        }
        if(grantResults.length > 0){
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
    }


}
