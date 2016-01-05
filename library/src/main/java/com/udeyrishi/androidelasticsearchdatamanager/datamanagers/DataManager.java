/**
 Copyright 2016 Udey Rishi
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.udeyrishi.androidelasticsearchdatamanager.datamanagers;

import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * A base interface for a class acting as a data manager for arbitrary types of objects.
 * The implementations work with {@link DataKey} to uniquely map the objects to paths on whatever
 * storage media they are using.
 * Created by rishi on 15-10-29.
 */
public interface DataManager {
    /**
     * Checks if the {@link DataKey} exists in the storage media.
     *
     * @param key The {@link DataKey} to be looked up.
     * @return True, if the key exists, else false.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    boolean keyExists(DataKey key) throws IOException, ServiceNotAvailableException;

    /**
     * Retrieves the data pointed to by the key from the storage media.
     *
     * @param key     The {@link DataKey} to be used for lookup.
     * @param typeOfT The {@link Type} of the object being retrieved.
     * @param <T>     The type of the object being retrieved. This type should match with the parameter
     *                "typeOfT", else a {@link ClassCastException} might be thrown.
     * @return The retrieved object.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    <T> T getData(DataKey key, Type typeOfT) throws IOException, ServiceNotAvailableException;

    /**
     * Write the object to the storage, or overwrites the existing one if one existed.
     *
     * @param key     The {@link DataKey} for the object.
     * @param obj     The object to be stored.
     * @param typeOfT The {@link Type} of the object.
     * @param <T>     The type of the object.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    <T> void writeData(DataKey key, T obj, Type typeOfT) throws IOException, ServiceNotAvailableException;

    /**
     * Deletes the object pointed by the {@link DataKey} from the storage media, if it exists.
     *
     * @param key The {@link DataKey} for which the object has to be deleted.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    void deleteIfExists(DataKey key) throws IOException, ServiceNotAvailableException;

    /**
     * Checks if the storage media used by this {@link DataManager} implementation is currently
     * operational or online.
     *
     * @return True, if the {@link DataManager} is completely operational, else false. Some
     * operations might still work if false is returned.
     */
    boolean isOperational();

    /**
     * Tells if this DataManager requires network or not.
     *
     * @return True, if network is needed, else false.
     */
    boolean requiresNetwork();
}
