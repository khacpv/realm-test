package com.oic.test.realm;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.oic.test.realm.utils.MyYAxisValueFormatter;
import com.oic.test.realm.utils.TestManager;

import java.util.ArrayList;

/**
 * Created by FRAMGIA\pham.van.khac on 11/05/2016.
 */
public class StatisticActivity extends AppCompatActivity {

    public static final int ID_SELECT_SQLITE = 0;
    public static final int ID_SELECT_REALM = 1;
    public static final int ID_INSERT_SQLITE = 2;
    public static final int ID_INSERT_REALM = 3;
    public static final int ID_UPDATE_SQLITE = 4;
    public static final int ID_UPDATE_REALM = 5;
    public static final int ID_DELETE_SQLITE = 6;
    public static final int ID_DELETE_REALM = 7;

    boolean isCalculating = false;

    protected BarChart mChart;
    private Typeface mTf;

    public static final int[] COLORFUL_COLORS = {
            Color.rgb(193, 37, 82), Color.rgb(193, 37, 82),
            Color.rgb(255, 102, 0), Color.rgb(255, 102, 0),
            Color.rgb(245, 199, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(106, 150, 31),
            Color.rgb(179, 100, 53), Color.rgb(179, 100, 53)
    };

    protected String[] optionTests = new String[]{
            "SELECT", "", "INSERT","", "UPDATE","", "DELETE", ""
    };

    YAxisValueFormatter custom = new MyYAxisValueFormatter();

    ProgressBar progressBar;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Realm vs Sqlite");
        setContentView(R.layout.activity_statistic);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        title = (TextView) findViewById(R.id.title);

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.animateY(2000);
        mChart.animateX(2000);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");
        mChart.setPinchZoom(true);
        mChart.setDrawGridBackground(false);
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(1);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend l = mChart.getLegend();
        l.setEnabled(false);

        setData(0,0,0,0,0,0,0,0);

        statisctic();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.statistic_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.statistic:
                statisctic();
                break;
        }
        return true;
    }

    private void statisctic(){
        if(isCalculating){
            Toast.makeText(this,"Calculating... please wait", Toast.LENGTH_LONG).show();
            return;
        }
        new AsyncTask<Void,Void,Void>(){

            TestManager testManager;

            @Override
            protected void onPreExecute() {
                setData(0,0,0,0,0,0,0,0);
                isCalculating = true;
                title.setText("Comparing Realm vs SQLite");
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                testManager = new TestManager(StatisticActivity.this);
                testManager.startInsert();
                publishProgress();
                testManager.startSelect();
                publishProgress();
                testManager.startUpdate();
                publishProgress();
                testManager.startDelete();
                publishProgress();
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                updateData();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                updateData();
                isCalculating = false;
                title.setText(String.format("Realm fasters ~%s times SQlite", testManager.getFaster()));
            }

            void updateData(){
                setData(
                        testManager.selectSql,testManager.selectRealm,
                        testManager.insertSql,testManager.insertRealm,
                        testManager.updateSql,testManager.updateRealm,
                        testManager.deleteSql,testManager.deleteRealm);
            }
        }.execute();
    }

    private void setData(long... datas) {
        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < optionTests.length; i++) {
            xVals.add(optionTests[i % optionTests.length]);
        }
        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        yVals1.add(new BarEntry(datas[ID_SELECT_SQLITE],ID_SELECT_SQLITE));
        yVals1.add(new BarEntry(datas[ID_SELECT_REALM],ID_SELECT_REALM));
        yVals1.add(new BarEntry(datas[ID_INSERT_SQLITE],ID_INSERT_SQLITE));
        yVals1.add(new BarEntry(datas[ID_INSERT_REALM],ID_INSERT_REALM));
        yVals1.add(new BarEntry(datas[ID_UPDATE_SQLITE],ID_UPDATE_SQLITE));
        yVals1.add(new BarEntry(datas[ID_UPDATE_REALM],ID_UPDATE_REALM));
        yVals1.add(new BarEntry(datas[ID_DELETE_SQLITE],ID_DELETE_SQLITE));
        yVals1.add(new BarEntry(datas[ID_DELETE_REALM],ID_DELETE_REALM));

        // DataSet
        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "Operations");
        set1.setBarSpacePercent(35f);
        set1.setColors(COLORFUL_COLORS);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        data.setValueTypeface(mTf);
        mChart.setData(data);
        mChart.getData().notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }
}
