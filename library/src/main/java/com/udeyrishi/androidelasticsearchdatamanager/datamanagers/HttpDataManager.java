package com.udeyrishi.androidelasticsearchdatamanager.datamanagers;

import android.content.Context;
import android.content.res.Resources;

import com.path.android.jobqueue.network.NetworkUtil;
import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.Preconditions;
import com.udeyrishi.androidelasticsearchdatamanager.elasticsearchhelpers.ElasticSearchHelper;
import com.udeyrishi.androidelasticsearchdatamanager.elasticsearchhelpers.ElasticSearchNetworkUtil;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.DataKeyNotFoundException;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * A {@link JsonDataManager} that uses the ElasticSearch server as the storage media.
 * Created by rishi on 15-10-30.
 */
public class HttpDataManager extends JsonDataManager {
    private final ElasticSearchHelper elasticSearchHelper;
    private final NetworkUtil networkUtil;
    private final Context context;

    /**
     * Creates an instance of {@link HttpDataManager}.
     *
     * @param context                     The {@link Context} to be used for network operations.
     * @param rootUrl                     The root URL to elastic search.
     * @param networkUtil                 The {@link NetworkUtil} to be used for checking the network state.
     * @param useExplicitExposeAnnotation True, if the @expose annotations are to be explicitly used,
     *                                    else false. If this is set to true, only the fields with
     *                                    the annotation @expose will be serialized/de-serialized.
     */
    public HttpDataManager(Context context, String rootUrl, NetworkUtil networkUtil, boolean useExplicitExposeAnnotation) {
        super(useExplicitExposeAnnotation);
        this.networkUtil = Preconditions.checkNotNull(networkUtil, "networkUtil");
        this.context = Preconditions.checkNotNull(context, "context");
        this.elasticSearchHelper = new ElasticSearchHelper(Preconditions.checkNotNullOrWhitespace(rootUrl, "rootUrl"));
    }

    /**
     * Creates an instance of the {@link HttpDataManager}. The manager will set the value of
     * "useExplicitExposeAnnotation" to false.
     *
     * @param context     The {@link Context} to be used for network operations.
     * @param rootUrl     The root URL to elastic search.
     * @param networkUtil The {@link NetworkUtil} to be used for checking the network state.
     */
    public HttpDataManager(Context context, String rootUrl, NetworkUtil networkUtil) {
        this(context, rootUrl, networkUtil, false);
    }

    /**
     * Creates an instance of the {@link HttpDataManager}. The manager will set the value of
     * "useExplicitExposeAnnotation" to false, and will use {@link ElasticSearchNetworkUtil} as the
     * {@link NetworkUtil}.
     *
     * @param context The {@link Context} to be used for network operations.
     * @param rootUrl The root URL to elastic search.
     */
    public HttpDataManager(Context context, String rootUrl) {
        this(context, rootUrl, new ElasticSearchNetworkUtil(context));
    }

    /**
     * Creates an instance of {@link HttpDataManager}. The manager will use {@link ElasticSearchNetworkUtil}
     * as the {@link NetworkUtil}.
     *
     * @param context                     The {@link Context} to be used for network operations.
     * @param rootUrl                     The root URL to elastic search.
     * @param useExplicitExposeAnnotation True, if the @expose annotations are to be explicitly used,
     *                                    else false. If this is set to true, only the fields with
     *                                    the annotation @expose will be serialized/de-serialized.
     */
    public HttpDataManager(Context context, String rootUrl, boolean useExplicitExposeAnnotation) {
        this(context, rootUrl, new ElasticSearchNetworkUtil(context), useExplicitExposeAnnotation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean keyExists(DataKey key) throws IOException, ServiceNotAvailableException {
        if (!isOperational()) {
            throw new ServiceNotAvailableException("HttpDataManager is not operational. Cannot perform this operation.");
        }

        return elasticSearchHelper.checkPathExists(key.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getData(DataKey key, Type typeOfT) throws IOException, ServiceNotAvailableException {
        if (!isOperational()) {
            throw new ServiceNotAvailableException("HttpDataManager is not operational. Cannot perform this operation.");
        }

        try {
            return deserialize(elasticSearchHelper.getJson(key.toString()), typeOfT);
        } catch (Resources.NotFoundException e) {
            throw new DataKeyNotFoundException(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void writeData(DataKey key, T obj, Type typeOfT) throws IOException, ServiceNotAvailableException {
        if (!isOperational()) {
            throw new ServiceNotAvailableException("HttpDataManager is not operational. Cannot perform this operation.");
        }

        elasticSearchHelper.postJson(serialize(obj, typeOfT), key.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteIfExists(DataKey key) throws IOException, ServiceNotAvailableException {
        if (!isOperational()) {
            throw new ServiceNotAvailableException("HttpDataManager is not operational. Cannot perform this operation.");
        }

        elasticSearchHelper.sendDeleteRequestAtPath(key.toString());
    }

    /**
     * True, if the phone is online, else false.
     *
     * @return True, if the phone is online, else false.
     */
    @Override
    public boolean isOperational() {
        return networkUtil.isConnected(context);
    }

    /**
     * Returns true.
     */
    @Override
    public boolean requiresNetwork() {
        return true;
    }
}

