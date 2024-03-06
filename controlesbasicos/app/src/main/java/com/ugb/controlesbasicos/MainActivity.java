package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    TextView tempVal;
    Button btn;
    FloatingActionButton btnRegresar;
    String accion="nuevo", id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegresar = findViewById(R.id.btnRegresarListaAmigos);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regresarListaAmigos();
            }
        });
        btn = findViewById(R.id.btnGuardarAmigos);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempVal = findViewById(R.id.txtnombre);
                String nombre = tempVal.getText().toString();

                tempVal = findViewById(R.id.txtdireccion);
                String direccion = tempVal.getText().toString();

                tempVal = findViewById(R.id.txtTelefono);
                String tel = tempVal.getText().toString();

                tempVal = findViewById(R.id.txtemail);
                String email = tempVal.getText().toString();

                tempVal = findViewById(R.id.txtdui);
                String dui = tempVal.getText().toString();

                DB db = new DB(getApplicationContext(),"", null, 1);
                String[] datos = new String[]{id,nombre,direccion,tel,email,dui};
                String respuesta = db.administrar_amigos(accion, datos);
                if( respuesta.equals("ok") ){
                    Toast.makeText(getApplicationContext(), "Amigo Registrado con Exito.", Toast.LENGTH_SHORT).show();
                    regresarListaAmigos();
                }else{
                    Toast.makeText(getApplicationContext(), "Error: "+ respuesta, Toast.LENGTH_LONG).show();
                }
            }
        });
        mostrarDatosAmigos();
    }
    private void regresarListaAmigos(){
        Intent abrirVentana = new Intent(getApplicationContext(), lista_amigos.class);
        startActivity(abrirVentana);
    }
    private void mostrarDatosAmigos(){
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");
            if( accion.equals("modificar") ){
                String[] datos = parametros.getStringArray("amigos");
                id = datos[0];

                tempVal = findViewById(R.id.txtnombre);
                tempVal.setText(datos[1]);

                tempVal = findViewById(R.id.txtdireccion);
                tempVal.setText(datos[2]);

                tempVal = findViewById(R.id.txtTelefono);
                tempVal.setText(datos[3]);

                tempVal = findViewById(R.id.txtemail);
                tempVal.setText(datos[4]);

                tempVal = findViewById(R.id.txtdui);
                tempVal.setText(datos[5]);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error al mostrar los datos: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}