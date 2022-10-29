package vn.edu.greenwich.cw1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import vn.edu.greenwich.cw1.models.TripItem;
import vn.edu.greenwich.cw1.models.Trip;

public class TripDAO {
    protected TripDbHelper tripDbHelper;
    protected SQLiteDatabase dbWrite, dbRead;

    public TripDAO(){}
    public TripDAO(Context context) {
        tripDbHelper = new TripDbHelper(context);

        dbRead = tripDbHelper.getReadableDatabase();
        dbWrite = tripDbHelper.getWritableDatabase();
    }

    public void close() {
        dbRead.close();
        dbWrite.close();
    }

    public void reset() {
        tripDbHelper.onUpgrade(dbWrite, 0, 0);
    }

    // Trip.

    public long insertTrip(Trip trip) {
        ContentValues values = getTripValues(trip);

        return dbWrite.insert(TripEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Trip> getTripList(Trip trip, String orderByColumn, boolean isDesc) {
        String orderBy = getOrderByString(orderByColumn, isDesc);

        String selection = null;
        String[] selectionArgs = null;

        if (null != trip) {
            selection = "";
            ArrayList<String> conditionList = new ArrayList<String>();

            if (trip.getName() != null && !trip.getName().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_NAME + " LIKE ?";
                conditionList.add("%" + trip.getName() + "%");
            }
            if (trip.getCurrentAmout() != -1 ) {
                selection += " AND " + TripEntry.COL_Current_Amount + " = ?";
                conditionList.add(String.valueOf(trip.getCurrentAmout()));
            }

            if (trip.getStartDate() != null && !trip.getStartDate().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_START_DATE + " = ?";
                conditionList.add(trip.getStartDate());
            }

            if (trip.getDestination() != null && !trip.getName().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_Destination + " LIKE ?";
                conditionList.add("%" + trip.getName() + "%");
            }

            if (trip.getDescription() != null && !trip.getName().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_Description + " LIKE ?";
                conditionList.add("%" + trip.getName() + "%");
            }

            if (trip.getOwner() != -1) {
                selection += " AND " + TripEntry.COL_RISK + " = ?";
                conditionList.add(String.valueOf(trip.getOwner()));
            }


            if (!selection.trim().isEmpty()) {
                selection = selection.substring(5);
            }

            selectionArgs = conditionList.toArray(new String[conditionList.size()]);
        }

        return getTripFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    public Trip getTripById(long id) {
        String selection = TripEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return getTripFromDB(null, selection, selectionArgs, null, null, null).get(0);
    }

    public long updateTrip(Trip trip) {
        ContentValues values = getTripValues(trip);

        String selection = TripEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(trip.getId())};

        return dbWrite.update(TripEntry.TABLE_NAME, values, selection, selectionArgs);
    }


    public long deleteTrip(long id) {
        String selection = TripEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return dbWrite.delete(TripEntry.TABLE_NAME, selection, selectionArgs);
    }

    protected String getOrderByString(String orderByColumn, boolean isDesc) {
        if (orderByColumn == null || orderByColumn.trim().isEmpty())
            return null;

        if (isDesc)
            return orderByColumn.trim() + " DESC";

        return orderByColumn.trim();
    }

    protected ContentValues getTripValues(Trip trip) {
        ContentValues values = new ContentValues();

        values.put(TripEntry.COL_NAME, trip.getName());
        values.put(TripEntry.COL_START_DATE, trip.getStartDate());

        values.put(TripEntry.COL_Current_Amount, trip.getCurrentAmout());
        values.put(TripEntry.COL_RISK, trip.getOwner());
        values.put(TripEntry.COL_Destination, trip.getDestination());
        values.put(TripEntry.COL_Description, trip.getDescription());
        return values;
    }

    protected ArrayList<Trip> getTripFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Trip> list = new ArrayList<>();

        Cursor cursor = dbRead.query(TripEntry.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        while (cursor.moveToNext()) {
            Trip tripItem = new Trip();

            tripItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(TripEntry.COL_ID)));
            tripItem.setName(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_NAME)));
            tripItem.setDestination(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_Destination)));
            tripItem.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_Description)));
            tripItem.setCurrentAmout(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_Current_Amount))) );
            tripItem.setOwner(cursor.getInt(cursor.getColumnIndexOrThrow(TripEntry.COL_RISK)));
            tripItem.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_START_DATE)));

            list.add(tripItem);
        }

        cursor.close();

        return list;
    }

    // TripItem.

    public long insertTripList(TripItem tripItem) {
        ContentValues values = getTripListValues(tripItem);

        return dbWrite.insert(TripItemEntry.TABLE_NAME, null, values);
    }
    public void updatedb(String s){

       dbWrite.execSQL(s);
    }
    public ArrayList<TripItem> getTripListList(TripItem tripItem, String orderByColumn, boolean isDesc) {
        String orderBy = getOrderByString(orderByColumn, isDesc);

        String selection = null;
        String[] selectionArgs = null;

        if (tripItem != null) {
            selection = "";
            ArrayList<String> conditionList = new ArrayList<String>();

            if (tripItem.getamount() != -1 ) {
                selection += " AND " + TripItemEntry.COL_AMOUNT + " LIKE ?";
                conditionList.add("%" + tripItem.getamount() + "%");
            }

            if (tripItem.getContent() != null && !tripItem.getContent().trim().isEmpty()) {
                selection += " AND " + TripItemEntry.COL_CONTENT + " LIKE ?";
                conditionList.add("%" + tripItem.getContent() + "%");
            }

            if (tripItem.getDate() != null && !tripItem.getDate().trim().isEmpty()) {
                selection += " AND " + TripItemEntry.COL_DATE + " = ?";
                conditionList.add(tripItem.getDate());
            }

            if (tripItem.getTime() != null && !tripItem.getTime().trim().isEmpty()) {
                selection += " AND " + TripItemEntry.COL_TIME + " = ?";
                conditionList.add(tripItem.getTime());
            }

            if (tripItem.getType() != null && !tripItem.getType().trim().isEmpty()) {
                selection += " AND " + TripItemEntry.COL_TYPE + " = ?";
                conditionList.add(tripItem.getType());
            }


            if (tripItem.getTripId() != -1) {
                selection += " AND " + TripItemEntry.COL_Trip_ID + " = ?";
                conditionList.add(String.valueOf(tripItem.getTripId()));
            }

            if (!selection.trim().isEmpty()) {
                selection = selection.substring(5);
            }

            selectionArgs = conditionList.toArray(new String[conditionList.size()]);
        }

        return getTripListFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    public TripItem getTripListById(long id) {
        String selection = TripItemEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return getTripListFromDB(null, selection, selectionArgs, null, null, null).get(0);
    }

    public long updateTripList(TripItem tripItem) {
        ContentValues values = getTripListValues(tripItem);

        String selection = TripItemEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(tripItem.getId())};

        return dbWrite.update(TripItemEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public long deleteTripList(long id) {
        String selection = TripItemEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return dbWrite.delete(TripItemEntry.TABLE_NAME, selection, selectionArgs);
    }

    protected ContentValues getTripListValues(TripItem tripItem) {
        ContentValues values = new ContentValues();
        values.put(TripItemEntry.COL_AMOUNT, tripItem.getamount());
        values.put(TripItemEntry.COL_CONTENT, tripItem.getContent());
        values.put(TripItemEntry.COL_DATE, tripItem.getDate());
        values.put(TripItemEntry.COL_TIME, tripItem.getTime());
        values.put(TripItemEntry.COL_TYPE, tripItem.getType());
        values.put(TripItemEntry.COL_Trip_ID, tripItem.getTripId());

        return values;
    }

    protected ArrayList<TripItem> getTripListFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<TripItem> list = new ArrayList<>();

        Cursor cursor = dbRead.query(TripItemEntry.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        while (cursor.moveToNext()) {
            TripItem tripItemItem = new TripItem();

            tripItemItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(TripItemEntry.COL_ID)));
            tripItemItem.setamount(cursor.getLong(cursor.getColumnIndexOrThrow(TripItemEntry.COL_AMOUNT)));
            tripItemItem.setContent(cursor.getString(cursor.getColumnIndexOrThrow(TripItemEntry.COL_CONTENT)));
            tripItemItem.setDate(cursor.getString(cursor.getColumnIndexOrThrow(TripItemEntry.COL_DATE)));
            tripItemItem.setTime(cursor.getString(cursor.getColumnIndexOrThrow(TripItemEntry.COL_TIME)));
            tripItemItem.setType(cursor.getString(cursor.getColumnIndexOrThrow(TripItemEntry.COL_TYPE)));
            tripItemItem.setTripId(cursor.getLong(cursor.getColumnIndexOrThrow(TripItemEntry.COL_Trip_ID)));

            list.add(tripItemItem);
        }

        cursor.close();

        return list;
    }
}