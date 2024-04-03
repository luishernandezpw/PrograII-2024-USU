package com.ugb.controlesbasicos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    private static final String dbname = "db_amigos";
    private static final int v=1;
    private static final String SQldb = "CREATE TABLE amigos(id text, rev text, idAmigo text, nombre text, direccion text, telefono text, email text, dui text, foto text)";
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
                db.execSQL("INSERT INTO amigos(id,rev,idAmigo,nombre,direccion,telefono,email,dui, foto) VALUES('"+ datos[0] +"', '"+ datos[1] +"', '"+
                        datos[2] +"', '"+ datos[3] +"','"+ datos[4] +"','"+ datos[5] +"','"+ datos[6] +"','"+ datos[7] +"', '"+ datos[8] +"')");
            } else if (accion.equals("modificar")) {
                db.execSQL("UPDATE amigos SET id='"+ datos[0] +"',rev='"+ datos[1] +"', nombre='"+ datos[3] +"',direccion='"+ datos[4] +"',telefono='"+
                        datos[5] +"',email='"+ datos[6] +"',dui='"+ datos[7] +"', foto='"+ datos[8] +"' WHERE idAmigo='"+ datos[2] +"'");
            } else if (accion.equals("eliminar")) {
                db.execSQL("DELETE FROM amigos WHERE idAmigo='"+ datos[2] +"'");
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
