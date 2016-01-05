package com.udeyrishi.androidelasticsearchdatamanager;

import android.content.res.Resources;

/**
 * Subclass of {@link android.content.res.Resources.NotFoundException} thrown when a particular
 * {@link DataKey} is not found by a {@link DataManager}.
 * Created by rishi on 15-10-30.
 */
public class DataKeyNotFoundException extends Resources.NotFoundException {
    /**
     * Creates a new instance of {@link DataKeyNotFoundException}.
     */
    public DataKeyNotFoundException() {
        super();
    }

    /**
     * Creates a new instance of {@link DataKeyNotFoundException}.
     *
     * @param key The {@link DataKey} not found.
     */
    public DataKeyNotFoundException(DataKey key) {
        super(key.toString());
    }
}
