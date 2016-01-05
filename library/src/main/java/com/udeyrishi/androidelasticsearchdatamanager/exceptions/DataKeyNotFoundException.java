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

package com.udeyrishi.androidelasticsearchdatamanager.exceptions;

import android.content.res.Resources;

import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.datamanagers.DataManager;

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
