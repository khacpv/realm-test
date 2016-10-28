package com.oic.test.realm.utils;

import android.content.Context;
import com.oic.test.realm.model.Record;
import com.oic.test.realm.realm.RealmHandler;
import com.oic.test.realm.sqlite.DatabaseHandler;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FRAMGIA\pham.van.khac on 11/05/2016.
 */
public class TestManager {

    public static final long TEST_NO_TRANS = 1000;

    public static final long TEST_WITH_TRANS = 10000;
    private DatabaseHandler sqlite;
    private RealmHandler realm;

    public long insertSql;
    public long insertRealm;
    public long selectSql;
    public long selectRealm;
    public long updateSql;
    public long updateRealm;
    public long deleteSql;
    public long deleteRealm;

    public TestManager(Context context){
        sqlite = new DatabaseHandler(context);
        realm = new RealmHandler(context);
    }

    public void useSQLiteTransaction(boolean used){
        sqlite.setRunWithTransaction(used);
    }

    public void startSelect(){
        // select
        {
            sqlite.openDatabase();
            sqlite.beginTransaction();
            sqlite.select();
            sqlite.closeTransaction();
            sqlite.closeDatabase();
            selectSql = sqlite.getDuration();
        }

        // realm
        {
            realm.beginTransaction();
            realm.select();
            realm.closeTransaction();
            selectRealm = realm.getDuration();
        }
    }

    public void startInsert(){
        // insert
        {
            sqlite.openDatabase();
            sqlite.beginTransaction();
            for (Record record : initRecords()) {
                sqlite.insert(record);
            }
            sqlite.closeTransaction();
            sqlite.closeDatabase();
            insertSql = sqlite.getDuration();
        }

        // realm
        {
            realm.beginTransaction();
            List<Record> data = initRecords();
            for (Record record : data) {
                TestManager.this.realm.insert(record);
            }
            realm.closeTransaction();

            insertRealm = realm.getDuration();
        }
    }

    public void startUpdate(){
        // update
        {
            sqlite.openDatabase();
            sqlite.beginTransaction();
            List<Record> data = initRecords();
            for (Record record : data) {
                record.value += "1";
                sqlite.update(record);
            }
            sqlite.closeTransaction();
            sqlite.closeDatabase();
            updateSql = sqlite.getDuration();
        }

        // realm
        {
            realm.beginTransaction();
            List<Record> data = initRecords();
            for (Record record : data) {
                record.value += "1";
                realm.update(record);
            }
            realm.closeTransaction();
            updateRealm = realm.getDuration();
        }
    }

    public void startDelete(){
        // delete
        {
            sqlite.openDatabase();
            sqlite.beginTransaction();
            for (Record record : initRecords()) {
                sqlite.delete(record);
            }
            sqlite.closeTransaction();
            sqlite.closeDatabase();
            deleteSql = sqlite.getDuration();
        }

        // realm
        {
            realm.beginTransaction();
            List<Record> data = initRecords();
            for (Record record : data) {
                realm.delete(record);
            }
            realm.closeTransaction();
            deleteRealm = realm.getDuration();
        }
    }

    public float getFaster(){
        float fasterS = (float) selectSql / (float) selectRealm;
        float fasterC = (float) insertSql / (float) insertRealm;
        float fasterU = (float) updateSql / (float) updateRealm;
        float fasterD = (float) deleteSql / (float) deleteRealm;
        return (fasterS + fasterC + fasterU + fasterD) / 4;
    }

    public String getFasterStr(){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(getFaster());
    }

    public static List<Record> initRecords(){
        int totalItem = (int) TEST_NO_TRANS;
        if(DatabaseHandler.hasTransaction){
            totalItem = (int) TEST_WITH_TRANS;
        }
        List<Record> records = new ArrayList<>();
        for(int i=0;i<totalItem;i++){
            records.add(new Record(i,String.format("record item %d", i)));
        }
        return records;
    }

    public void onDestroy() {
        realm.onDestroy();
    }
}
