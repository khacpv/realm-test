package com.oic.test.realm.realm;

import android.content.Context;
import android.util.Log;
import com.oic.test.realm.model.Record;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.List;

/**
 * Created by FRAMGIA\pham.van.khac on 11/05/2016.
 */
public class RealmHandler implements IRealmHandler{

    private Realm realm;

    private long startTime = System.currentTimeMillis();

    private long endTime = System.currentTimeMillis();

    private Context context;

    public RealmHandler(Context context){
        this.context = context;
        initRealm();
    }

    public Realm getRealm(){
        return realm;
    }

    private void initRealm(){
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void beginTransaction() {
        try {
            realm.beginTransaction();
        }catch (IllegalStateException e){
            initRealm();
            realm.beginTransaction();
        }
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
        return realm.where(Record.class).findAll();
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
        realmRecord.deleteFromRealm();
    }

    @Override
    public void onDestroy() {
        realm.close();
    }
}
