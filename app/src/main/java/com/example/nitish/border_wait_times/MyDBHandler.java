package com.example.nitish.border_wait_times;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyDBHandler extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static final int DATABASE_VERSION = 1; //database version
    private static final String DATABASE_NAME = "time.db"; //database name
    public static final String TIMES = "times"; //table name
    public static final String ID = "id";
    public static final String CUSTOMS_OFFICE = "customsoffice";
    public static final String LOCATION = "location";
    public static final String LASTUPDATED = "lastupdated";
    public static final String COMMERICIAL_FLOW = "commericialflow";
    public static final String TRAVELLERS_FLOW = "travellersflow";
    public static final String FAVOURITE = "favourite";
    public static final String[] ALL_KEYS = new String[] {ID, CUSTOMS_OFFICE, LOCATION, LASTUPDATED, COMMERICIAL_FLOW, TRAVELLERS_FLOW, FAVOURITE};

    private final Context myContext;
    String DB_PATH = null;
    private SQLiteDatabase myDataBase;

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.myContext = context;
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        if(checkFile() == false) {
            copyDataBase();
        }
    }

    private boolean checkFile() {
        File file = new File(DB_PATH + DATABASE_NAME);
        if(file.exists()){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TIMES);
        onCreate(db);
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = 	db.query(TIMES, ALL_KEYS, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor distinct(String column_name) {
        SQLiteDatabase db = this.getWritableDatabase();
            return db.rawQuery("select DISTINCT "+ column_name +" from "+ TIMES + " order by " + column_name, null);
    }

    public Cursor distinctWhereLocation(String fromName, String toName, String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select DISTINCT "+ fromName + " from "+ TIMES + " WHERE " + toName + " = '" + item + "'" + "order by " + fromName, null);
    }

    public Cursor favouriteList(String columnName, String fav){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select DISTINCT "+ columnName + " from "+ TIMES + " WHERE " + fav + " = '" + "1" + "'" + "order by " + columnName, null);
    }

    public Cursor addFavourite(String type, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("update " + TIMES + " SET favourite " + " = '" + "1" + "'" + " where " + type + " = '" + name + "'", null);
    }

    public Cursor removeFavourite(String type, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("update " + TIMES + " SET favourite " + " = '" + "0" + "'" + " where " + type + " = '" + name + "'", null);
    }

    public Cursor checkFavourite(String fav, String l, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select DISTINCT "+ fav + " from "+ TIMES + " WHERE " + l + " = '" + name + "'", null);
    }

    public Cursor getData(String type, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select " + LASTUPDATED + ", " + COMMERICIAL_FLOW + ", " + TRAVELLERS_FLOW + " from "+ TIMES + " WHERE " + type + " = '" + name + "'", null);
    }

    private void createDBFolder() {
        File file = new File(DB_PATH);
        file.mkdir();
    }

    private void copyDataBase(){
        //Open your local db as the input stream
        InputStream myInput = null;
        try {
            myInput = myContext.getAssets().open(DATABASE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creates databases folder if it does not exist
        createDBFolder();

        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = null;
        try {
            myOutput = new FileOutputStream(outFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Close the streams
        try {
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}