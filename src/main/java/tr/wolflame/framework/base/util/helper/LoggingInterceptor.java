package tr.wolflame.framework.base.util.helper;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import tr.wolflame.framework.base.util.LogApp;

public final class LoggingInterceptor implements Interceptor {
    private static final String TAG = LoggingInterceptor.class.getSimpleName();

    public LoggingInterceptor() {
    }

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        LogApp.d(TAG, String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        LogApp.d(TAG, String.format("Received response for %s in %.1fms%n%s", response.request().url(), (double) (t2 - t1) / 1000000.0D, response.headers()));
        return response;
    }
}