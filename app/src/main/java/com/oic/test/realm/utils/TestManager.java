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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FRAMGIA\pham.van.khac on 11/05/2016.
 */
public class TestManager {
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
        sqlite = new DatabaseHandler(context);
        realm = new RealmHandler(context);
    }

    public void startSelect(){
        // select
        {
            sqlite.beginTransaction();
            sqlite.select();
            sqlite.closeTransaction();
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
            sqlite.beginTransaction();
            for (Record record : initRecords()) {
                sqlite.insert(record);
            }
            sqlite.closeTransaction();
            insertSql = sqlite.getDuration();
        }

        // realm
        {
            realm.beginTransaction();
            List<Record> data = initRecords();
            for (Record record : data) {
                realm.insert(record);
            }
            realm.closeTransaction();
            insertRealm = realm.getDuration();
        }
    }

    public void startUpdate(){
        // update
        {
            sqlite.beginTransaction();
            List<Record> data = initRecords();
            for (Record record : data) {
                record.value += "1";
                sqlite.update(record);
            }
            sqlite.closeTransaction();
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
            sqlite.beginTransaction();
            for (Record record : initRecords()) {
                sqlite.delete(record);
            }
            sqlite.closeTransaction();
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
        List<Record> records = new ArrayList<>();
        for(int i=0;i<1000;i++){
            records.add(new Record(i,String.format("record item %d", i)));
        }
        return records;
    }
}
