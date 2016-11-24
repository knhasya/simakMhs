package com.galihpw.simakmhs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by GalihPW on 23/09/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    //Defining views
    private EditText editTextNip;
    private Button buttonLogin;
    public ProgressDialog progressDialog;

    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;
    String nim, nimm;

    public final static String EXTRA_MESSAGE = "com.galihpw.simak";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Initializing views
        editTextNip = (EditText) findViewById(R.id.un);

        buttonLogin = (Button) findViewById(R.id.login_btn);

        //Adding click listener
        buttonLogin.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        nimm = sharedPreferences.getString(Config.NIM_SHARED_PREF, nim);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(EXTRA_MESSAGE,nimm);
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

                            //Saving values to editor
                            editor.commit();

                            progressDialog.dismiss();

                            //Starting profile activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(EXTRA_MESSAGE,nim);
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

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
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

        //Calling the login function
        login();

       /* new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {


                        progressDialog.dismiss();
                    }
                }, 3000);*/
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
}
