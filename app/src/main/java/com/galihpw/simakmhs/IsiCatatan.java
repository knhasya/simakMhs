package com.galihpw.simakmhs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import static com.galihpw.simakmhs.Catatan.CATATAN_MESSAGE1;
import static com.galihpw.simakmhs.Catatan.CATATAN_MESSAGE2;

/**
 * Created by Nada on 12/21/2016.
 */

public class IsiCatatan extends AppCompatActivity {

    String sJudulCat, sIsiCat;
    TextView tvJudulCat, tvIsiCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listresume);

        tvJudulCat = (TextView) findViewById(R.id.judulcat);
        tvIsiCat = (TextView) findViewById(R.id.isiCat);

        Intent intent = getIntent();
        sJudulCat = intent.getStringExtra(CATATAN_MESSAGE1);
        sIsiCat = intent.getStringExtra(CATATAN_MESSAGE2);

        tvJudulCat.setText("Catatan pertemuan ke: " + sJudulCat);
        tvIsiCat.setText(sIsiCat);
    }


}

