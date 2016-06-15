package com.example.wuhongxu.UI;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;

import android.app.ListFragment;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ArrayAdapter;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.TextView;

import com.example.wuhongxu.Server.Fserver;
import com.example.wuhongxu.systemarrange.R;


public class SimpleListFragment extends ListFragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slidding_menu_list, null);

    }



    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        SampleAdapter adapter = new SampleAdapter(getActivity());
        adapter.add(new SampleItem("辅助悬浮窗"));

        setListAdapter(adapter);

    }



    private class SampleItem {

        public String tag;

        public int iconRes;
        public SampleItem(String tag){
            this.tag = tag;
        }

        public SampleItem(String tag, int iconRes) {

            this.tag = tag;

            this.iconRes = iconRes;
        }

    }



    public class SampleAdapter extends ArrayAdapter<SampleItem> {



        public SampleAdapter(Context context) {

            super(context, 0);

        }



        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.slidding_row, null);

            }
            final CheckBox cb = (CheckBox) convertView.findViewById(R.id.window_o_c);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cb.setChecked(!cb.isChecked());
                }
            });
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                        System.out.println("2:ok:serviceclick!!!!!");
                        Intent intent = new Intent(getActivity(), Fserver.class);
                        //启动FxService
                        getActivity().startService(intent);
                    }else{
                        System.out.println("2:no:serviceclick!!!!!");
                        Intent intent = new Intent(getActivity(), Fserver.class);
                        //终止FxService
                        getActivity().stopService(intent);
                    }
                }
            });

            TextView title = (TextView) convertView.findViewById(R.id.row_title);

            title.setText(getItem(position).tag);



            return convertView;

        }



    }

}