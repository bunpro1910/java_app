package vn.edu.greenwich.cw1.database;

public class TripEntry {
    public static final String TABLE_NAME = "trip";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_START_DATE = "start_date";
    public static final String COL_RISK = "risk";
    public static final String COL_Destination = "destination";
    public static final String COL_Description = "description";
    public static final String COL_Current_Amount = "Current_Amount";
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY," +
                    COL_NAME + " TEXT NOT NULL," +
                    COL_Destination + " TEXT NOT NULL," +
                    COL_Description + " TEXT NOT NULL," +
                    COL_START_DATE + " TEXT NOT NULL," +
                    COL_Current_Amount+" INTEGER NOT NULL,"+
                    COL_RISK + " INTEGER NULL)";

    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}