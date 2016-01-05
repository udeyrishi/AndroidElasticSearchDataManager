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

package com.udeyrishi.androidelasticsearchdatamanager;

import com.udeyrishi.androidelasticsearchdatamanager.datamanagers.DataManager;

/**
 * A class that acts as the key mapping to an object being stored by a {@link DataManager}.
 * Behaviourally, this key should correspond to the path where an object is stored. The
 * particulars of the correspondence may be determined by the implementations of {@link DataManager}.
 * Created by rishi on 15-10-29.
 */
public class DataKey {
    private String type;
    private String id;

    /**
     * Creates a new instance of {@link DataKey}
     *
     * @param type A string describing the type of object being stored using this {@link DataKey}.
     * @param id   A string describing the unique ID of the object.
     * @throws IllegalArgumentException Thrown, if either of the parameters are null or whitespace.
     */
    public DataKey(String type, String id) throws IllegalArgumentException {
        setType(type);
        setId(id);
    }

    /**
     * Gets the ID for the {@link DataKey}.
     *
     * @return The ID for the {@link DataKey}.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID for the {@link DataKey}.
     *
     * @param id The new ID.
     * @throws IllegalArgumentException Thrown, if the new ID is either null or whitespace.
     */
    public void setId(String id) throws IllegalArgumentException {
        this.id = Preconditions.checkNotNullOrWhitespace(id, "id");
    }

    /**
     * Gets the object type for this {@link DataKey}.
     *
     * @return The object type for this {@link DataKey}.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the object type for this {@link DataKey}.
     *
     * @param type The new object type.
     * @throws IllegalArgumentException Thrown, if the new type is either null or whitespace.
     */
    public void setType(String type) throws IllegalArgumentException {
        this.type = Preconditions.checkNotNullOrWhitespace(type, "type");
    }

    /**
     * Returns a string representation of the {@link DataKey} as a path in the format: type/ID
     *
     * @return The path representation of the {@link DataKey}.
     */
    @Override
    public String toString() {
        return String.format("%s/%s", getType(), getId());
    }


    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof DataKey)) {
            return false;
        }

        DataKey rhs = (DataKey) o;
        return rhs.type.equals(this.type) && rhs.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode() ^ type.hashCode();
    }
}
