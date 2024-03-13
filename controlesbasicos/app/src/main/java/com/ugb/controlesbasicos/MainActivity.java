package com.ugb.controlesbasicos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView tempVal;
    Button btn;
    FloatingActionButton btnRegresar;
    String accion="nuevo", id="", urlCompletaImg="";
    Intent tomarFotoIntent;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.imgAmigo);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFotoAmigo();
            }
        });
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
                String[] datos = new String[]{id,nombre,direccion,tel,email,dui, urlCompletaImg};
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if( requestCode==1 && resultCode==RESULT_OK ){
                Bitmap imagenBitmap = BitmapFactory.decodeFile(urlCompletaImg);
                img.setImageBitmap(imagenBitmap);
            }else{
                mostrarMsg("Se cancelo la toma de la foto");
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar la camara: "+ e.getMessage());
        }
    }
    private void tomarFotoAmigo(){
        tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if( tomarFotoIntent.resolveActivity(getPackageManager())!=null ){
            File fotoAmigo = null;
            try{
                fotoAmigo = crearImagenAmigo();
                if( fotoAmigo!=null ){
                    Uri uriFotoAmigo = FileProvider.getUriForFile(MainActivity.this, "com.ugb.controlesbasicos.fileprovider", fotoAmigo);
                    tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoAmigo);
                    startActivityForResult(tomarFotoIntent, 1);
                }else{
                    mostrarMsg("NO pude tomar la foto");
                }
            }catch (Exception e){
                mostrarMsg("Error al tomar la foto: "+ e.getMessage());
            }
        /*}else{
            mostrarMsg("No se selecciono una foto...");
        }*/
    }
    private File crearImagenAmigo() throws Exception{
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "imagen_"+ fechaHoraMs +"_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if( !dirAlmacenamiento.exists() ){
            dirAlmacenamiento.mkdirs();
        }
        File image = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        urlCompletaImg = image.getAbsolutePath();
        return image;
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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

                urlCompletaImg = datos[6];
                Bitmap bitmap = BitmapFactory.decodeFile(urlCompletaImg);
                img.setImageBitmap(bitmap);
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar los datos: "+ e.getMessage());
        }
    }
}