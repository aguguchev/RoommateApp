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
    public static ChoresDatabase choresDB;

    public static final String CHORES_TABLE_NAME = "chores";
    public static final String CHISTORY_TABLE_NAME = "chistory";
    public final int USER_ID;

    public static final String DATABASE_NAME = "RA_App.db";
    private static int DATABASE_VERSION = 2;

    public static final String ID = "ID";
    public static final String DESC = "Description";
    public static final String FREQ= "Frequency";
    public static final String LAST_COMPLETE = "LastComplete";
    public static final String VALUE = "Value";

    public static final String H_ID = "H_ID";
    public static final String H_USER_ID = "User_ID";
    public static final String H_DATE_COMPLETED = "Date_Completed";
    public static final String H_VALUE = "Value";
    //TODO:IMPLEMENT CHORE COMPLETION HISTORY TABLE

    public static final String F_ID = "F_ID";
    public static final String F_FREQ = "F_FREQ";
    public final String[] POSSIBLE_FREQS;

    public static synchronized ChoresDatabase getInstance(Context context){
        if (choresDB == null) {
            choresDB = new ChoresDatabase(context.getApplicationContext());
        }
        return choresDB;
    }

    private ChoresDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if(!BuildConfig.DEBUG) {
            POSSIBLE_FREQS = context.getResources().getStringArray(R.array.chores_frequencies);
            USER_ID = 117;//TODO:IMPLEMENT USER ID SYSTEM FOR PRODUCTION BUILD
        }
        else {
            POSSIBLE_FREQS = context.getResources().getStringArray(R.array.debug_chores_frequencies);
            USER_ID = context.getResources().getInteger(R.integer.debug_user_id);
        }
    }

    private void createTables(SQLiteDatabase sqLiteDatabase) {
        //query to create main chores table
        String qs = "CREATE TABLE " + CHORES_TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                DESC + " TEXT, " +
                FREQ + " INTEGER, " +
                LAST_COMPLETE + " REAL, " +
                VALUE + " INTEGER);";

        //query to create chistory table
        String ch = "CREATE TABLE " + CHISTORY_TABLE_NAME + "(" +
                H_ID + " INTEGER PRIMARY KEY, " +
                H_DATE_COMPLETED + " REAL, " +
                H_USER_ID + " INTEGER, " +
                H_VALUE + " INTEGER);";

        sqLiteDatabase.execSQL(qs);
        sqLiteDatabase.execSQL(ch);
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

    public Chore addChore(String desc, String freq, int val){
        long initialLC = Chore.computeLCMinusInterval(freq).getTimeInMillis() - 1;
        String sql = "INSERT INTO " + CHORES_TABLE_NAME + "(" + DESC + ", " + FREQ
                + ", " + LAST_COMPLETE + ", " + VALUE + ") VALUES (?, ?, ?, ?)";
        Object[] bindArgs = new Object[]{desc, freq, initialLC, val};
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
        Chore newChore = new Chore(c.getColChoresID(), initialLC, desc, freq, val);
        return newChore;
    }

    public void editChore(long choreID, String desc, String freq, long lastComplete, int val){
        String sql = "UPDATE " + CHORES_TABLE_NAME + " SET " +
                DESC + " = ?, " +
                FREQ + " = ?, " +
                LAST_COMPLETE + " = ?, " +
                VALUE + " = ? " +
                "WHERE " + ID + " = ?";
        Object[] bindArgs = new Object[]{desc, freq, lastComplete, choreID, val};
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

    public CHCursor getChoreHistory(){
        //fill chistory table with dummy values if desired
        addDummyDataToCHistory();

        SQLiteDatabase d = getReadableDatabase();
        CHCursor c = (CHCursor) d.rawQueryWithFactory(
                new CHCursor.CHCFactory(), CHCursor.QUERY, null, null);
        c.moveToFirst();
        return c;
    }

    private void addDummyDataToCHistory(){
        String sql = "INSERT INTO " + CHISTORY_TABLE_NAME + " (" + H_DATE_COMPLETED + ", " +
                H_USER_ID + ", " + H_VALUE + ") VALUES (0, 0, 1), (0, 0, 1), (0, 0, 1), (0, 0, 1)," +
                "(0, 0, 1), (0, 1, 5), (0, 1, 5), (0, 1, 5), (0, 1, 5), (0, 1, 5), (0, 2, 10);";
        try{
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e){
            Log.e("stddebug", e.toString());
        }
    }

    public void logCompletion(Chore c){
        String sql = "INSERT INTO " + CHISTORY_TABLE_NAME + " (" + H_DATE_COMPLETED + ", " +
                H_USER_ID + ", " + H_VALUE + ") VALUES (?, ?, ?)";
        Object[] bindArgs = new Object[]{c.getLastComplete().getTime(), USER_ID, c.getValue()};
        try{
            getWritableDatabase().execSQL(sql, bindArgs);
        } catch(SQLException e){
            Log.e("Error logging chore", e.toString());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        createTables(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldv, int newv){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CHORES_TABLE_NAME + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CHISTORY_TABLE_NAME + ";");
        createTables(sqLiteDatabase);
    }

    public static class ChoresCursor extends SQLiteCursor{
        private static final String QUERY = "SELECT " + ID + ", " + DESC + ", " + FREQ +
                ", " + LAST_COMPLETE + ", " + VALUE + " FROM " + CHORES_TABLE_NAME;
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
        public long getColChoresLastComplete(){ return getLong(getColumnIndexOrThrow(LAST_COMPLETE));}
        public int getColChoresVal(){return getInt(getColumnIndexOrThrow(VALUE));}

    }

    public static class CHCursor extends SQLiteCursor{
        private static final String QUERY = "SELECT " + H_ID + ", " + H_DATE_COMPLETED + ", " +
                H_USER_ID + ", " + H_VALUE + " FROM " + CHISTORY_TABLE_NAME;
        private CHCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable,
                             SQLiteQuery query){
            super(db, driver, editTable, query);
        }
        private static class CHCFactory implements SQLiteDatabase.CursorFactory{
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable,
                                    SQLiteQuery query){
                return new CHCursor(db, driver, editTable, query);
            }
        }
        public int getColCHID(){return getInt(getColumnIndexOrThrow(H_ID));}
        public long getColCHDateCompleted(){return getLong(getColumnIndexOrThrow(H_DATE_COMPLETED));}
        public int getColCHUserID(){return getInt(getColumnIndexOrThrow(H_USER_ID));}
        public int getColCHValue(){return getInt(getColumnIndexOrThrow(H_VALUE));}

    }
}
