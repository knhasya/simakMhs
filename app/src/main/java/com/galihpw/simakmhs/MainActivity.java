package com.galihpw.simakmhs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.galihpw.simakmhs.LoginActivity.LOGIN_MESSAGE;
import static com.galihpw.simakmhs.LoginActivity.LOGIN_MESSAGE1;
import static com.galihpw.simakmhs.LoginActivity.LOGIN_MESSAGE2;
import static com.galihpw.simakmhs.LoginActivity.LOGIN_MESSAGE3;

public class MainActivity extends AppCompatActivity {

    CircleImageView imageView2;
    TextView vNim, vNama, vBintang, tvHariTgl, vMatkul, vKodeMatkul, vDosen;
    String sNim, sNama, nim, sMatkul, sKodeMatkul, sDosen, dayName, sPertemuan;
    EditText edResume;
    Integer sBintang;
    Calendar calendar;
    int status = 0;
    Button updateProf, saveProf;

    ProgressDialog loadingMhs;
    public final static String MAIN_MESSAGE = "com.galihpw.nim";
    public final static String MAIN_MESSAGE1 = "com.galihpw.matkul";
    public final static String MAIN_MESSAGE2 = "com.galihpw.kode_matkul";
    public final static String MAIN_MESSAGE3 = "com.galihpw.dosen";

    private static String url_gMhs = Config.URL + "getMhs.php";
    private static String url_gMatkul = Config.URL + "getMatkul.php";
    private static String url_gBintang = Config.URL + "getBintang.php";
    private static String url_iResume = Config.URL + "insertResume.php";
    private static String url_uResume = Config.URL + "updateResume.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        nim = intent.getStringExtra(LOGIN_MESSAGE);
        sMatkul = intent.getStringExtra(LOGIN_MESSAGE1);
        sKodeMatkul = intent.getStringExtra(LOGIN_MESSAGE2);
        sDosen = intent.getStringExtra(LOGIN_MESSAGE3);

        //imageView2 = (CircleImageView) findViewById(R.id.imageView2);
        //imageView2.setImageResource(R.drawable.default_profile);
        vNim = (TextView) findViewById(R.id.tvNimAM);
        vNama = (TextView) findViewById(R.id.tvNamaAM);
        vBintang = (TextView) findViewById(R.id.tvBintangAM);
        vMatkul = (TextView) findViewById(R.id.tvNamaMatkulAM);
        vKodeMatkul = (TextView) findViewById(R.id.tvKodeMatkulAM);
        vDosen = (TextView) findViewById(R.id.tvNamaDosenAM);
        edResume = (EditText) findViewById(R.id.edResume);

        vMatkul.setText(sMatkul);
        vKodeMatkul.setText("(" + sKodeMatkul + ")");
        vDosen.setText(sDosen);

        tvHariTgl = (TextView) findViewById(R.id.tvHariTgl);
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
        tvHariTgl.setText("" + dayName + ", " + currentDate + "");

        getData();

        updateProf = (Button) findViewById(R.id.btnPerbaharui);
        saveProf = (Button) findViewById(R.id.btnSubmit);
        saveProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertResume();
            }
        });

        updateProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateResume();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_profil:
                Intent intent2 = new Intent( this , ProfileMhs.class );
                intent2.putExtra(MAIN_MESSAGE,nim);
                startActivity(intent2);

                return true;
            case R.id.menu_catatan:
                Intent intent4 = new Intent( this , Catatan.class );
                intent4.putExtra(MAIN_MESSAGE1,sMatkul);
                intent4.putExtra(MAIN_MESSAGE2,sKodeMatkul);
                intent4.putExtra(MAIN_MESSAGE3,sDosen);
                intent4.putExtra(MAIN_MESSAGE, nim);
                startActivity(intent4);

                return true;
            case R.id.menu_forum:
                Intent intent3 = new Intent( this , Forum.class );
                intent3.putExtra(MAIN_MESSAGE1,sMatkul);
                intent3.putExtra(MAIN_MESSAGE2,sKodeMatkul);
                intent3.putExtra(MAIN_MESSAGE3,sDosen);
                intent3.putExtra(MAIN_MESSAGE, nim);
                startActivity(intent3);

                return true;
            case R.id.menu_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.NIM_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);

                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void getData(){
        loadingMhs = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gMhs, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingMhs.dismiss();
                showJSON(response);
                getDataMatkul();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingMhs.dismiss();
                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_NIM, nim);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getDataMatkul(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gMatkul, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingMhs.dismiss();
                showJSON2(response);
                getDataBintang();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingMhs.dismiss();
                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_HARI, dayName);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject data = result.getJSONObject(0);
            sNim = data.getString(Config.KEY_NIM);
            sNama = data.getString(Config.KEY_NAMA);

        }catch (JSONException e){
            e.printStackTrace();
        }

        vNim.setText(sNim);
        vNama.setText(sNama);
    }

    private void showJSON2(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject data = result.getJSONObject(0);
            sPertemuan = data.getString(Config.KEY_PERTEMUAN);

        }catch (JSONException e){
            e.printStackTrace();
        }

        /*vMatkul.setText(sMatkul);
        vKodeMatkul.setText("(" + sKodeMatkul + ")");
        vDosen.setText(sDosen);*/

    }

    private void insertResume(){
        loadingMhs = ProgressDialog.show(this, "Please wait...", "Updating Data...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_iResume, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt(Config.SUCCESS);
                    if (success==1) {
                        loadingMhs.dismiss();
                        updateProf.setVisibility(View.VISIBLE);
                        saveProf.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        status = 1;
                        loadingMhs.dismiss();
                        Toast.makeText(MainActivity.this, "Data not update", Toast.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                status = 1;
                loadingMhs.dismiss();
                Toast.makeText(MainActivity.this, "No connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_NIM, sNim);
                params.put(Config.KEY_KODEMATKUL, sKodeMatkul);
                params.put(Config.KEY_RESUME, edResume.getText().toString());
                params.put(Config.KEY_PERTEMUAN, sPertemuan);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateResume(){
        loadingMhs = ProgressDialog.show(this, "Please wait...", "Updating Data...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_uResume, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt(Config.SUCCESS);
                    if (success==1) {
                        loadingMhs.dismiss();
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        loadingMhs.dismiss();
                        Toast.makeText(MainActivity.this, "Data not update", Toast.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingMhs.dismiss();
                Toast.makeText(MainActivity.this, "No connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_NIM, sNim);
                params.put(Config.KEY_RESUME, edResume.getText().toString());
                params.put(Config.KEY_KODEMATKUL, sKodeMatkul);
                params.put(Config.KEY_PERTEMUAN, sPertemuan);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    int jbintang;

    private void getDataBintang(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gBintang, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                jbintang = 0;
                try{
                    //JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = new JSONArray(response);
                    for(int i = 0;i < result.length();i++){
                        JSONObject Data = result.getJSONObject(i);
                        String bintang = Data.getString(Config.KEY_BINTANG);
                        int ibintang = Integer.valueOf(bintang);
                        Log.v("tesssss",""+bintang);

                        jbintang = jbintang + ibintang;

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                vBintang.setText(""+jbintang);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_NIM, sNim);
                params.put(Config.KEY_KODEMATKUL, sKodeMatkul);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
