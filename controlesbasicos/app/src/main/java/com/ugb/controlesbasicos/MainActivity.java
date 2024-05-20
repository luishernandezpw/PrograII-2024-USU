package com.ugb.controlesbasicos;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView tempVal;
    Button btn;
    FloatingActionButton btnRegresar;
    String accion="nuevo", id="", urlCompletaImg="", getUrlCompletaImgFirestore = "", rev="", idAmigo="";
    Intent tomarFotoIntent;
    ImageView img;
    utilidades utls;
    detectarInternet di;
    DatabaseReference databaseReference;
    String miToken="";
    Token objToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        obtenerToken();
        di = new detectarInternet(getApplicationContext());
        utls = new utilidades();
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
                try {
                    subirFotoFirestore();
                }catch (Exception e){
                    mostrarMsg("Error al llamar metodos de subir fotos: "+ e.getMessage());
                }
            }
        });
        mostrarDatosAmigos();
    }
    void obtenerToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(tarea-> {
            if (!tarea.isSuccessful()) {
                return;
            }
            miToken = tarea.getResult();
        });
    }
    private void subirFotoFirestore(){
        mostrarMsg("Subiendo foto al servidor...");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        Uri file = Uri.fromFile(new File(urlCompletaImg));
        final StorageReference reference = storageReference.child("fotos/"+file.getLastPathSegment());

        final UploadTask uploadTask = reference.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mostrarMsg("Error al subir la foto: "+ e.getMessage());
            }
        });
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mostrarMsg("Foto subida con exito.");
                Task<Uri> downloadUri = uploadTask.continueWithTask(task -> reference.getDownloadUrl()).addOnCompleteListener(task -> {
                    if( task.isSuccessful() ){
                        getUrlCompletaImgFirestore = task.getResult().toString();
                        guardarAmigos();
                    }else{
                        mostrarMsg("La foto se subio, pero con errores, nose pudo obtener el enlace.");
                    }
                });
            }
        });
    }
    private void guardarAmigos(){
        try {
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

            databaseReference = FirebaseDatabase.getInstance().getReference("amigos");
            String key  = databaseReference.push().getKey();

            if( miToken.equals("") || miToken==null ){
                obtenerToken();
            }
            amigos amigo = new amigos(idAmigo,nombre,direccion,tel,email,dui,urlCompletaImg,getUrlCompletaImgFirestore,miToken);
            if( key!=null ){
                databaseReference.child(key).setValue(amigo).addOnSuccessListener(unused -> {
                    mostrarMsg("Amigo registrado con exito.");
                    regresarListaAmigos();
                });
            }else{
                mostrarMsg("Error no se inserto el amigo en la base de datos.");
            }
        }catch (Exception ex){
            mostrarMsg("Error al guardar amigos: "+ex.getMessage());
        }
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
                JSONObject jsonObject = new JSONObject(parametros.getString("amigos")).getJSONObject("value");
                id = jsonObject.getString("_id");
                rev = jsonObject.getString("_rev");
                idAmigo = jsonObject.getString("idAmigo");

                tempVal = findViewById(R.id.txtnombre);
                tempVal.setText(jsonObject.getString("nombre"));

                tempVal = findViewById(R.id.txtdireccion);
                tempVal.setText(jsonObject.getString("direccion"));

                tempVal = findViewById(R.id.txtTelefono);
                tempVal.setText(jsonObject.getString("telefono"));

                tempVal = findViewById(R.id.txtemail);
                tempVal.setText(jsonObject.getString("email"));

                tempVal = findViewById(R.id.txtdui);
                tempVal.setText(jsonObject.getString("dui"));

                urlCompletaImg = jsonObject.getString("urlCompletaFoto");
                Bitmap bitmap = BitmapFactory.decodeFile(urlCompletaImg);
                img.setImageBitmap(bitmap);
            }else{ //nuevo registro
                idAmigo = utls.generarIdUnico();
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar los datos: "+ e.getMessage());
        }
    }
}