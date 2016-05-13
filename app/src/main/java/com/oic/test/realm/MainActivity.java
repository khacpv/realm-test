package com.oic.test.realm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.oic.test.realm.adapter.RecordAdapter;
import com.oic.test.realm.model.Record;
import com.oic.test.realm.realm.RealmHandler;
import com.oic.test.realm.sqlite.DatabaseHandler;
import com.oic.test.realm.utils.TestManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean isRealm = true;

    private ArrayList<Record> records = new ArrayList<>();

    DatabaseHandler sqlite;
    RealmHandler realm;

    TextView log;

    ListView listData;

    RecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log = (TextView)findViewById(R.id.log);
        listData = (ListView) findViewById(R.id.listdata);

        sqlite = new DatabaseHandler(this);
        realm = new RealmHandler(this);

        adapter = new RecordAdapter(this);
        adapter.setData(records);
        listData.setAdapter(adapter);

        for(int i=0;i<1000;i++){
            records.add(new Record(i,String.format("record item %d", i)));
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.statistic:
                startActivity(new Intent(this,StatisticActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshUI(){
        if(isRealm){
            realm.beginTransaction();
            List<Record> result = realm.select();
            realm.closeTransaction();

            records.clear();
            for(Record record: result){
                records.add(record);
            }
            adapter.notifyDataSetChanged();
            return;
        }
        sqlite.openDatabase();
        sqlite.beginTransaction();
        List<Record> result = sqlite.select();
        sqlite.closeTransaction();
        sqlite.closeDatabase();

        records.clear();
        for(Record record: result){
            records.add(record);
        }
        adapter.notifyDataSetChanged();
    }

    public void selectTest(View button){
        Log.e("Tag","Select records");
        if(isRealm){
            realm.beginTransaction();
            List<Record> result = realm.select();
            realm.closeTransaction();
            log.setText(realm.printLog("select "+result.size()+" items"));

            records.clear();
            for(Record record: result){
                records.add(record);
            }
            adapter.notifyDataSetChanged();
            return;
        }

        sqlite.openDatabase();
        sqlite.beginTransaction();
        List<Record> result = sqlite.select();
        sqlite.closeTransaction();
        sqlite.closeDatabase();
        log.setText(sqlite.printLog("select "+result.size()+" items"));

        records.clear();
        for(Record record: result){
            records.add(record);
        }
        adapter.notifyDataSetChanged();
    }

    public void insertTest(View button){
        Log.e("Tag","Insert records");
        if(isRealm){
            realm.beginTransaction();
            List<Record> data = TestManager.initRecords();
            for(Record record: data){
                realm.insert(record);
            }
            realm.closeTransaction();
            log.setText(realm.printLog("insert "+data.size()+" items"));
            refreshUI();
            return;
        }

        sqlite.openDatabase();
        sqlite.beginTransaction();
        List<Record> data = TestManager.initRecords();
        for(Record record: data){
            sqlite.insert(record);
        }
        sqlite.closeTransaction();
        sqlite.closeDatabase();
        log.setText(sqlite.printLog("insert "+data.size()+" items"));
        refreshUI();
    }

    public void updateTest(View button){
        Log.e("Tag","Update records");
        if(isRealm){
            realm.beginTransaction();
            List<Record> data = TestManager.initRecords();
            for(Record record: data){
                record.value += "1";
                realm.update(record);
            }
            realm.closeTransaction();
            log.setText(realm.printLog("update "+data.size()+" items"));
            refreshUI();
            return;
        }

        sqlite.openDatabase();
        sqlite.beginTransaction();
        List<Record> data = TestManager.initRecords();
        for(Record record: data){
            record.value += "1";
            sqlite.update(record);
        }
        sqlite.closeTransaction();
        sqlite.closeDatabase();
        log.setText(sqlite.printLog("update "+data.size()+" items"));
        refreshUI();
    }

    public void deleteTest(View button){
        Log.e("Tag","Delete records");
        if(isRealm){
            realm.beginTransaction();
            List<Record> data = TestManager.initRecords();
            for(Record record: data){
                realm.delete(record);
            }
            realm.closeTransaction();
            log.setText(realm.printLog("delete "+data.size()+" items"));
            refreshUI();
            return;
        }

        sqlite.openDatabase();
        sqlite.beginTransaction();
        for(Record record: TestManager.initRecords()){
            sqlite.delete(record);
        }
        sqlite.closeTransaction();
        sqlite.closeDatabase();
        log.setText(sqlite.printLog("delete "+records.size()+" items"));
        refreshUI();
    }

    public void setRunMode(View button){
        isRealm = button.getId() == R.id.isRealm;
    }
}
