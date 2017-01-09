package com.galihpw.simakmhs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Locale;
import java.util.Map;

import static com.galihpw.simakmhs.MainActivity.MAIN_MESSAGE;
import static com.galihpw.simakmhs.MainActivity.MAIN_MESSAGE1;
import static com.galihpw.simakmhs.MainActivity.MAIN_MESSAGE2;
import static com.galihpw.simakmhs.MainActivity.MAIN_MESSAGE3;

/**
 * Created by Nada on 1/1/2017.
 */

public class Materi extends AppCompatActivity {

    Calendar calendar;
    TextView tvHariTglMateri, vMatkulMateri, vKodeMatkulMateri, edtMateri, vDosenMateri;
    String dayName, sMatkulMateri, sKodeMatkulMateri, sDosenMateri;
    ListView listMateri;
    ProgressDialog loading;
    Dialog dia;
    ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.materi);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listMateri = (ListView) findViewById(R.id.listviewMateri);

        Intent intent = getIntent();
        sMatkulMateri = intent.getStringExtra(MainActivity.MAIN_MESSAGE1);
        sKodeMatkulMateri = intent.getStringExtra(MainActivity.MAIN_MESSAGE2);
        sDosenMateri = intent.getStringExtra(MainActivity.MAIN_MESSAGE3);

        vMatkulMateri = (TextView) findViewById(R.id.tvMatkulMateri);
        vKodeMatkulMateri = (TextView) findViewById(R.id.tvKodeMatkulMateri);
        vDosenMateri = (TextView) findViewById(R.id.tvNamaDosenMateri);

        tvHariTglMateri = (TextView) findViewById(R.id.tvHariTglMateri);
        calendar = Calendar.getInstance();
        SimpleDateFormat adf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        String currentDate = adf.format(calendar.getTime());

        SimpleDateFormat adf_ = new SimpleDateFormat("EEEE", Locale.US);
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
        tvHariTglMateri.setText("" + dayName + ", " + currentDate + "");
        vMatkulMateri.setText(sMatkulMateri);
        vKodeMatkulMateri.setText("(" + sKodeMatkulMateri + ")");
        vDosenMateri.setText("" + sDosenMateri);

        getMateri();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getMateri(){
        loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);

        String url_gMateri = Config.URL + "getMateri.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gMateri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSONForum(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Materi.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_KODEMATKUL, sKodeMatkulMateri);
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
            final String[] isiMateri = new String[result.length()];
            final int[] pertemuan = new int[result.length()];
            for(int i = 0;i < result.length();i++){
                JSONObject Data = result.getJSONObject(i);
                isiMateri[i] = Data.getString(Config.KEY_ISI_MATERI);
                pertemuan[i] = Integer.valueOf(Data.getString(Config.KEY_PERTEMUAN));

                items.add("Materi Pertemuan Ke-" + pertemuan[i]);
                listMateri.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        dialogMateri(pertemuan[position], isiMateri[position]);
                    }
                });
            }


        }catch (JSONException e){
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
        listMateri.setAdapter(adapter);
        listMateri.setClickable(true);
    }

    public void dialogMateri(int i, final String materi) {
        dia = new Dialog(Materi.this);
        dia.setContentView(R.layout.dialog_materi);
        dia.setTitle("Materi Pertemuan Ke-" + i);
        dia.setCancelable(true);

        edtMateri = (TextView) dia.findViewById(R.id.edtMateri);
        edtMateri.setText(materi);

        dia.show();
    }

}
