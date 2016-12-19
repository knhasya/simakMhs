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

public class MainActivity extends AppCompatActivity {

    CircleImageView imageView2;
    TextView vNim, vNama, vBintang, tvHariTgl, vMatkul, vKodeMatkul, vDosen;
    String sNim, sNama, nim, sMatkul, sKodeMatkul, sDosen, dayName, sPertemuan;
    EditText edResume;
    Integer sBintang;
    Calendar calendar;

    ProgressDialog loadingMhs;
    public final static String EXTRA_MESSAGE = "com.galihpw.simak";

    private static String url_gMhs = Config.URL + "getMhs.php";
    private static String url_gMatkul = Config.URL + "getMatkul.php";
    private static String url_iResume = Config.URL + "insertResume.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        nim = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        //imageView2 = (CircleImageView) findViewById(R.id.imageView2);
        //imageView2.setImageResource(R.drawable.default_profile);
        vNim = (TextView) findViewById(R.id.tvNimAM);
        vNama = (TextView) findViewById(R.id.tvNamaAM);
        vBintang = (TextView) findViewById(R.id.tvBintangAM);
        vMatkul = (TextView) findViewById(R.id.tvNamaMatkulAM);
        vKodeMatkul = (TextView) findViewById(R.id.tvKodeMatkulAM);
        vDosen = (TextView) findViewById(R.id.tvNamaDosenAM);
        edResume = (EditText) findViewById(R.id.edResume);

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
        getDataMatkul();

        Button saveProf = (Button) findViewById(R.id.btnSubmit);
        saveProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("cek", "" + sNim + "," + sKodeMatkul + "," + sPertemuan + "," + edResume.getText().toString());
                insertResume();
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
                intent2.putExtra(EXTRA_MESSAGE,nim);
                startActivity(intent2);

                return true;
            case R.id.menu_catatan:
                Intent intent4 = new Intent( this , Catatan.class );
                startActivity(intent4);

                return true;
            case R.id.menu_forum:
                Intent intent3 = new Intent( this , ForumActivity.class );
                startActivity(intent3);

                finish();
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
            sBintang = data.getInt(Config.KEY_BINTANG);

        }catch (JSONException e){
            e.printStackTrace();
        }

        vNim.setText(sNim);
        vNama.setText(sNama);
        vBintang.setText(sBintang.toString());
    }

    private void showJSON2(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject data = result.getJSONObject(0);
            sMatkul = data.getString(Config.KEY_MATKUL);
            sKodeMatkul = data.getString(Config.KEY_KODEMATKUL);
            sDosen = data.getString(Config.KEY_NAMADOSEN);
            sPertemuan = data.getString(Config.KEY_PERTEMUAN);

        }catch (JSONException e){
            e.printStackTrace();
        }

        vMatkul.setText(sMatkul);
        vKodeMatkul.setText("(" + sKodeMatkul + ")");
        vDosen.setText(sDosen);
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
                params.put(Config.KEY_KODEMATKUL, sKodeMatkul);
                params.put(Config.KEY_RESUME, edResume.getText().toString());
                params.put(Config.KEY_PERTEMUAN, sPertemuan);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
