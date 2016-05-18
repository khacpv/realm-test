package com.oic.test.realm.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.oic.test.realm.R;
import com.oic.test.realm.adapter.RecordAdapter;
import com.oic.test.realm.model.Record;
import com.oic.test.realm.realm.RealmHandler;
import com.oic.test.realm.sqlite.DatabaseHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.internal.SharedGroup;
import io.realm.internal.Table;
import io.realm.internal.WriteTransaction;

/**
 * Created by FRAMGIA\pham.van.khac on 11/05/2016.
 */
public class TestManager {
    Context context;
    DatabaseHandler sqlite;
    RealmHandler realm;

    public long insertSql;
    public long insertRealm;
    public long selectSql;
    public long selectRealm;
    public long updateSql;
    public long updateRealm;
    public long deleteSql;
    public long deleteRealm;

    public TestManager(Context context){
        this.context = context;
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
        return (selectSql+insertSql+updateSql+deleteSql)/(selectRealm+insertRealm+updateRealm+deleteRealm);
    }

    public static List<Record> initRecords(){
        int totalItem = 1000;
        if(DatabaseHandler.hasTransaction){
            totalItem = 10000;
        }
        List<Record> records = new ArrayList<>();
        for(int i=0;i<totalItem;i++){
            records.add(new Record(i,String.format("record item %d", i)));
        }
        return records;
    }
}
