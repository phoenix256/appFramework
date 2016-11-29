package tr.wolflame.framework.base.network;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import tr.wolflame.framework.base.util.helper.LoggingInterceptor;

/**
 * Created by SADIK on 29/02/16.
 */
public class RestBuilderGenerator {

    public static final String RETROFIT_TOO_MUCH_REQUEST_CODE = "429";

    private static final String API_BASE_URL = "http://publisher.milliyet.com.tr";

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(API_BASE_URL)
            .setClient(new OkClient(createOkHttpClient()));

    public static RestAdapter.Builder getBaseBuilder() {
        return builder;
    }

    private static OkHttpClient createOkHttpClient() {
        final OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new LoggingInterceptor());
        return client;
    }

    public static RestAdapter.Builder getCustomBuilder(String apiBaseUrl) {

        return new RestAdapter.Builder()
                .setEndpoint(apiBaseUrl)
                .setClient(new OkClient(createOkHttpClient()));
    }

}
