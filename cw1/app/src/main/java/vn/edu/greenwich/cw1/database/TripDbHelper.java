package vn.edu.greenwich.cw1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class TripDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Trip";
    public static final int DATABASE_VERSION = 1;

    public TripDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TripEntry.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(TripItemEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(TripItemEntry.SQL_DELETE_TABLE);
        sqLiteDatabase.execSQL(TripEntry.SQL_DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void updateRS (SQLiteDatabase sqLiteDatabase,String s){
        sqLiteDatabase.execSQL(s);
    }

}