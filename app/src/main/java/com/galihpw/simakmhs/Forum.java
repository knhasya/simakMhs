package com.galihpw.simakmhs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.galihpw.simakmhs.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.galihpw.simakmhs.MainActivity.MAIN_MESSAGE;
import static com.galihpw.simakmhs.MainActivity.MAIN_MESSAGE1;
import static com.galihpw.simakmhs.MainActivity.MAIN_MESSAGE2;
import static com.galihpw.simakmhs.MainActivity.MAIN_MESSAGE3;

/**
 * Created by Nada on 12/26/2016.
 */

public class Forum extends AppCompatActivity {

    Calendar calendar;
    TextView tvHariTglForum, vMatkulForum, vKodeMatkulForum, vDosenForum;
    String dayName, sNim, sMatkulForum, sKodeMatkulForum, sDosenForum, sNama;
    EditText edJudulTopik, edIsiTopik;
    ProgressDialog loadingForum, loadingTopik;

    public final static String FORUM_MESSAGE1 = "com.galihpw.judulforum";
    public final static String FORUM_MESSAGE2 = "com.galihpw.isiforum";
    public final static String FORUM_MESSAGE3 = "com.galihpw.nimforum";
    public final static String FORUM_MESSAGE4 = "com.galihpw.idtopik";
    public final static String FORUM_MESSAGE5 = "com.galihpw.nama";

    ListView listForum;
    ArrayAdapter adapter;
    private ArrayList<String> items = new ArrayList<>();
    private Topik[] mTopik;
    Dialog dia;

    private static String url_gTopik = Config.URL + "getTopik.php";
    private static String url_iTopik = Config.URL + "insertTopik.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        listForum = (ListView) findViewById(R.id.listviewForum);

        Intent intent = getIntent();
        sNim = intent.getStringExtra(MAIN_MESSAGE);
        sMatkulForum = intent.getStringExtra(MAIN_MESSAGE1);
        sKodeMatkulForum = intent.getStringExtra(MAIN_MESSAGE2);
        sDosenForum = intent.getStringExtra(MAIN_MESSAGE3);

        vMatkulForum = (TextView) findViewById(R.id.tvMatkulForum);
        vKodeMatkulForum = (TextView) findViewById(R.id.tvKodeMatkulForum);
        vDosenForum = (TextView) findViewById(R.id.tvNamaDosenForum);


        tvHariTglForum = (TextView) findViewById(R.id.tvHariTglForum);
        calendar = Calendar.getInstance();
        SimpleDateFormat adf = new SimpleDateFormat("dd-MMM-yyyy");
        String currentDate = adf.format(calendar.getTime());

        SimpleDateFormat adf_ = new SimpleDateFormat("EEEE");
        Date date = new Date();
        dayName = adf_.format(date);
        switch(dayName){
            case "Monday":
                dayName = "Senin";
                break;
            case "Tuesday":
                dayName = "Selasa";
                break;
            case "Wednesday":
                dayName = "Rabu";
                break;
            case "Thursday":
                dayName = "Kamis";
                break;
            case "Friday":
                dayName = "Jumat";
                break;
            case "Saturday":
                dayName = "Sabtu";
                break;
            case "Sunday":
                dayName = "Minggu";
                break;
        }
        tvHariTglForum.setText("" + dayName + ", " + currentDate + "");
        vMatkulForum.setText(sMatkulForum);
        vKodeMatkulForum.setText("(" + sKodeMatkulForum + ")");
        vDosenForum.setText(sDosenForum);

        getDataForum();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forum_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tambah_topik:
                TambahTopikDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getDataForum(){
        loadingForum = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gTopik, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingForum.dismiss();
                showJSONForum(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingForum.dismiss();
                Toast.makeText(Forum.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_KODEMATKUL, sKodeMatkulForum);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSONForum(String response){
        try{
            //JSONObject jsonObject = new JSONObject(response);
            JSONArray result = new JSONArray(response);
            mTopik = new Topik[result.length()];
            for(int i = 0;i < result.length();i++){
                JSONObject Data = result.getJSONObject(i);
                Topik data = new Topik("" + Data.getString(Config.KEY_JUDUL), "" + Data.getString(Config.KEY_ISITOPIK), "" + Data.getString(Config.KEY_IDTOPIK), "" + Data.getString(Config.KEY_NAMA));
                mTopik[i] = data;

                items.add(mTopik[i].getJudulForum());
            }


        }catch (JSONException e){
            e.printStackTrace();
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, items);

        listForum.setAdapter(adapter);
        listForum.setClickable(true);
        listForum.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(Forum.this , IsiForum.class );
                intent.putExtra(FORUM_MESSAGE1, "" + mTopik[position].getJudulForum());
                intent.putExtra(FORUM_MESSAGE2, "" + mTopik[position].getIsiForum());
                intent.putExtra(FORUM_MESSAGE4, "" + mTopik[position].getIdTopik());
                intent.putExtra(FORUM_MESSAGE3, sNim);
                intent.putExtra(FORUM_MESSAGE5, "" + mTopik[position].getNamaForum());
                startActivity(intent);
            }
        });
    }

    public void TambahTopikDialog() {
        dia = new Dialog(Forum.this);
        dia.setContentView(R.layout.dialog_forum);
        dia.setTitle("Tambah Topik");
        dia.setCancelable(true);
        dia.show();

        edJudulTopik = (EditText) dia.findViewById(R.id.etJudul);
        edIsiTopik = (EditText) dia.findViewById(R.id.etKomentar);

        Button but = (Button) dia.findViewById(R.id.btnbat);
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
                items.clear();
                insertTopik();
                //Log.v("tes judul", edJudulTopik.getText().toString());
                //Log.v("tes isi", edIsiTopik.getText().toString());
                dia.dismiss();
            }
        });
    }

    private void insertTopik(){
        loadingTopik = ProgressDialog.show(this, "Please wait...", "Updating Data...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_iTopik, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt(Config.SUCCESS);
                    if (success==1) {
                        loadingTopik.dismiss();
                        Toast.makeText(Forum.this, "Success", Toast.LENGTH_SHORT).show();
                        getDataForum();
                    } else {
                        loadingTopik.dismiss();
                        Toast.makeText(Forum.this, "Data not update", Toast.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingTopik.dismiss();
                Toast.makeText(Forum.this, "No connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_NIM, sNim);
                //Log.v("tes nim",sNim);
                params.put(Config.KEY_KODEMATKUL, sKodeMatkulForum);
                //Log.v("tes kodeMat",sKodeMatkulForum);
                params.put(Config.KEY_JUDUL, edJudulTopik.getText().toString());
                //Log.v("tes judul",edJudulTopik.getText().toString());
                params.put(Config.KEY_ISITOPIK, edIsiTopik.getText().toString());
                //Log.v("tes isi",edIsiTopik.getText().toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
