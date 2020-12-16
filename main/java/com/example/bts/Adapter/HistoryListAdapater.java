package com.example.bts.Adapter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bts.R;

import java.util.ArrayList;

import androidx.appcompat.widget.AppCompatImageView;


public class HistoryListAdapater extends BaseAdapter {
    public ArrayList<HistoryListItem> listViewItemList = new ArrayList<HistoryListItem>() ;
    public Application mApp;

    public HistoryListAdapater(Application app) {
        mApp = app;
    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_history, parent, false);
        }

        TextView time = (TextView) convertView.findViewById(R.id.txt_time) ;
        TextView name = (TextView) convertView.findViewById(R.id.txt_name) ;
        TextView temperature = (TextView) convertView.findViewById(R.id.txt_temperature) ;

        HistoryListItem listViewItem = listViewItemList.get(position);

        time.setText(listViewItem.getTime());
        name.setText(listViewItem.getName());
        temperature.setText(listViewItem.getTemperature());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public void addItem(String time, String name, String temperature) {
        HistoryListItem item = new HistoryListItem();
        item.setTime(time);
        item.setName(name);
        item.setTemperature(temperature);
        listViewItemList.add(item);
    }
}