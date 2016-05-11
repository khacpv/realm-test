package com.oic.test.realm.realm;

import android.content.Context;
import android.util.Log;

import com.oic.test.realm.model.Record;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by FRAMGIA\pham.van.khac on 11/05/2016.
 */
public class RealmHandler implements IRealmHandler{

    static Realm realm;

    long startTime = System.currentTimeMillis();

    long endTime = System.currentTimeMillis();

    public RealmHandler(Context context){
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context)
                .name("myrealm.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getInstance(realmConfig);
    }

    @Override
    public void beginTransaction() {
        realm.beginTransaction();
        startTime = System.currentTimeMillis();
        Log.e("TAG","realm start");
    }

    @Override
    public void closeTransaction() {
        realm.commitTransaction();
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
        RealmResults<Record> result = realm.where(Record.class).findAll();
        return result;
    }

    @Override
    public void insert(Record record) {
        realm.copyToRealmOrUpdate(record);
    }

    @Override
    public int update(Record record) {
        realm.copyToRealmOrUpdate(record);
        return 0;
    }

    @Override
    public void delete(Record record) {
        Record realmRecord = realm.copyToRealmOrUpdate(record);
        realmRecord.removeFromRealm();
    }
}
