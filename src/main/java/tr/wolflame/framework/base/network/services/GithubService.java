package tr.wolflame.framework.base.network.services;


import retrofit.ResponseCallback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by SADIK on 29/02/16.
 */
public interface GithubService {

    @GET("/repos/{owner}/{repo}/contributors")
    void contributors(
            @Path("owner") String owner,
            @Path("repo") String repo,
            ResponseCallback responseCallback);
}
