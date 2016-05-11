package com.oic.test.realm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oic.test.realm.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FRAMGIA\pham.van.khac on 11/05/2016.
 */
public class RecordAdapter extends ArrayAdapter<Record> {

    List<Record> datas = new ArrayList<>();

    public RecordAdapter(Context context) {
        super(context, -1);
    }

    public void setData(List<Record> data){
        this.datas = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(getContext());
        textView.setText(datas.get(position).id + " : " +datas.get(position).value);
        return textView;
    }

    @Override
    public int getCount() {
        return datas.size();
    }
}
