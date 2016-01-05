/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of "Trading Post"
 *
 * "Trading Post" is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.udeyrishi.androidelasticsearchdatamanager.jobs;

import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.Preconditions;
import com.udeyrishi.androidelasticsearchdatamanager.elasticsearchhelpers.ElasticSearchHelper;

import java.io.IOException;

/**
 * A {@link DataManagerJob} for performing the write data request. Creates a new object, or replaces
 * the existing one.
 * Created by rishi on 15-11-11.
 */
public class WriteDataJob extends DataManagerJob {

    private final String json;

    /**
     * Creates an instance of {@link WriteDataJob}.
     *
     * @param rootUrl The root URL to elastic search.
     * @param dataKey The {@link DataKey} for the object. The new object will be created at the location
     *                pointed by this key. Existing object will be replaced, if applicable.
     * @param json    The JSON string to be written.
     */
    public WriteDataJob(String rootUrl, DataKey dataKey, String json) {
        super(rootUrl, dataKey);
        this.json = Preconditions.checkNotNullOrWhitespace(json, "json");
    }

    /**
     * Calls the {@link ElasticSearchHelper#postJson(String, String)} operation using the JSON
     * and {@link DataKey} passed during construction.
     *
     * @throws IOException Thrown if the network request fails.
     */
    @Override
    public void onRun() throws IOException {
        new ElasticSearchHelper(rootUrl).postJson(json, getRequestSuffix());
    }
}
