package com.galihpw.simakmhs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Locale;
import java.util.Map;

/**
 * Created by GalihPW on 23/09/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    //Defining views
    private EditText editTextNip;
    public ProgressDialog progressDialog;

    String nim, nimm, nimmm, waktuMulai, waktuSelesai, dayName, sKodeMatkul, komat, mat, dos, sMatkul, sDosen;
    Calendar calendar;

    public final static String LOGIN_MESSAGE = "com.galihpw.simak";
    public final static String LOGIN_MESSAGE1 = "com.galihpw.matkul";
    public final static String LOGIN_MESSAGE2 = "com.galihpw.kodematkul";
    public final static String LOGIN_MESSAGE3 = "com.galihpw.dosen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Initializing views
        editTextNip = (EditText) findViewById(R.id.un);

        Button buttonLogin = (Button) findViewById(R.id.login_btn);

        //Adding click listener
        if (buttonLogin != null) {
            buttonLogin.setOnClickListener(this);
        }

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

        getJadwal();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        boolean loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        nimm = sharedPreferences.getString(Config.NIM_SHARED_PREF, nim);
        mat = sharedPreferences.getString(Config.MATKUL_SHARED_PREF, sMatkul);
        komat = sharedPreferences.getString(Config.KMATKUL_SHARED_PREF, sKodeMatkul);
        dos = sharedPreferences.getString(Config.DOSEN_SHARED_PREF, sDosen);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(LOGIN_MESSAGE,nimm);
            intent.putExtra(LOGIN_MESSAGE1,mat);
            intent.putExtra(LOGIN_MESSAGE2,komat);
            intent.putExtra(LOGIN_MESSAGE3,dos);
            startActivity(intent);

            finish();
        }
    }

    private void login(){
        //Getting values from edit texts
        nim = editTextNip.getText().toString().trim();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if(response.equalsIgnoreCase(Config.SUCCESS)){
                            //Creating a shared preference
                            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.NIM_SHARED_PREF, nim);
                            editor.putString(Config.MATKUL_SHARED_PREF, sMatkul);
                            editor.putString(Config.KMATKUL_SHARED_PREF, sKodeMatkul);
                            editor.putString(Config.DOSEN_SHARED_PREF, sDosen);

                            //Saving values to editor
                            editor.apply();

                            progressDialog.dismiss();

                            //Starting profile activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(LOGIN_MESSAGE,nim);
                            intent.putExtra(LOGIN_MESSAGE1,sMatkul);
                            intent.putExtra(LOGIN_MESSAGE2,sKodeMatkul);
                            intent.putExtra(LOGIN_MESSAGE3,sDosen);
                            startActivity(intent);

                            finish();
                        }else{
                            progressDialog.dismiss();

                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        progressDialog.dismiss();

                        Toast.makeText(LoginActivity.this, "No Connection", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_NIM, nim);
                params.put(Config.KEY_NAMADOSEN, sDosen);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean validate() {
        boolean valid = true;

        String un = editTextNip.getText().toString();

        if (un.isEmpty()) {
            editTextNip.setError("Masukkan NIM");
            valid = false;
        } else {
            editTextNip.setError(null);
        }

        return valid;
    }

    //Compare Time
    public static final String inputFormat = "HH:mm";

    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);

    private Date parseDate(String date){
        try{
            return inputParser.parse(date);
        }catch(java.text.ParseException e){
            return new Date(0);
        }
    }

    private void getJadwal(){
        String url_gJadwal = Config.URL + "getJadwalMhs.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gJadwal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON2(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void showJSON2(String response){
        Calendar now = Calendar.getInstance();

        int hour = now.get(Calendar.HOUR);
        int minute =  now.get(Calendar.MINUTE);

        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            Date date = parseDate(hour + ":" + minute);

            for(int i=0; i<result.length(); i++){
                JSONObject data = result.getJSONObject(i);
                waktuMulai = data.getString(Config.KEY_WAKTUMULAI);
                waktuSelesai = data.getString(Config.KEY_WAKTUSELESAI);

                Date dateCompareOne = parseDate(waktuMulai);
                Date dateCompareTwo = parseDate(waktuSelesai);

                if(dateCompareOne.before(date) && dateCompareTwo.after(date)){
                    Log.v("hai", "masuuuuuuuuuuuukk");

                    sKodeMatkul = data.getString(Config.KEY_KODEMATKUL);
                    sMatkul = data.getString(Config.KEY_MATKUL);
                    sDosen = data.getString(Config.KEY_NAMADOSEN);
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void getKontrak(){
        String url_gKontrak = Config.URL + "getKontrak.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_gKontrak, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    int status = 0;
                    JSONArray result = new JSONArray(response);
                    for(int i = 0;i < result.length();i++){
                        JSONObject Data = result.getJSONObject(i);
                        String nimkontrak = Data.getString(Config.KEY_NIM);
                        if(nimmm.equals(nimkontrak)){
                            status=1;
                        }
                    }
                    if(status == 1){
                        login();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Anda tidak terdaftar di mata kuliah ini", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                //Log.v("tes2",""+nimmm);
                //vBintang.setText(""+jbintang);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_KODEMATKUL, sKodeMatkul);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            return;
        }


        //Loading
        progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        nimmm = editTextNip.getText().toString().trim();
        getKontrak();
       /* new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {


                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }

}
