package com.udeyrishi.androidelasticsearchdatamanager.mocks;

import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.JsonDataManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;


/**
 * Created by rishi on 15-11-08.
 */
public class InMemoryDataManager extends JsonDataManager {

    private boolean isOperational = true;
    private HashMap<String, Object> inMemoryDataRepository = new HashMap<>();

    @Override
    public boolean keyExists(DataKey key) throws IOException {
        return inMemoryDataRepository.containsKey(key.toString());
    }

    @Override
    public <T> T getData(DataKey key, Type typeOfT) throws IOException {
        return (T) inMemoryDataRepository.get(key.toString());
    }

    @Override
    public <T> void writeData(DataKey key, T obj, Type typeOfT) throws IOException {
        inMemoryDataRepository.put(key.toString(), obj);
    }

    @Override
    public void deleteIfExists(DataKey key) throws IOException {
        inMemoryDataRepository.remove(key.toString());
    }

    @Override
    public boolean isOperational() {
        return isOperational;
    }

    @Override
    public boolean requiresNetwork() {
        return false;
    }

    public void setIsOperational(boolean isOperational) {
        this.isOperational = isOperational;
    }
}
