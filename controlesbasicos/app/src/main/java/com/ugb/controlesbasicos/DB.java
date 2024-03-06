package com.ugb.controlesbasicos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    private static final String dbname = "db_amigos";
    private static final int v=1;
    private static final String SQldb = "CREATE TABLE amigos(idAmigo integer primary key autoincrement, nombre text, direccion text, telefono text, email text, dui text)";
    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, v);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQldb);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //para hacer la actualizacion de la BD
    }
    public String administrar_amigos(String accion, String[] datos){
        try {
            SQLiteDatabase db = getWritableDatabase();
            if(accion.equals("nuevo")){
                db.execSQL("INSERT INTO amigos(nombre,direccion,telefono,email,dui) VALUES('"+ datos[1] +"','"+ datos[2] +"','"+ datos[3] +"','"+ datos[4] +"','"+ datos[5] +"')");
            } else if (accion.equals("modificar")) {
                db.execSQL("UPDATE amigos SET nombre='"+ datos[1] +"',direccion='"+ datos[2] +"',telefono='"+ datos[3] +"',email='"+ datos[4] +"',dui='"+ datos[5] +"' WHERE idAmigo='"+ datos[0] +"'");
            } else if (accion.equals("eliminar")) {
                db.execSQL("DELETE FROM amigos WHERE idAmigo='"+ datos[0] +"'");
            }
            return "ok";
        }catch (Exception e){
            return "Error: "+ e.getMessage();
        }
    }
    public Cursor consultar_amigos(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM amigos ORDER BY nombre", null);
        return cursor;

    }
}
