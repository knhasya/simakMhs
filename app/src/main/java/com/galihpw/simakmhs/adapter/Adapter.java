package com.galihpw.simakmhs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.galihpw.simakmhs.R;
import com.galihpw.simakmhs.Topik;

/**
 * Created by ACER on 17/09/2016.
 */
public class Adapter extends BaseAdapter {

    private Context mContext;

    private Topik[] mTopik;

    public Adapter(Context Context, Topik[] Topik){
        mContext = Context;
        mTopik = Topik;
    }

    @Override
    public int getCount() {
        return mTopik.length;
    }

    @Override
    public Object getItem(int position) {
        return mTopik[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listtopik, null);
            viewHolder = new ViewHolder();
            viewHolder.judul = (TextView)convertView.findViewById(R.id.judul);
            viewHolder.deskripsi = (TextView)convertView.findViewById(R.id.deskripsi);
            viewHolder.penanya = (TextView)convertView.findViewById(R.id.penanya);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Topik mhs = mTopik[position];
        viewHolder.judul.setText(mhs.getJudul() + "");
        viewHolder.deskripsi.setText(mhs.getDeskripsi() + "");
        viewHolder.penanya.setText(mhs.getPenanya() + "");

        return convertView;
    }

    private static class ViewHolder{
        TextView judul;
        TextView deskripsi;
        TextView penanya;
    }
}
