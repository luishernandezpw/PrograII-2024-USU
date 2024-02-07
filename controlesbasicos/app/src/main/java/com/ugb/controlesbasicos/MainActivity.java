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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TabHost tbh;
    Spinner spn;
    TextView tempVal;
    Button btn;
    conversores miObj = new conversores();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbh = findViewById(R.id.tbhConversores);
        tbh.setup();

        tbh.addTab(tbh.newTabSpec("longitud").setIndicator("LONGITUD").setContent(R.id.tabLongitud));
        tbh.addTab(tbh.newTabSpec("almacenamiento").setIndicator("ALMACENAMIENTO").setContent(R.id.tabAlmacenamiento));
        tbh.addTab(tbh.newTabSpec("monedas").setIndicator("MONEDAS").setContent(R.id.tabMonedas));

        btn = findViewById(R.id.btnLongitudConvertir);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spn = findViewById(R.id.spnLongitudDe);
                int de = spn.getSelectedItemPosition();

                spn = findViewById(R.id.spnLongitudA);
                int a = spn.getSelectedItemPosition();

                tempVal = findViewById(R.id.txtLongitudCantidad);
                double cantidad = Double.parseDouble(tempVal.getText().toString());

                double resp = miObj.convertir(0, de, a, cantidad);
                Toast.makeText(getApplicationContext(), "Respuesta: "+ resp, Toast.LENGTH_LONG).show();
            }
        });
    }
}
class conversores{
    double[][] valores = {
            {1, 100, 39.3701, 3.28084, 1.193, 1.09361, 0.001, 0.000621371},
            {},
            {},
            {}
    };
    public double convertir(int opcion, int de, int a, double cantidad){
        return valores[opcion][a] / valores[opcion][de] * cantidad;
    }
}




