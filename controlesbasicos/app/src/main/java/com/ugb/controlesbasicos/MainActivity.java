package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TabHost tbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbh = findViewById(R.id.tbhConversores);
        tbh.setup();

        tbh.addTab(tbh.newTabSpec("longitud").setIndicator("LONGITUD").setContent(R.id.tabLongitud));
        tbh.addTab(tbh.newTabSpec("almacenamiento").setIndicator("ALMACENAMIENTO").setContent(R.id.tabAlmacenamiento));
        tbh.addTab(tbh.newTabSpec("monedas").setIndicator("MONEDAS").setContent(R.id.tabMonedas));
    }
}