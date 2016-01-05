package com.udeyrishi.androidelasticsearchdatamanager;

import android.content.Context;

import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * A {@link JsonDataManager} that wraps around an inner {@link JsonDataManager} to keep a copy of
 * the data in a local cache using an {@link LocalDataManager}. Allows to perform some operations
 * when the inner manager's {@link JsonDataManager#isOperational()} returns false.
 * <p>
 * <p>
 * Allows to perform the
 * {@link DataManager#keyExists(DataKey)} and {@link DataManager#getData(DataKey, Type)} operations
 * when the innerManager's {@link JsonDataManager#isOperational()} returns false. In that case,
 * these operations will be performed on the local cache.
 * Created by rishi on 15-10-31.
 */
public class CachedDataManager extends JsonDataManager {
    private static long MAX_IN_MEMORY_CACHE_DURATION_MS = 3000;

    private static Object cacheLock = new Object();
    private static HashMap<DataKey, TimeObjectWrapper> inMemoryCache = new HashMap<>();

    private final LocalDataManager cachingDataManager;
    protected final JsonDataManager innerManager;

    /**
     * Creates an instance of the {@link CachedDataManager}.
     *
     * @param context The {@link Context} to be used for local file IO.
     * @param innerManager The inner {@link JsonDataManager} to be used.
     */
    public CachedDataManager(Context context, JsonDataManager innerManager) {
        super(Preconditions.checkNotNull(innerManager, "innerManager").jsonFormatter.getUseExplicitExposeAnnotation());
        this.innerManager = innerManager;
        this.cachingDataManager = new LocalDataManager(context, innerManager.jsonFormatter.getUseExplicitExposeAnnotation());
    }

    /**
     * If the inner manager is operational, then checks if the {@link DataKey} exists in the inner
     * manager. Else, checks the cache.
     *
     * @param key The {@link DataKey} to be looked up.
     * @return True, if the key is found, else false.
     * @throws IOException Thrown, if the communications fails with the storage media being used.
     */
    @Override
    public boolean keyExists(DataKey key) throws IOException, ServiceNotAvailableException {
        if (innerManager.isOperational()) {
            return innerManager.keyExists(key);
        }

        synchronized (cacheLock) {
            if (inMemoryCache.containsKey(key)) {
                return true;
            }
        }

        return cachingDataManager.keyExists(key);
    }

    /**
     * If the inner manager is operational, then calls {@link JsonDataManager#getData(DataKey, Type)}
     * on it, else calls it on the cache.
     *
     * @param key     The {@link DataKey} to be used for lookup.
     * @param typeOfT The {@link Type} of the object being retrieved.
     * @param <T>     The type of the object being retrieved. This type should match with the parameter
     *                "typeOfT", else a {@link ClassCastException} might be thrown.
     * @return The retrieved object.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    @Override
    public <T> T getData(DataKey key, Type typeOfT) throws IOException, DataKeyNotFoundException, ClassCastException, ServiceNotAvailableException {

        synchronized (cacheLock) {
            if (shouldGetFromInMemoryCache(key)) {
                return (T) inMemoryCache.get(key).getObject();
            }
        }

        if (innerManager.isOperational()) {
            T retrievedData = innerManager.getData(key, typeOfT);
            writeToCache(key, retrievedData, typeOfT);
            return retrievedData;
        }

        synchronized (cacheLock) {
            if (inMemoryCache.containsKey(key)) {
                return (T) inMemoryCache.get(key).getObject();
            } else {
                return cachingDataManager.getData(key, typeOfT);
            }
        }
    }

    /**
     * Writes the object to the inner manager, if it is operational.
     *
     * @param key     The {@link DataKey} for the object.
     * @param obj     The object to be stored.
     * @param typeOfT The {@link Type} of the object.
     * @param <T>     The type of the object.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    @Override
    public <T> void writeData(DataKey key, T obj, Type typeOfT) throws IOException, ServiceNotAvailableException {
        if (isOperational()) {
            innerManager.writeData(key, obj, typeOfT);
            writeToCache(key, obj, typeOfT);
        } else {
            throw new ServiceNotAvailableException("Inner DataManager is not operational. Cannot perform the write operation.");
        }
    }

    /**
     * Deletes the object from the inner manager and the cache, if the inner manager is operational.
     * Only deletes if the key is found.
     *
     * @param key The {@link DataKey} for which the object has to be deleted.
     * @return True, if the key is found and deleted. False, if the key is not found.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    @Override
    public void deleteIfExists(DataKey key) throws IOException, ServiceNotAvailableException {
        if (!isOperational()) {
            throw new ServiceNotAvailableException("Inner DataManager is not operational. Cannot perform the delete operation.");
        }

        innerManager.deleteIfExists(key);
        deleteFromCache(key);
    }

    /**
     * True, if the inner manager is operational, else false. In this case, isOperational == false
     * implies that only the write and delete operations are always completely non-operational.
     * The get and keyExists operations may still operate (partially) on the cache.
     *
     * @return True, if the inner manager is operational, else false.
     */
    @Override
    public boolean isOperational() {
        return innerManager.isOperational();
    }

    /**
     * Returns if the innerManager requires network or not.
     */
    @Override
    public boolean requiresNetwork() {
        return innerManager.requiresNetwork();
    }

    /**
     * Writes the object to the cache.
     * @param key The {@link DataKey} for the object that was passed. This will be converted to an
     *            appropriate caching key.
     * @param obj The object to be cached.
     * @param typeOfT The {@link Type} of the object.
     * @param <T> The type of the object.
     */
    protected <T> void writeToCache(DataKey key, T obj, Type typeOfT) {
        cachingDataManager.writeData(key, obj, typeOfT);
        synchronized (cacheLock) {
            inMemoryCache.put(key, new TimeObjectWrapper(obj));
        }
    }

    /**
     * Deletes the object from the cache, if it exists.
     * @param key The {@link DataKey} for the object that was passed. This will be converted to an
     *            appropriate caching key.
     */
    protected void deleteFromCache(DataKey key) {
        cachingDataManager.deleteIfExists(key);
        synchronized (cacheLock) {
            if (inMemoryCache.containsKey(key)) {
                inMemoryCache.remove(key);
            }
        }
    }

    private boolean shouldGetFromInMemoryCache(DataKey key) {
        if (inMemoryCache.containsKey(key)) {
            TimeObjectWrapper timeObjectWrapper = inMemoryCache.get(key);
            long difference = System.currentTimeMillis() - timeObjectWrapper.getDateCreatedEpochMillis();
            return difference <= MAX_IN_MEMORY_CACHE_DURATION_MS;
        } else {
            return false;
        }
    }

    private class TimeObjectWrapper {
        private final Object obj;
        private final long dateCreated;

        public TimeObjectWrapper(Object obj) {
            this.obj = obj;
            this.dateCreated = System.currentTimeMillis();
        }

        public Object getObject() {
            return obj;
        }

        public long getDateCreatedEpochMillis() {
            return dateCreated;
        }
    }
}