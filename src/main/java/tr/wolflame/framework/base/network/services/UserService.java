package tr.wolflame.framework.base.network.services;

import retrofit.Callback;
import retrofit.http.POST;

public interface UserService {
    @POST("/me")
    <T> void me(Callback<T> callback);
}