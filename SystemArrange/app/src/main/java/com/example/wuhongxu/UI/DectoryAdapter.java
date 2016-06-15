package com.example.wuhongxu.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wuhongxu.ValueEnum.Value;
import com.example.wuhongxu.systemarrange.R;

import java.util.List;
import java.util.Map;

/**
 * Created by wuhongxu on 2015/10/11.
 */
public class DectoryAdapter extends BaseAdapter {
    private List<Map<String, Object>> list;
    private int layoutId;
    private Context context;
    private String[] from;
    private int[] to;
    private LayoutInflater mInflater;

    public DectoryAdapter(Context context, List<Map<String, Object>> list, int layoutId, String[] from, int[] to) {
        this.list = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
        this.from = from;
        this.to = to;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(layoutId, null);
        }
        View space = convertView.findViewById(R.id.space_view);
        space.setLayoutParams(new LinearLayout.LayoutParams((Integer.valueOf(list.get(position).get(from[2]).toString())) * Value.ONE_SPACE, 0));
        ImageView iv = (ImageView) convertView.findViewById(to[0]);
        iv.setImageResource(R.drawable.close);
        TextView tv = (TextView) convertView.findViewById(to[1]);
        tv.setText(list.get(position).get(from[0]).toString());
        return convertView;
    }
}
