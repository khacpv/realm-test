package com.oic.test.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FRAMGIA\pham.van.khac on 11/05/2016.
 */
public class Record extends RealmObject{

    @PrimaryKey
    public int id = -1;

    public String value = "data";

    public Record(){

    }

    public Record(String data){
        this.value = data;
    }

    public Record(int id, String data){
        this.id = id;
        this.value = data;
    }
}
