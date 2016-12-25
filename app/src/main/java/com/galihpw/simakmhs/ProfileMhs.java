package com.galihpw.simakmhs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.galihpw.simakmhs.MainActivity.MAIN_MESSAGE;

public class ProfileMhs extends AppCompatActivity {

    CircleImageView imgProfile;
    TextView vNim, vNama, vBintang, vKelas, vAlamat, vKontak, vEmail, vFB, vTW;
    String sNim, sNama, sKelas, sAlamat, sKontak, sEmail, sFB, sTW, nim;
    Integer sBintang;
    EditText edAlamat, edKontak, edEmail, edFB, edTW;

    ProgressDialog loadingMhs;

    private static String url_gMhs = Config.URL + "getMhs.php";
    private static String url_uMhs = Config.URL + "updateMhs.php";

    static final int REQUEST_CAMERA = 0;
    static final int SELECT_FILE = 1;
    private String userChoosenTask;

    String[] value = new String[]{
            "Choose from Gallery",
            "Take Photo",
            "Remove Profile Photo"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);

        Intent intent = getIntent();
        nim = intent.getStringExtra(MAIN_MESSAGE);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        vNim = (TextView) findViewById(R.id.tvNim);
        vNama = (TextView) findViewById(R.id.tvNama);
        vKelas = (TextView) findViewById(R.id.tvKelas);
        vBintang = (TextView) findViewById(R.id.tvBintang);
        vAlamat = (TextView) findViewById(R.id.view_alamat);
        vKontak = (TextView) findViewById(R.id.view_kontak);
        vEmail = (TextView) findViewById(R.id.view_email);
        vFB = (TextView) findViewById(R.id.view_facebook);
        vTW = (TextView) findViewById(R.id.view_twitter);

        edAlamat = (EditText) findViewById(R.id.edit_alamat);
        edKontak = (EditText) findViewById(R.id.edit_kontak);
        edEmail = (EditText) findViewById(R.id.edit_email);
        edFB = (EditText) findViewById(R.id.edit_facebook);
        edTW = (EditText) findViewById(R.id.edit_twitter);

        getData();

        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        imgProfile.setImageResource(R.drawable.foto);

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPhoto();
            }
        });

        Button editProf = (Button) findViewById(R.id.editProf);
        editProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProf();
            }
        });

        Button saveProf = (Button) findViewById(R.id.simpProf);
        saveProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProf();
                updateData();
            }
        });

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    public void dialogPhoto(){
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(ProfileMhs.this);

        alertdialogbuilder.setTitle("Profile Picture");

        alertdialogbuilder.setItems(value, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(ProfileMhs.this);

                if(value[item].equals("Choose from Gallery")){
                    Toast.makeText(ProfileMhs.this, "Select Photo", Toast.LENGTH_SHORT).show();
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();
                }else if(value[item].equals("Take Photo")){
                    Toast.makeText(ProfileMhs.this, "Take Photo", Toast.LENGTH_SHORT).show();
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();
                }else if(value[item].equals("Remove Profile Photo")){
                    Toast.makeText(ProfileMhs.this, "Photo Removed", Toast.LENGTH_SHORT).show();
                    imgProfile.setImageResource(R.drawable.default_profile);
                }

            }
        });

        AlertDialog dialog = alertdialogbuilder.create();

        dialog.show();
    }

    public void editProf(){
        edAlamat.setText(vAlamat.getText().toString());
        edKontak.setText(vKontak.getText().toString());
        edEmail.setText(vEmail.getText().toString());
        edFB.setText(vFB.getText().toString());
        edTW.setText(vTW.getText().toString());

        switchProf();
    }

    public void saveProf(){
        vAlamat.setText(edAlamat.getText().toString());
        vKontak.setText(edKontak.getText().toString());
        vEmail.setText(edEmail.getText().toString());
        vFB.setText(edFB.getText().toString());
        vTW.setText(edTW.getText().toString());

        updateData();
    }

    public void switchProf(){
        ViewSwitcher viewSwitcher0 =   (ViewSwitcher)findViewById(R.id.alamat);
        ViewSwitcher viewSwitcher1 =   (ViewSwitcher)findViewById(R.id.noKontak);
        ViewSwitcher viewSwitcher2 =   (ViewSwitcher)findViewById(R.id.email);
        ViewSwitcher viewSwitcher3 =   (ViewSwitcher)findViewById(R.id.facebook);
        ViewSwitcher viewSwitcher4 =   (ViewSwitcher)findViewById(R.id.twitter);
        ViewSwitcher viewSwitcher5 =   (ViewSwitcher)findViewById(R.id.button);

        View myFirstView = findViewById(R.id.view_kontak);
        View mySecondView = findViewById(R.id.edit_kontak);
        // TODO Auto-generated method stub
        if (viewSwitcher1.getCurrentView() != myFirstView){

            viewSwitcher0.showPrevious();
            viewSwitcher1.showPrevious();
            viewSwitcher2.showPrevious();
            viewSwitcher3.showPrevious();
            viewSwitcher4.showPrevious();
            viewSwitcher5.showPrevious();
        } else if (viewSwitcher1.getCurrentView() != mySecondView){

            viewSwitcher0.showNext();
            viewSwitcher1.showNext();
            viewSwitcher2.showNext();
            viewSwitcher3.showNext();
            viewSwitcher4.showNext();
            viewSwitcher5.showNext();
        }
    }

    private void cameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgProfile.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imgProfile.setImageBitmap(bm);
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
                Toast.makeText(ProfileMhs.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_NIM, nim);
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
            sKelas = data.getString(Config.KEY_KELAS);
            sKontak = data.getString(Config.KEY_KONTAK);
            sBintang = data.getInt(Config.KEY_BINTANG);
            sAlamat = data.getString(Config.KEY_ALAMAT);
            sEmail = data.getString(Config.KEY_EMAIL);
            sTW = data.getString(Config.KEY_TW);
            sFB = data.getString(Config.KEY_FB);

        }catch (JSONException e){
            e.printStackTrace();
        }

        vNim.setText(sNim);
        vNama.setText(sNama);
        vKelas.setText(sKelas);
        vKontak.setText(sKontak);
        vBintang.setText(sBintang.toString());
        vAlamat.setText(sAlamat);
        vEmail.setText(sEmail);
        vTW.setText(sTW);
        vFB.setText(sFB);
    }

    private void updateData(){
        loadingMhs = ProgressDialog.show(this, "Please wait...", "Updating Data...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_uMhs, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase(Config.SUCCESS)){
                    loadingMhs.dismiss();
                    switchProf();
                    Toast.makeText(ProfileMhs.this, "Success", Toast.LENGTH_SHORT).show();
                }else{
                    loadingMhs.dismiss();
                    Toast.makeText(ProfileMhs.this, "Data not update", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingMhs.dismiss();
                Toast.makeText(ProfileMhs.this, "No connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_NIM, nim);
                params.put(Config.KEY_ALAMAT, edAlamat.getText().toString());
                params.put(Config.KEY_KONTAK, edKontak.getText().toString());
                params.put(Config.KEY_EMAIL, edEmail.getText().toString());
                params.put(Config.KEY_FB, edFB.getText().toString());
                params.put(Config.KEY_TW, edTW.getText().toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
