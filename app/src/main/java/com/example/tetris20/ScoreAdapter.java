package com.example.tetris20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class ScoreAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public ArrayList<Record> listRecord = new ArrayList<>();

    public ScoreAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return listRecord.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        public TextView textViewName;
        public TextView textViewScore;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.layout_list_item, null);
            holder = new ViewHolder();
            holder.textViewName = convertView.findViewById(R.id.textViewName);
            holder.textViewScore = convertView.findViewById(R.id.textViewScore);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //values
        holder.textViewName.setText(listRecord.get(position).name);
        holder.textViewScore.setText(listRecord.get(position).score);
        return convertView;
    }
}
