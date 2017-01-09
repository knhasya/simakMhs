package com.galihpw.simakmhs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.galihpw.simakmhs.adapter.AdapterKom;
import com.galihpw.simakmhs.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.galihpw.simakmhs.Forum.FORUM_MESSAGE1;
import static com.galihpw.simakmhs.Forum.FORUM_MESSAGE2;
import static com.galihpw.simakmhs.Forum.FORUM_MESSAGE3;
import static com.galihpw.simakmhs.Forum.FORUM_MESSAGE4;
import static com.galihpw.simakmhs.Forum.FORUM_MESSAGE5;

/**
 * Created by Nada on 12/26/2016.
 */

public class IsiForum extends AppCompatActivity{

    String sJudulForum, sIsiForum, sNim, sIdTopik, sNama;
    TextView tvJudulForum, tvIsiForum, tvNamaForum, tvNamaKomentar, tvIsiKomentar;
    EditText edKomentar;
    Dialog dia;
    ProgressDialog loadingKomentar;

    ListView listKomentar;
    private Komentar[] mKomentar;

    private static String url_iKomentar = Config.URL + "insertKomentar.php";
    private static String url_gKomentar = Config.URL + "getKomentar.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listtopik);

        tvJudulForum = (TextView) findViewById(R.id.judulforum);
        tvIsiForum = (TextView) findViewById(R.id.isiforum);
        tvNamaForum = (TextView) findViewById(R.id.namaforum);
        tvNamaKomentar = (TextView) findViewById(R.id.namakom);
        tvIsiKomentar = (TextView) findViewById(R.id.isikom);

        Intent intent = getIntent();
        sJudulForum = intent.getStringExtra(FORUM_MESSAGE1);
        sIsiForum = intent.getStringExtra(FORUM_MESSAGE2);
        sNim = intent.getStringExtra(FORUM_MESSAGE3);
        sIdTopik = intent.getStringExtra(FORUM_MESSAGE4);
        sNama = intent.getStringExtra(FORUM_MESSAGE5);

        tvJudulForum.setText(sJudulForum);
        tvIsiForum.setText(sIsiForum);
        tvNamaForum.setText(sNama);

        getDataKomentar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.komentar_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tambah_komentar:
                KomentarDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void KomentarDialog() {
        dia = new Dialog(IsiForum.this);
        dia.setContentView(R.layout.dialog_komentar);
        dia.setTitle("Tambah Komentar");
        dia.setCancelable(true);
        dia.show();

        edKomentar = (EditText) dia.findViewById(R.id.etKomentar);

        Button but = (Button) dia.findViewById(R.id.btnbatkom);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.dismiss(); //keluar dialog
            }
        });

        Button bin = (Button) dia.findViewById(R.id.btnsimkom);
        bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertKomentar();
                //Log.v("tes judul", edJudulTopik.getText().toString());
                //Log.v("tes isi", edIsiTopik.getText().toString());
                dia.dismiss();
            }
        });
    }

    private void getDataKomentar(){
        loadingKomentar = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gKomentar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingKomentar.dismiss();
                showJSONForum(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingKomentar.dismiss();
                Toast.makeText(IsiForum.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_IDTOPIK, sIdTopik);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSONForum(String response){
        listKomentar = (ListView) findViewById(R.id.listKomentar);
        try{
            JSONArray result = new JSONArray(response);
            mKomentar = new Komentar[result.length()];
            for(int i = 0;i < result.length();i++){
                JSONObject Data = result.getJSONObject(i);
                Komentar data = new Komentar("" + Data.getString(Config.KEY_NAMA), "" + Data.getString(Config.KEY_ISIKOMENTAR));
                mKomentar[i] = data;
            }


        }catch (JSONException e){
            e.printStackTrace();
        }

        AdapterKom adapter = new AdapterKom(IsiForum.this, mKomentar);
        listKomentar.setAdapter(adapter);
    }

    private void insertKomentar(){
        loadingKomentar = ProgressDialog.show(this, "Please wait...", "Updating Data...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_iKomentar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt(Config.SUCCESS);
                    if (success==1) {
                        loadingKomentar.dismiss();
                        Toast.makeText(IsiForum.this, "Success", Toast.LENGTH_SHORT).show();
                        getDataKomentar();
                    } else {
                        loadingKomentar.dismiss();
                        Toast.makeText(IsiForum.this, "Data not update", Toast.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingKomentar.dismiss();
                Toast.makeText(IsiForum.this, "No connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_NIM, sNim);
                params.put(Config.KEY_ISIKOMENTAR, edKomentar.getText().toString());
                params.put(Config.KEY_IDTOPIK, sIdTopik);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
