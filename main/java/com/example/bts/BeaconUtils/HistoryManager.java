package com.example.bts.BeaconUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class HistoryManager {
    static SharedPreferences pref;

    public static void pushJsonArray(Context context, String key, JSONObject jsonObject) {
        try{
            String stringArray = getJsonArray(context,key);
            JSONArray jsonArray;
            if(getJsonArray(context,key).equals("")){
                jsonArray = new JSONArray();
            }
            else{
                jsonArray = new JSONArray(stringArray);
            }
            jsonArray.put(jsonObject);

            pref = context.getSharedPreferences(key,context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(key, jsonArray.toString());
            Log.i("log","jsonArray.toString(): " +jsonArray.toString());
            editor.commit();
        }catch (Exception e){
            Log.i("log","json parse error: " + e);
        }
    }

    public static String getJsonArray(Context context, String key) {
        pref = context.getSharedPreferences(key, context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static void clearHistory(Context context, String key){
        pref = context.getSharedPreferences(key,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

}
