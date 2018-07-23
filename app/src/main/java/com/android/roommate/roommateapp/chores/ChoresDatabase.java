package com.android.roommate.roommateapp.chores;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import com.android.roommate.roommateapp.BuildConfig;
import com.android.roommate.roommateapp.R;

public class ChoresDatabase extends SQLiteOpenHelper {
    public static final String CHORES_TABLE_NAME = "chores";
    public static final String FREQS_TABLE_NAME = "frequencies";

    public static final String DATABASE_NAME = "RA_App.db";
    private static int DATABASE_VERSION = 2;

    public static final String ID = "ID";
    public static final String DESC = "Description";
    public static final String FREQ= "Frequency";
    public static final String LAST_COMPLETE = "LastComplete";

    public static final String F_ID = "F_ID";
    public static final String F_FREQ = "F_FREQ";
    public final String[] POSSIBLE_FREQS;

    public ChoresDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if(!BuildConfig.DEBUG)
            POSSIBLE_FREQS = context.getResources().getStringArray(R.array.chores_frequencies);
        else
            POSSIBLE_FREQS = context.getResources().getStringArray(R.array.debug_chores_frequencies);
    }

    private void createTable(SQLiteDatabase sqLiteDatabase) {
        //query to create main chores table
        String qs = "CREATE TABLE " + CHORES_TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                DESC + " TEXT, " +
                FREQ + " INTEGER, " +
                LAST_COMPLETE + " REAL);";

        sqLiteDatabase.execSQL(qs);
    }

    public ChoresCursor getChores(){
        //fashions query that joins appropriate freqs table and sorts by freq
        String query = ChoresCursor.QUERY + " ORDER BY " + LAST_COMPLETE + " ASC;";
        SQLiteDatabase d = getReadableDatabase();
        ChoresCursor c = (ChoresCursor) d.rawQueryWithFactory(
                new ChoresCursor.CCFactory(), query, null, null);
        c.moveToFirst();
        return c;
    }

    public Chore addChore(String desc, String freq){
        long initialLC = Chore.computeLCMinusInterval(freq).getTimeInMillis() - 1;
        String sql = "INSERT INTO " + CHORES_TABLE_NAME + "(" + DESC + ", " + FREQ
                + ", " + LAST_COMPLETE + ") VALUES (?, ?, ?)";
        Object[] bindArgs = new Object[]{desc, freq, initialLC};
        try {
            getWritableDatabase().execSQL(sql, bindArgs);
            Log.d("dbPersistence", ": " + desc);
        } catch(SQLException e){
            Log.e("Error writing new chore", e.toString());
        }

        //retrieve assigned id from db
        String ret = "SELECT * FROM " + CHORES_TABLE_NAME + " WHERE " + DESC + " = \'" + desc +
                "\' ORDER BY " + LAST_COMPLETE + " DESC";
        SQLiteDatabase d = getReadableDatabase();
        ChoresCursor c = (ChoresCursor) d.rawQueryWithFactory(
                new ChoresCursor.CCFactory(), ret, null, null);
        c.moveToFirst();
        return new Chore(c.getColChoresID(), initialLC, desc, freq);
    }

    public void editChore(long choreID, String desc, String freq, long lastComplete){
        String sql = "UPDATE " + CHORES_TABLE_NAME + " SET " +
                DESC + " = ?, " +
                FREQ + " = ?, " +
                LAST_COMPLETE + " = ? " +
                "WHERE " + ID + " = ?";
        Object[] bindArgs = new Object[]{desc, freq, lastComplete, choreID};
        try{
            getWritableDatabase().execSQL(sql, bindArgs);
        }catch (SQLException e){
            Log.e("Error updating chore", e.toString());
        }
    }

    public void deleteChore(long choreID){
        String sql = "DELETE FROM " + CHORES_TABLE_NAME + " WHERE " + ID + " = ?";
        Object[] bindArgs = new Object[]{choreID};
        try{
            getWritableDatabase().execSQL(sql, bindArgs);
        }catch (SQLException e){
            Log.e("Error deleting chore", e.toString());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        createTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldv, int newv){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CHORES_TABLE_NAME + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FREQS_TABLE_NAME + ";");
        createTable(sqLiteDatabase);
    }

    public static class ChoresCursor extends SQLiteCursor{
        private static final String QUERY = "SELECT " + ID + ", " + DESC + ", " + FREQ +
                ", " + LAST_COMPLETE + " FROM " + CHORES_TABLE_NAME;
        private static final String QUERYWITHJOIN = "SELECT " + ID + ", " + DESC + ", " + F_FREQ +
                ", " + LAST_COMPLETE +
                " FROM " + CHORES_TABLE_NAME +
                " INNER JOIN " + FREQS_TABLE_NAME +
                " ON " + CHORES_TABLE_NAME + "." + FREQ + " = " + FREQS_TABLE_NAME + "." + F_ID +
                " ORDER BY " + F_FREQ + ";";
        private ChoresCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable,
                             SQLiteQuery query){
            super(db, driver, editTable, query);
        }
        private static class CCFactory implements SQLiteDatabase.CursorFactory{
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable,
                                    SQLiteQuery query){
                return new ChoresCursor(db, driver, editTable, query);
            }
        }
        public int getColChoresID(){
            return getInt(getColumnIndexOrThrow(ID));
        }
        public String getColChoresDesc(){
            return getString(getColumnIndexOrThrow(DESC));
        }
        public String getColChoresFreq(){
            return getString(getColumnIndexOrThrow(FREQ));
        }
        public long getColChoresLastComplete(){
            return getLong(getColumnIndexOrThrow(LAST_COMPLETE));
        }

    }
}
