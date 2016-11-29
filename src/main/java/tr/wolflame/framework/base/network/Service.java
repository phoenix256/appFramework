package tr.wolflame.framework.base.network;

import java.util.Map;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import retrofit.mime.TypedInput;

/**
 * Created by SADIK on 29/06/15.
 */
public interface Service<T> {

    @GET("/webservice/xmlprep/reader/PrepareIssueListXML.aspx")
    T getMenuList(@Header("Authorization") String authorization,
                  @Query("pID") String pID,
                  @Query("rType") String rType);

    @GET("/webservice/xmlprep/reader/PrepareIssueListXML.aspx")
    void getMenuList(@Header("Authorization") String authorization,
                     @Query("pID") String pID,
                     @Query("rType") String rType,
                     Callback<T> response);

    @GET("/webservice/xmlprep/reader/PrepareIssueListXML.aspx")
    void getMenuList(@Header("Authorization") String authorization,
                     ResponseCallback response);


    @GET("/webservice/xmlprep/reader/PrepareIssueListXML.aspx")
    void getMenuListWithAuthHeader(@Header("Authorization") String authorization,
                                   @Query("pID") String pID,
                                   @Query("rType") String rType,
                                   ResponseCallback response);


    @GET("/webservice/xmlprep/reader/PrepareIssueListXML.aspx")
    void getMenuListWithQuery(@Header("Authorization") String authorization,
                              @Query("pID") String pID,
                              @Query("rType") String rType,
                              ResponseCallback response);


    @GET("/webservice/xmlprep/reader/PrepareIssueListXML.aspx")
    void getMenuListQueryMap(@Header("Authorization") String authorization,
                             @QueryMap Map<String, String> options,
                             ResponseCallback response);


    @POST("/webservice/xmlprep/reader/PrepareIssueListXML.aspx")
    void postRawJson(@Body TypedInput body,
                     ResponseCallback response);


}
