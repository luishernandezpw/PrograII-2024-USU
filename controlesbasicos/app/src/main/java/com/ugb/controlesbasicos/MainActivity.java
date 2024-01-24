package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tempVal;
    RadioGroup opt;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            btn = findViewById(R.id.btnCalcular);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        opt = findViewById(R.id.optCalculadora);

                        tempVal = findViewById(R.id.txtnum1);
                        double num1 = Double.parseDouble(tempVal.getText().toString());

                        tempVal = findViewById(R.id.txtnum2);
                        double num2 = Double.parseDouble(tempVal.getText().toString());

                        double resp = 0;
                        switch (opt.getCheckedRadioButtonId()){
                            case R.id.optSuma:
                                resp = num1+num2;
                                break;
                            case R.id.optResta:
                                resp = num1-num2;
                                break;
                            case R.id.optMultiplicacion:
                                resp=num1*num2;
                                break;
                            case R.id.optDivision:
                                resp = num1/num2;
                                break;
                        }
                        tempVal = findViewById(R.id.lblrespuesta);
                        tempVal.setText("Suma: " + resp);
                    }catch (Exception e){
                        tempVal = findViewById(R.id.lblrespuesta);
                        tempVal.setText("Error: "+ e.getMessage());
                    }
                }
            });

    }
}