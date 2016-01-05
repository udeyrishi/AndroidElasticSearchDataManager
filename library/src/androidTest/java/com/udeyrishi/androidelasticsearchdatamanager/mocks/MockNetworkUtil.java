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
