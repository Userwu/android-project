package com.example.wuhongxu.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by wuhongxu on 2015/10/9.
 */
public class SearchResultListAdapter extends BaseAdapter {
    int time;
    private Context context;
    private List<Map<String, Object>> list;
    private int layoutIdCheck, layoutIdUncheck;
    private String[] from;
    private int[] to;
    private LayoutInflater minflater;
    private boolean checkState;

    public SearchResultListAdapter(Context context, List<Map<String, Object>> list, int layoutId, String[] from, int[] to) {
        this.context = context;
        minflater = LayoutInflater.from(context);
        this.list = list;
        this.layoutIdCheck = layoutId;
        this.from = from;
        this.to = to;
        this.layoutIdUncheck = layoutIdUncheck;
        init();
    }

    public void init() {
        time = 0;
        checkState = false;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = minflater.inflate(layoutIdCheck, null);
        }
        if (!Boolean.valueOf(list.get(position).get(from[0]).toString())) {
            ImageView cbIv = (ImageView) convertView.findViewById(to[0]);
            cbIv.setVisibility(View.GONE);
        } else {
            ImageView cbIv = (ImageView) convertView.findViewById(to[0]);
            cbIv.setVisibility(View.VISIBLE);
        }

        ImageView iv = (ImageView) convertView.findViewById(to[1]);
        iv.setImageResource((Integer) list.get(position).get(from[1]));
        TextView tv = (TextView) convertView.findViewById(to[2]);
        tv.setText(list.get(position).get(from[2]).toString());
        TextView sizetv = (TextView) convertView.findViewById(to[3]);
        sizetv.setText(list.get(position).get(from[3]).toString());

        return convertView;
    }
}
