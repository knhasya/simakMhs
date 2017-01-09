package com.galihpw.simakmhs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
 * Created by Nada on 12/15/2016.
 */

public class Catatan extends AppCompatActivity {

    Calendar calendar;
    TextView tvHariTglCat, vMatkulCat, vKodeMatkulCat, vDosenCat;
    String sMatkulCat, sKodeMatkulCat, sDosenCat, dayName, sNim;
    ProgressDialog loadingMhsCat;

    public final static String CATATAN_MESSAGE1 = "com.galihpw.judulcat";
    public final static String CATATAN_MESSAGE2 = "com.galihpw.isicat";

    ListView listResume;
    //AdapterCat adapter;
    ArrayAdapter adapter;
    private ArrayList<String> items = new ArrayList<>();
    private Resume[] mResume;

    private static String url_gResume = Config.URL + "getResume.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catatan);

        Intent intent = getIntent();
        sNim = intent.getStringExtra(MAIN_MESSAGE);
        sMatkulCat = intent.getStringExtra(MAIN_MESSAGE1);
        sKodeMatkulCat = intent.getStringExtra(MAIN_MESSAGE2);
        sDosenCat = intent.getStringExtra(MAIN_MESSAGE3);

        listResume = (ListView) findViewById(R.id.listviewCat);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        vMatkulCat = (TextView) findViewById(R.id.tvMatkulCat);
        vKodeMatkulCat = (TextView) findViewById(R.id.tvKodeMatkulCat);
        vDosenCat = (TextView) findViewById(R.id.tvNamaDosenCat);

        tvHariTglCat = (TextView) findViewById(R.id.tvHariTglCat);
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
        tvHariTglCat.setText("" + dayName + ", " + currentDate + "");
        vMatkulCat.setText(sMatkulCat);
        vKodeMatkulCat.setText("(" + sKodeMatkulCat + ")");
        vDosenCat.setText(sDosenCat);

        getDataMatkulCat();
        //getDataResume();
    }

    /*public void ListResume(){
        for (i = 0; i < j; i++) {
            Topik data = new Topik("Judul Topik Diskusi");
            mResume[i] = data;

            listResume.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Resume dataMhs = mResume[position];
                    Intent intent = new Intent(Catatan.this, IsiTopik.class);
                    startActivity(intent);
                }
            });
        }
        //set adapter ke listview
        AdapterCat adapter = new Adapter(this, mResume);
        listResume.setAdapter(adapter);
    }*/

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

    private void getDataMatkulCat(){
        loadingMhsCat = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gResume, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingMhsCat.dismiss();
                showJSONCat(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingMhsCat.dismiss();
                Toast.makeText(Catatan.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_NIM, sNim);
                params.put(Config.KEY_KODEMATKUL, sKodeMatkulCat);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSONCat(String response){
        try{
            //JSONObject jsonObject = new JSONObject(response);
            JSONArray result = new JSONArray(response);
            mResume = new Resume[result.length()];
            for(int i = 0;i < result.length();i++){
                JSONObject Data = result.getJSONObject(i);
                Resume data = new Resume("" + Data.getString(Config.KEY_PERTEMUAN), "" + Data.getString(Config.KEY_RESUME));
                mResume[i] = data;

                items.add("Catatan pertemuan ke: " + mResume[i].getJudulCat());
            }


        }catch (JSONException e){
            e.printStackTrace();
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, items);

        listResume.setAdapter(adapter);
        listResume.setClickable(true);
        listResume.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(Catatan.this , IsiCatatan.class );
                intent.putExtra(CATATAN_MESSAGE1, "" + mResume[position].getJudulCat());
                intent.putExtra(CATATAN_MESSAGE2, "" + mResume[position].getIsiCat());
                startActivity(intent);
            }
        });
    }

    /*private void showJSONCat(String response){
        ListView listView = (ListView) findViewById(R.id.listviewCat);
        mResume = new Resume[response.length()];
        for(i=0;i<response.length();i++){
            try{
                JSONObject Data = response.getJSONObject(i);
                Resume data = new Resume ("" + Data.getString(Config.KEY_PERTEMUAN), "" + Data.getString(Config.KEY_RESUME));
                mResume[i] = data;

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        Resume dataResume = mResume[position];
                        Intent intent1 = new Intent(Catatan.this, IsiCatatan.class);
                        startActivity(intent1);
                    }
                });
            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        AdapterCat adapter = new AdapterCat(Catatan.this, mResume);
        listView.setAdapter(adapter);
    }*/

    /*private void getDataResume(){
        JsonArrayRequest jArr = new JsonArrayRequest(url_gMatkul, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ListView listView = (ListView) findViewById(R.id.listviewCat);
                mResume = new Resume[response.length()];
                for(i=0;i<response.length();i++){
                    try{
                        JSONObject Data = response.getJSONObject(i);
                        Resume data = new Resume ("" + Data.getString(Config.KEY_PERTEMUAN), "" + Data.getString(Config.KEY_RESUME));
                        mResume[i] = data;

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                                Resume dataResume = mResume[position];
                                Intent intent1 = new Intent(Catatan.this, IsiCatatan.class);
                                startActivity(intent1);
                            }
                        });
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

                AdapterCat adapter = new AdapterCat(Catatan.this, mResume);
                listView.setAdapter(adapter);
            }
        }, new Response.ErrorListener(){

        }
        )

    }*/

}
