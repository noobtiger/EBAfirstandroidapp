package eba.bodyloger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB_sqlOpenHelper extends SQLiteOpenHelper {




    private static final String DATABASE_NAME = "BodyLog.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TOURS = "bodyMeasurements";
    public static final String COLUMN_ID = "bodyID";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_WAIST= "waist";
    public static final String COLUMN_NECK = "neck";
    public static final String COLUMN_BF = "BF";
    public static final String COLUMN_BMI = "BMI";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TOURS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_WEIGHT + " REAL, " +
                    COLUMN_WAIST + " REAL, " +
                    COLUMN_NECK + " REAL, " +
                    COLUMN_BF + " REAL," +
                    COLUMN_BMI + " REAL " +
                    ")";



    public DB_sqlOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+TABLE_TOURS);
        onCreate(db);
    }

}
