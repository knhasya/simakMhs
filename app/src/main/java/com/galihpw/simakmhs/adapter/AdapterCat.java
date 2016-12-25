package com.galihpw.simakmhs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.galihpw.simakmhs.R;
import com.galihpw.simakmhs.Resume;

/**
 * Created by Nada on 12/21/2016.
 */

public class AdapterCat extends BaseAdapter {

    private Context mContextCat;

    private Resume[] mResume;

    public AdapterCat(Context ContextCat, Resume[] Resume){
        mContextCat = ContextCat;
        mResume = Resume;
    }

    @Override
    public int getCount() {
        return mResume.length;
    }

    @Override
    public Object getItem(int position) {
        return mResume[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContextCat).inflate(R.layout.listresume, null);
            viewHolder = new AdapterCat.ViewHolder();
            viewHolder.judulcat = (TextView)convertView.findViewById(R.id.judulcat);
            viewHolder.isicat = (TextView)convertView.findViewById(R.id.isiCat);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (AdapterCat.ViewHolder)convertView.getTag();
        }

        Resume mhsCat = mResume[position];
        viewHolder.judulcat.setText(mhsCat.getJudulCat() + "");
        viewHolder.isicat.setText(mhsCat.getIsiCat() + "");

        return convertView;
    }

    static class ViewHolder{
        TextView judulcat;
        TextView isicat;
    }
}
