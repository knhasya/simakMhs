package com.galihpw.simakmhs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.galihpw.simakmhs.R;
import com.galihpw.simakmhs.Resume;
import com.galihpw.simakmhs.Topik;

/**
 * Created by Nada on 12/26/2016.
 */

public class AdapterForum extends BaseAdapter {

    private Context mContextForum;

    private Topik[] mTopik;

    public AdapterForum(Context ContextCat, Topik[] Topik){
        mContextForum = ContextCat;
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

        AdapterForum.ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContextForum).inflate(R.layout.listtopik, null);
            viewHolder = new AdapterForum.ViewHolder();
            viewHolder.judulforum = (TextView)convertView.findViewById(R.id.judulforum);
            viewHolder.isiforum = (TextView)convertView.findViewById(R.id.isiforum);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (AdapterForum.ViewHolder)convertView.getTag();
        }

        Topik mhsForum = mTopik[position];
        viewHolder.judulforum.setText(mhsForum.getJudulForum() + "");
        viewHolder.isiforum.setText(mhsForum.getIsiForum() + "");

        return convertView;
    }

    static class ViewHolder{
        TextView judulforum;
        TextView isiforum;
    }

}
