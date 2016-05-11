package com.oic.test.realm.realm;

import com.oic.test.realm.model.Record;

import java.util.List;

/**
 * Created by FRAMGIA\pham.van.khac on 11/05/2016.
 */
public interface IRealmHandler {

    void beginTransaction();

    void closeTransaction();

    List<Record> select();

    void insert(Record record);

    int update(Record record);

    void delete(Record record);
}
