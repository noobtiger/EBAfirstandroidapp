package eba.bodyloger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DB_datasource {

    private static final String LOGTAG = "TEST";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private static final String[] allCoulmns={
            DB_sqlOpenHelper.COLUMN_ID,
            DB_sqlOpenHelper.COLUMN_DATE,
            DB_sqlOpenHelper.COLUMN_WEIGHT,
            DB_sqlOpenHelper.COLUMN_WAIST,
            DB_sqlOpenHelper.COLUMN_NECK,
            DB_sqlOpenHelper.COLUMN_BF,
            DB_sqlOpenHelper.COLUMN_BMI
    };

    public DB_datasource(Context context){

        dbhelper=new DB_sqlOpenHelper(context);


    }

    public void open(){

        database=dbhelper.getWritableDatabase();
        Log.i(LOGTAG, "database open");
    }

    public void close(){

        dbhelper.close();
        Log.i(LOGTAG, "database closed");
    }
    public DB_Model create(DB_Model model){
        ContentValues values=new ContentValues();
        values.put(DB_sqlOpenHelper.COLUMN_DATE, model.getdate());
        values.put(DB_sqlOpenHelper.COLUMN_WEIGHT, model.getweight());
        values.put(DB_sqlOpenHelper.COLUMN_WAIST, model.getwaist());
        values.put(DB_sqlOpenHelper.COLUMN_NECK, model.getneck());
        values.put(DB_sqlOpenHelper.COLUMN_BF, model.getBF());
        values.put(DB_sqlOpenHelper.COLUMN_BMI, model.getBMI());
        long insertid=database.insert(DB_sqlOpenHelper.TABLE_TOURS, null, values);
        model.setId(insertid);
        return model;
    }

    public ArrayList<String>  findDate(){
        ArrayList<String> Tour = new ArrayList<String>();
        Cursor cursor=database.query (DB_sqlOpenHelper.TABLE_TOURS,allCoulmns,
                null,null,null,null,null);
        if (cursor.getCount()>0){

            while(cursor.moveToNext()){

                String date=cursor.getString(cursor.getColumnIndex(DB_sqlOpenHelper.COLUMN_DATE));
                Tour.add(date);
            }
        }
        return Tour;
    }
    public String  findLastWaist(){

        Cursor cursor=database.query (DB_sqlOpenHelper.TABLE_TOURS,allCoulmns,
                null,null,null,null,null);
        String waist="";
        while(cursor.moveToNext()){

            waist=cursor.getString(cursor.getColumnIndex(DB_sqlOpenHelper.COLUMN_WAIST));

        }
        return waist;
    }

    public String  findLastWeight(){

        Cursor cursor=database.query (DB_sqlOpenHelper.TABLE_TOURS,allCoulmns,
                null,null,null,null,null);
        String weight="";
        while(cursor.moveToNext()){

            weight=cursor.getString(cursor.getColumnIndex(DB_sqlOpenHelper.COLUMN_WEIGHT));

        }
        return weight;
    }

    public String  findLastNeck(){

        Cursor cursor=database.query (DB_sqlOpenHelper.TABLE_TOURS,allCoulmns,
                null,null,null,null,null);
        String neck="";
        while(cursor.moveToNext()){

            neck=cursor.getString(cursor.getColumnIndex(DB_sqlOpenHelper.COLUMN_NECK));

        }
        return neck;
    }

    public String  findLastBMI(){

        Cursor cursor=database.query (DB_sqlOpenHelper.TABLE_TOURS,allCoulmns,
                null,null,null,null,null);
        String bmi="";
        while(cursor.moveToNext()){

            bmi=cursor.getString(cursor.getColumnIndex(DB_sqlOpenHelper.COLUMN_BMI));

        }
        return bmi;
    }

    public String  findLastBF(){

        Cursor cursor=database.query (DB_sqlOpenHelper.TABLE_TOURS,allCoulmns,
                null,null,null,null,null);
        String bf="";
        while(cursor.moveToNext()){

            bf=cursor.getString(cursor.getColumnIndex(DB_sqlOpenHelper.COLUMN_BF));

        }
        return bf;
    }

    public int findTotalRows(){
        Cursor cursor=database.query (DB_sqlOpenHelper.TABLE_TOURS,allCoulmns,
                null,null,null,null,null);
        int total=0;
        while(cursor.moveToNext()){

            total=cursor.getInt(cursor.getColumnIndex(DB_sqlOpenHelper.COLUMN_ID));

        }
        return total;

    }
    public String findRowDate(int id){
        String dateselected="";
        Cursor cursor=database.rawQuery("Select * from " + DB_sqlOpenHelper.TABLE_TOURS +
                " WHERE " +  DB_sqlOpenHelper.COLUMN_ID+" = "+id,null);
        // dateselected=cursor.toString();
        while(cursor.moveToNext()) {
            dateselected = cursor.getString(1).toString();
            Log.i(LOGTAG, "date is " + dateselected + " id is " + id);
        }
        return dateselected;
    }

    public Double findRowWeight(int id){
        Double weight_selected=0.0;
        Cursor cursor=database.rawQuery("Select * from " + DB_sqlOpenHelper.TABLE_TOURS +
                " WHERE " +  DB_sqlOpenHelper.COLUMN_ID+" = "+id,null);

        while(cursor.moveToNext()) {
            weight_selected = cursor.getDouble(2);
        }
        return weight_selected;
    }

    public Double findRowWaist(int id){
        Double weight_selected=0.0;
        Cursor cursor=database.rawQuery("Select * from " + DB_sqlOpenHelper.TABLE_TOURS +
                " WHERE " +  DB_sqlOpenHelper.COLUMN_ID+" = "+id,null);

        while(cursor.moveToNext()) {
            weight_selected = cursor.getDouble(3);
        }
        return weight_selected;
    }

    public Double findRowNeck(int id){
        Double neck_selected=0.0;
        Cursor cursor=database.rawQuery("Select * from " + DB_sqlOpenHelper.TABLE_TOURS +
                " WHERE " +  DB_sqlOpenHelper.COLUMN_ID+" = "+id,null);

        while(cursor.moveToNext()) {
            neck_selected = cursor.getDouble(4);
        }
        return neck_selected;
    }

    public void updateData(int id, String weight, String waist, String neck){
        ContentValues values=new ContentValues();

        values.put(DB_sqlOpenHelper.COLUMN_WEIGHT, weight);
        values.put(DB_sqlOpenHelper.COLUMN_WAIST, waist);
        values.put(DB_sqlOpenHelper.COLUMN_NECK, neck);
        database.update(DB_sqlOpenHelper.TABLE_TOURS,values, DB_sqlOpenHelper.COLUMN_ID + " = "+ id,null);
        Log.i(LOGTAG,"migh hav been updated");
    }
}
