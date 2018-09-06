package com.android.roommate.roommateapp.payments;

import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;


public class PaymentsDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "payments.db";
    public static final String TABLE_PAYMENTS = "payments";
    public static final String COLUMN_DESC = "_desc";
    public static final String COLUMN_PRICE = "_price";

    public PaymentsDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public PaymentsDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PAYMENTS + " (" +
                COLUMN_DESC + " TEXT PRIMARY KEY, " +
                COLUMN_PRICE + " INTEGER " +
                ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS );
        onCreate(db);
    }

    public void addPayment(Payment curr){
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESC, curr.getitemName());
        values.put(COLUMN_PRICE, curr.getPrice());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PAYMENTS, null, values);
        db.close();
    }

    public void removePayment(String currDesc){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PAYMENTS + " WHERE " + COLUMN_DESC + " =\"" + currDesc + "\";");

    }

    //Finish implementing data persistence. Done: Database Handler. Not Done: Override insert and get methods, and utilise
    //in PaymentAdapter and PayMainActivity
    //Assign ID using AUTOINCREMENT

}

