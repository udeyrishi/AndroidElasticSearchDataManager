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

package com.udeyrishi.androidelasticsearchdatamanager.elasticsearchhelpers;

import android.content.Context;

import com.path.android.jobqueue.network.NetworkEventProvider;
import com.path.android.jobqueue.network.NetworkUtil;
import com.path.android.jobqueue.network.NetworkUtilImpl;
import com.udeyrishi.androidelasticsearchdatamanager.Preconditions;

/**
 * A {@link NetworkUtil} and {@link NetworkEventProvider} implementation for checking if the
 * Elastic Search server can be contacted.
 * Created by rishi on 15-11-12.
 */
public class ElasticSearchNetworkUtil implements NetworkUtil, NetworkEventProvider {

    private final NetworkUtilImpl networkUtil;

    /**
     * Creates an instance of {@link ElasticSearchNetworkUtil}.
     *
     * @param context The {@link Context} to be used to network operations.
     */
    public ElasticSearchNetworkUtil(Context context) {
        this.networkUtil = new NetworkUtilImpl(Preconditions.checkNotNull(context, "context"));
    }

    /**
     * Sets a listener for the network changed events.
     *
     * @param listener A {@link com.path.android.jobqueue.network.NetworkEventProvider.Listener}.
     */
    @Override
    public void setListener(Listener listener) {
        networkUtil.setListener(listener);
    }

    /**
     * Tells whether there is internet access available or not.
     *
     * @param context The context to be used for checking the network connection.
     * @return True, if internet connection is available, else false.
     */
    @Override
    public boolean isConnected(Context context) {
        return networkUtil.isConnected(context);
    }
}
