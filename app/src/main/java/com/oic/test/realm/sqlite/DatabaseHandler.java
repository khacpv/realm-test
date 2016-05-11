package com.oic.test.realm.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.oic.test.realm.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FRAMGIA\pham.van.khac on 11/05/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHandler {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "testSqlite";

    private static final String TABLE_RECORDS = "records";

    private static final String KEY_ID = "id";
    private static final String KEY_VALUE = "value";

    SQLiteDatabase db;

    long startTime = System.currentTimeMillis();

    long endTime = System.currentTimeMillis();

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECORDS_TABLE = "CREATE TABLE " + TABLE_RECORDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_VALUE + " TEXT" + ")";
        db.execSQL(CREATE_RECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        onCreate(db);
    }

    @Override
    public void beginTransaction() {
        startTime = System.currentTimeMillis();
        Log.e("TAG","sqlite start");
        db = this.getWritableDatabase();
    }

    @Override
    public void closeTransaction() {
        db.close();
        endTime = System.currentTimeMillis();
    }

    public String printLog(String extraInfo){
        String log = "duration: "+getDuration()+"ms with "+extraInfo;
        Log.e("TAG", log);
        return log;
    }

    public long getDuration(){
        return endTime - startTime;
    }

    @Override
    public List<Record> select() {
        List<Record> result = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_RECORDS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Record contact = new Record(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
                result.add(contact);
            } while (cursor.moveToNext());
        }

        return result;
    }

    @Override
    public void insert(Record record) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, record.id);
        values.put(KEY_VALUE, record.value);

        db.insert(TABLE_RECORDS, null, values);
    }

    @Override
    public int update(Record record) {
        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, record.value);

        return db.update(TABLE_RECORDS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(record.id)});
    }

    @Override
    public void delete(Record record) {
        db.delete(TABLE_RECORDS, KEY_ID + " = ?",
                new String[]{String.valueOf(record.id)});
    }
}
