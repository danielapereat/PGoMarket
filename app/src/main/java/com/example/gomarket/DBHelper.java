package com.example.gomarket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper{
    public static final String DBNAME = "GoMarket.db";
    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInBytes;
    Integer drawable;
    public DBHelper(Context context) {
        super(context, "GoMarket.db", null, 2);
    }

    //CREAR TABLAS
    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, usuario TEXT, contraseña TEXT, nombre TEXT,apellidos TEXT, email TEXT)");
        MyDB.execSQL("create Table stores(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nit TEXT, nombre TEXT,descripcion TEXT, usuario TEXT, ubicacion TEXT, dias INTEGER,inicio TEXT,fin TEXT,imagen BLOB NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists usuarios");
    }

    //INERTAR DATOS NE TABLA "USERS"
    public Boolean insertDataUser(String username, String password, String name, String lastName, String mail){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("usuario", username);
        contentValues.put("contraseña", password);
        contentValues.put("nombre", name);
        contentValues.put("apellidos", lastName);
        contentValues.put("email", mail);
        long result = MyDB.insert("users", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    //INSERTAR DATOS EN TABLA "STORES"
    public Boolean insertDataStores(String username, String name,String info, String nit, String location, Integer days, String start, String end, Bitmap img){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();

        objectByteArrayOutputStream=new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat. JPEG, 100, objectByteArrayOutputStream);
        imageInBytes=objectByteArrayOutputStream.toByteArray();
        contentValues.put("nit", nit);
        contentValues.put("nombre", name);
        contentValues.put("descripcion", info);
        contentValues.put("usuario", username);
        contentValues.put("ubicacion", location);
        contentValues.put("dias", days);
        contentValues.put("inicio", start);
        contentValues.put("fin", end);
        contentValues.put("imagen", imageInBytes);
        long result = MyDB.insert("stores", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    //VERIFICAR SI ES USUSARIO SE REPITE
    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where usuario = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    //VERIFICAR SI EL NIT SE REPITE
    public Boolean checknit(String nit) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from stores where nit = ?", new String[]{nit});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    //VERIFICAR SI EL USUARIO Y LA CONTRASEÑA COINCIDEN (VALIDACIÓN DE INICIO DE SESIÓN)
    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where usuario = ? and contraseña = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    //CONSULTAR TODAS LAS TIENDAS
    public List<ListElement> mostrarTiendas(){
        SQLiteDatabase bd = getReadableDatabase();
        Cursor cursor=bd.rawQuery("Select * from stores" , null);
        List<ListElement> tiendas = new ArrayList<>();
        drawable = R.drawable.ic_action_next;
        if (cursor.moveToFirst()){
            do{
                tiendas.add(new ListElement(cursor.getInt(0),cursor.getString(1), cursor.getString(2),cursor.getString(3),
                        cursor.getString(4),cursor.getString(5),cursor.getInt(6),cursor.getString(7),
                        cursor.getString(8), cursor.getBlob(9),drawable));

            }while (cursor.moveToNext());
        }

        return tiendas;
    }

    //CONSULTAR TIENDAS POR NOMBRE
    public List<ListElement> buscarTiendas(List tiendasE, String nombreT){
        SQLiteDatabase bd = getReadableDatabase();
        Cursor cursor=bd.rawQuery("Select * from stores where nombre like ?", new String[]{"%"+ nombreT+ "%" } , null);
        if (cursor.moveToFirst()){
            do{
                tiendasE.add(new ListElement(cursor.getInt(0),cursor.getString(1), cursor.getString(2),cursor.getString(3),
                        cursor.getString(4),cursor.getString(5),cursor.getInt(6),cursor.getString(7),
                        cursor.getString(8), cursor.getBlob(9),drawable));

            }while (cursor.moveToNext());
        }
        return tiendasE;
    }

    //CONSULTAR TIENDAS POR USUARIO QUE LAS CREO
    public List<ListElement> mostrarTiendasUsuario(String user){
        SQLiteDatabase bd = getReadableDatabase();
        Cursor cursor=bd.rawQuery("Select * from stores where usuario = ?", new String[]{user} , null);
        List<ListElement> tiendas = new ArrayList<>();
        drawable = R.drawable.ic_action_next;
        if (cursor.moveToFirst()){
            do{
                tiendas.add(new ListElement(cursor.getInt(0),cursor.getString(1), cursor.getString(2),cursor.getString(3),
                        cursor.getString(4),cursor.getString(5),cursor.getInt(6),cursor.getString(7),
                        cursor.getString(8), cursor.getBlob(9),drawable));

            }while (cursor.moveToNext());
        }

        return tiendas;
    }

    //MODIFICAR UNA TIENDA ESPECIFICA DEL USUARIO DE ACUERDO A SU ID
    public boolean modificarTiendasUsuario(Integer id, String nit, String name, String info, String user, String location, Integer days,String start,String end, Bitmap image){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues contentValues= new ContentValues();

        objectByteArrayOutputStream=new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat. JPEG, 100, objectByteArrayOutputStream);
        imageInBytes=objectByteArrayOutputStream.toByteArray();
        contentValues.put("nit", nit);
        contentValues.put("nombre", name);
        contentValues.put("descripcion", info);
        contentValues.put("usuario", user);
        contentValues.put("ubicacion", location);
        contentValues.put("dias", days);
        contentValues.put("inicio", start);
        contentValues.put("fin", end);
        contentValues.put("imagen", imageInBytes);
        long result = db.update("stores",contentValues, "id="+id,  null);

        if(result==-1) return false;
        else
            return true;
    }

    //ELIMINAR UNA TIENDA ESPECIFICA DEL USUARIO DE ACUERDO A SU ID
    public boolean eliminarTiendasUsuario(Integer id){
        SQLiteDatabase db = getReadableDatabase();

        long result = db.delete("stores", "id="+id,  null);

        if(result==-1) return false;
        else
            return true;
    }

    //MOSTRAR LA INFORMACIÓN DE UNA TIENDA ESPECIFICA DE ACUERDO A SU ID
    public List<ListElement> mostrarInfoTiendas(Integer id){
        System.out.println(id);
        SQLiteDatabase bd = getReadableDatabase();
        Cursor cursor=bd.rawQuery("Select * from stores where id = ?", new String[]{String.valueOf(id)} , null);
        List<ListElement> tiendas = new ArrayList<>();
        drawable = R.drawable.ic_action_next;
        if (cursor.moveToFirst()){
            do{
                tiendas.add(new ListElement(cursor.getInt(0),cursor.getString(1), cursor.getString(2),cursor.getString(3),
                        cursor.getString(4),cursor.getString(5),cursor.getInt(6),cursor.getString(7),
                        cursor.getString(8), cursor.getBlob(9),drawable));

            }while (cursor.moveToNext());
        }
        return tiendas;
    }

    //MOSTRAR LA INFORMACIÓN DE UN USUARIO ESPECIFICO DE ACUERDO A SU USER
    public List<ListElement> mostrarInfoUsuarios(String user){
        SQLiteDatabase bd = getReadableDatabase();
        Cursor cursor=bd.rawQuery("Select * from users where usuario = ?", new String[]{user} , null);
        List<ListElement> tiendas = new ArrayList<>();
        drawable = R.drawable.ic_action_next;
        if (cursor.moveToFirst()){
            do{
                tiendas.add(new ListElement(cursor.getString(1),cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getString(5)));
                System.out.println(cursor.getString(1));
                System.out.println(cursor.getString(2));
                System.out.println(cursor.getString(3));
                System.out.println(cursor.getString(4));
                System.out.println(cursor.getString(5));

            }while (cursor.moveToNext());
        }
        return tiendas;
    }

    //MODIFICAR LA INFORMACÓN DE UN USUARIO ESPECIFICO DE ACUERDO A SU USER
    public boolean modificarInfoUsuarios( String name, String lastName,  String username,String user, String mail){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues contentValues= new ContentValues();

        contentValues.put("usuario", username);
        contentValues.put("apellidos", lastName);
        contentValues.put("nombre", name);
        contentValues.put("email", mail);


        long result = db.update("users",contentValues, "usuario= ?",  new String[]{user});

        if(result==-1) return false;
        else
            return true;
    }

    //CAMBIAR LA CONTRASEÑA DE UN USUARIO ESPECIFICO DE ACUERDO A SU USER
    public boolean CambiarContraseña(String user, String password){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("contraseña", password);
        long result = db.update("users",contentValues, "usuario= ?",  new String[]{user});

        if(result==-1) return false;
        else
            return true;
    }

}
