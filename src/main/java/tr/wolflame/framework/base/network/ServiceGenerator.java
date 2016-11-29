package tr.wolflame.framework.base.network;

import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Response;
import tr.wolflame.framework.base.network.model.accesstoken.AccessTokenObject;
import tr.wolflame.framework.base.util.helper.OnTaskCompleted;

public class ServiceGenerator {


    public static <S> S createService(RestAdapter.Builder builder, Class<S> serviceClass) {
        return createService(builder, null, serviceClass);
    }

    public static <S> S createService(RestAdapter.Builder builder, final String authToken, Class<S> serviceClass) {
        if (!TextUtils.isEmpty(authToken)) {
            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("Authorization", authToken);
                }
            });
        }

        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }

    public static <S> S createService(RestAdapter.Builder builder, String username, String password, Class<S> serviceClass) {
        if (username != null && password != null) {
            /*// concatenate username and password with colon for authentication
            final String credentials = username + ":" + password;
            // create Base64 encode to string
            final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);*/

            final String basic = userAuthHeader(username, password);

            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("Authorization", basic);
                    request.addHeader("Accept", "application/json");
                }
            });
        }

        final RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }


    public static String responseToString(Response response) {
        final BufferedReader reader;
        StringBuilder sb = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

            String line;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    public static String userAuthHeader(String userName, String password) {
        return "Basic " + Base64.encodeToString(String.format("%s:%s", userName, password).getBytes(), Base64.NO_WRAP);
    }


    public static AccessTokenObject getRequestTokenObject() {
        AccessTokenObject mAccessTokenObject = new AccessTokenObject();

        mAccessTokenObject.setAppID(BaseNetworkClient.APP_ID);
        mAccessTokenObject.setUDID(BaseNetworkClient.UDID);
        mAccessTokenObject.setSecret(BaseNetworkClient.SECRET_KEY);

        return mAccessTokenObject;
    }


    public static <T> void successfulRequestAction(Gson gson, Class<T> cls, Response response, final OnTaskCompleted<T> onTaskCompleted) {

        final String responseString = ServiceGenerator.responseToString(response);

        final T resultObject = gson.fromJson(responseString, cls);

        onTaskCompleted.onTaskCompleted(resultObject);
        onTaskCompleted.onTaskCompleted(responseString);
    }
}