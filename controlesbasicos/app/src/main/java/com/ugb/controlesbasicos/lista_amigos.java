package com.ugb.controlesbasicos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class lista_amigos extends AppCompatActivity {
    Bundle parametros = new Bundle();
    FloatingActionButton btn;
    ListView lts;
    Cursor cAmigos;
    DB dbAmigos;
    amigos misAmigos;
    final ArrayList<amigos> alAmigos=new ArrayList<amigos>();
    final ArrayList<amigos> alAmigosCopy=new ArrayList<amigos>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_amigos);

        btn = findViewById(R.id.btnAbrirNuevosAmigos);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parametros.putString("accion", "nuevo");
                abrirActividad(parametros);
            }
        });
        obtenerAmigos();
        buscarAmigos();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);

        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo)menuInfo;
        cAmigos.moveToPosition(info.position);
        menu.setHeaderTitle(cAmigos.getString(1));//1 es el nombre del amigo
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try {
            switch (item.getItemId()){
                case R.id.mnxAgregar:
                    parametros.putString("accion", "nuevo");
                    abrirActividad(parametros);
                    break;
                case R.id.mnxModificar:
                    String amigos[] = {
                            cAmigos.getString(0), //idAmigo
                            cAmigos.getString(1), //nombre
                            cAmigos.getString(2), //direccion
                            cAmigos.getString(3), //tel
                            cAmigos.getString(4), //email
                            cAmigos.getString(5) //dui
                    };
                    parametros.putString("accion","modificar");
                    parametros.putStringArray("amigos", amigos);
                    abrirActividad(parametros);
            }
            return true;
        }catch (Exception e){
            mostrarMsg("Error en menu: "+ e.getMessage());
            return super.onContextItemSelected(item);
        }
    }
    private void abrirActividad(Bundle parametros){
        Intent abriVentana = new Intent(getApplicationContext(), MainActivity.class);
        abriVentana.putExtras(parametros);
        startActivity(abriVentana);
    }
    private void obtenerAmigos(){
        try{
            alAmigos.clear();
            alAmigosCopy.clear();

            dbAmigos = new DB(lista_amigos.this, "", null, 1);
            cAmigos = dbAmigos.consultar_amigos();

            if ( cAmigos.moveToFirst() ){
                lts = findViewById(R.id.ltsAmigos);
                do{
                    misAmigos = new amigos(
                            cAmigos.getString(0),//idAmigo
                            cAmigos.getString(1),//nombre
                            cAmigos.getString(2),//direccion
                            cAmigos.getString(3),//tel
                            cAmigos.getString(4),//email
                            cAmigos.getString(5)//dui
                    );
                    alAmigos.add(misAmigos);
                }while(cAmigos.moveToNext());
                alAmigosCopy.addAll(alAmigos);

                adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alAmigos);
                lts.setAdapter(adImagenes);

                registerForContextMenu(lts);
            }else{
                mostrarMsg("No hay amigos que mostrar");
            }
        }catch (Exception e){
            mostrarMsg("Error al obtener amigos: "+ e.getMessage());
        }
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void buscarAmigos(){
        TextView tempVal;
        tempVal = findViewById(R.id.txtBuscarAmigos);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    alAmigos.clear();
                    String valor = tempVal.getText().toString().trim().toLowerCase();
                    if( valor.length()<=0 ){
                        alAmigos.addAll(alAmigosCopy);
                    }else{
                        for( amigos amigo : alAmigosCopy ){
                            String nombre = amigo.getNombre();
                            String direccion = amigo.getDireccion();
                            String tel = amigo.getTelefono();
                            String email = amigo.getEmail();
                            if( nombre.toLowerCase().trim().contains(valor) ||
                                direccion.toLowerCase().trim().contains(valor) ||
                                    tel.trim().contains(valor) ||
                                    email.trim().toLowerCase().contains(valor) ){
                                alAmigos.add(amigo);
                            }
                        }
                        adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alAmigos);
                        lts.setAdapter(adImagenes);
                    }
                }catch (Exception e){
                    mostrarMsg("Error al buscar: "+e.getMessage() );
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}