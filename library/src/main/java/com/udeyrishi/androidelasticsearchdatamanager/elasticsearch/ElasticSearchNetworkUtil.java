package com.udeyrishi.androidelasticsearchdatamanager.elasticsearch;

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
     * @param listener A {@link com.path.android.jobqueue.network.NetworkEventProvider.Listener}.
     */
    @Override
    public void setListener(Listener listener) {
        networkUtil.setListener(listener);
    }

    /**
     * Tells whether there is internet access available or not.
     * @param context The context to be used for checking the network connection.
     * @return True, if internet connection is available, else false.
     */
    @Override
    public boolean isConnected(Context context) {
        return networkUtil.isConnected(context);
    }
}
