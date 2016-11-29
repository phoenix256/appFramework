package tr.wolflame.framework.base.network.services;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import tr.wolflame.framework.base.network.model.accesstoken.AccessTokenObject;
import tr.wolflame.framework.base.network.model.extras.ResponseExtras;
import tr.wolflame.framework.base.network.model.issueList.ResponseIssueList;
import tr.wolflame.framework.base.network.model.issueList.ResponseMainScreenIssues;

/**
 * Created by SADIK on 29/06/15.
 */
public interface NewspaperService {


    @POST("/Services")
    void getAccessToken(@Body AccessTokenObject mAccessTokenObject,
                        Callback<AccessTokenObject> response);


    @GET("/Services/IssuePackages")
    void getIssueList(@Query("AccessToken") String AccessToken,
                      @Query("PublicationID") String PublicationID,
                      Callback<ResponseIssueList> response);


    @GET("/Services/IssuePackages")
    void getIssueList(@Query("AccessToken") String AccessToken,
                      @Query("PublicationID") String PublicationID,
                      @Query("PageIndex") String PageIndex,
                      @Query("PageSize") String PageSize,
                      ResponseCallback response);


    @GET("/Services/Supplements")
    void getIssueExtras(@Query("AccessToken") String AccessToken,
                        @Query("IssuePackageID") String IssuePackageID,
                        Callback<ResponseExtras> response);


    @GET("/Services/Today")
    void getMainScreenIssues(@Query("AccessToken") String AccessToken,
                             @Query("PublicationID") String PublicationID,
                             Callback<ResponseMainScreenIssues> response);
}
