package com.example.bts.BeaconUtils;

import org.altbeacon.beacon.Beacon;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BeaconUtils {
    public static DecimalFormat decimalFormat = new DecimalFormat("#.##");
    public static ArrayList<Beacon> beaconScanList = new ArrayList<Beacon>();
    public static String detectBeaconUUID = "";
    public static String detectBeaconName = "";
    public static Double detectBeaconDistance = 0.0;

    public BeaconUtils(){

    }

    public static Beacon getMinDistanceBeacon(ArrayList<Beacon> list){
        if (list == null) return null;
        double minDistance = 99999;
        Beacon minDistanceBeacon = null;
        for (int i = 0; i < list.size(); i++){
            double distance = Double.parseDouble(decimalFormat.format(list.get(i).getDistance()));
            if(distance < minDistance){
                minDistance = distance;
                minDistanceBeacon = list.get(i);
            }
        }
        return minDistanceBeacon;
    }

    public static void setDetectBeacon(String uuid, String name, Double distance){
        detectBeaconUUID = uuid;
        detectBeaconName = name;
        detectBeaconDistance = distance;
    }

    public static String getDetectBeaconUUID(){
        return detectBeaconUUID;
    }

    public static String getDetectBeaconName(){
        return detectBeaconName;
    }

    public static Double getDetectBeaconDistance(){
        return detectBeaconDistance;
    }

    public static void clearDetectBeacon(){
        detectBeaconDistance = 0.0;
        detectBeaconName = "";
        detectBeaconUUID = "";
    }
}
