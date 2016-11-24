package com.galihpw.simakmhs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.galihpw.simakmhs.adapter.Adapter;

public class ForumActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipe;
    ListView listTopik;
    Adapter adapter;
    private Topik[] mTopik;
    public final static String EXTRA_MESSAGE = "com.galih.simak";
    Dialog dia_forum;
    int i = 0, j = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cobain);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           //itemList.clear();
                           adapter.notifyDataSetChanged();
                           //callVolley();
                       }
                   }
        );

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForumDialog();
            }
        });*/

        listTopik = (ListView) findViewById(R.id.listview1);

        //ListTopik();
    }

    public void ListTopik(){
        for (i = 0; i < j; i++) {
            Topik data = new Topik("Judul Topik Diskusi", "Ini deskripsi bla bla bla", "nada");
            mTopik[i] = data;

            listTopik.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Topik dataMhs = mTopik[position];
                    Intent intent = new Intent(ForumActivity.this, IsiTopik.class);
                    intent.putExtra(EXTRA_MESSAGE, position);
                    startActivity(intent);
                }
            });
        }
        //set adapter ke listview
        Adapter adapter = new Adapter(this, mTopik);
        listTopik.setAdapter(adapter);
    }

    public void ForumDialog(){
        dia_forum = new Dialog(ForumActivity.this);
        dia_forum.setContentView(R.layout.dialog_forum);

        dia_forum.setTitle("Tambah Topik");
        dia_forum.setCancelable(false);
        dia_forum.show();

        //memanggil button but yang ada pada dialog
        Button sim = (Button) dia_forum.findViewById(R.id.btnsim);
        sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia_forum.dismiss(); //keluar dialog
                //klikButtonSimpan();
            }
        });

        Button bat = (Button) dia_forum.findViewById(R.id.btnbat);
        bat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia_forum.dismiss();
            }
        });

    }

    /*public void klikButtonSimpan() {
        EditText etJudul = (EditText) findViewById(R.id.etJudul);
        EditText etDesk = (EditText) findViewById(R.id.etDesk);
        String judul = etJudul.getText().toString();
        String desk = etDesk.getText().toString();

        mTopik = new Topik[j+1];
        Topik data = new Topik("" + judul, "" + desk, "nada");
        mTopik[j] = data;
        //ListTopik();
        //set adapter ke listview
        Adapter adapter = new Adapter(ForumActivity.this, mTopik);
        listTopik.setAdapter(adapter);
    }*/

    /*private void callVolley(){
        mTopik.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Data item = new Data();

                        item.setId(obj.getString(TAG_ID));
                        item.setNama(obj.getString(TAG_NAMA));
                        item.setAlamat(obj.getString(TAG_ALAMAT));

                        // menambah item ke array
                        itemList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }*/

    @Override
    public void onRefresh() {

    }
}
