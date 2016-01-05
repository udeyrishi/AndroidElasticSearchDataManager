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

package com.udeyrishi.androidelasticsearchdatamanager.mocks;

import android.content.Context;

import com.path.android.jobqueue.network.NetworkEventProvider;
import com.path.android.jobqueue.network.NetworkUtil;

/**
 * Created by rishi on 15-11-12.
 */
public class MockNetworkUtil implements NetworkUtil, NetworkEventProvider {

    private Listener listener;
    private boolean networkState;

    public MockNetworkUtil() {
        this.listener = null;
        this.networkState = true;
    }

    public void setNetworkState(boolean networkState) {
        this.networkState = networkState;
        if (listener != null) {
            listener.onNetworkChange(this.networkState);
        }
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public boolean isConnected(Context context) {
        return networkState;
    }
}
