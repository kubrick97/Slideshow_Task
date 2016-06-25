package com.example.sricharans.magic_lantern;

/**
 * Created by Srikanth S on 6/20/2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
public class adapter_music extends ArrayAdapter<music_info> {
    public adapter_music(Context context,  List<music_info> objects) {
        super(context, R.layout.layout_music, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.layout_music,parent,false);
        TextView t = (TextView) convertView.findViewById(R.id.titleTextView_ID);
        t.setText(getItem(position).title);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.layout_music,parent,false);
        TextView t = (TextView) convertView.findViewById(R.id.titleTextView_ID);
        t.setText(getItem(position).title);
        return convertView;
    }
}
