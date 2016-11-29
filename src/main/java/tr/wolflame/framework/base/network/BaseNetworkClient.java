package tr.wolflame.framework.base.network;


import com.google.gson.Gson;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tr.wolflame.framework.base.network.model.accesstoken.AccessTokenObject;
import tr.wolflame.framework.base.network.services.GithubService;
import tr.wolflame.framework.base.network.services.NewspaperService;
import tr.wolflame.framework.base.network.services.UserService;
import tr.wolflame.framework.base.util.LogApp;
import tr.wolflame.framework.base.util.helper.OnTaskCompleted;

/**
 * Created by SADIK on 29/06/15.
 */
public class BaseNetworkClient {

    private final String TAG = this.getClass().getSimpleName();

    private final GithubService githubService;
    private final UserService userService;
    private final NewspaperService newspaperService;

    private final static String USERNAME = "wbsvcs", PASSWORD = "Ml5wb09s!";
    private final static String R_TYPE = "1";

    private final static String PUBLISHER_ID = "1";
    private final static String PAGE_INDEX = "0";
    private final static String PAGE_SIZE = "30";

    public final static String APP_ID = "b8635a7b-843f-4c14-9f46-d01733fb531e";
    public final static String UDID = "0f607264fc6318a92b9e13c65db7cd3c";
    public final static String SECRET_KEY = "rStFTvr1F2Sz6TGGYlaDLL30oM10Qnvs";

    protected final Gson gson;

    public BaseNetworkClient() {
        gson = new Gson();

        githubService = ServiceGenerator.createService(RestBuilderGenerator.getBaseBuilder(), GithubService.class);
        userService = ServiceGenerator.createService(RestBuilderGenerator.getBaseBuilder(), "auth-token", UserService.class);
        newspaperService = ServiceGenerator.createService(RestBuilderGenerator.getBaseBuilder(), "auth-token", NewspaperService.class);

    }


    public <T> void getIssueList(final Class<T> classOfT, final OnTaskCompleted<T> onTaskCompleted) {

        final AccessTokenObject mAccessTokenObject = ServiceGenerator.getRequestTokenObject();

        newspaperService.getAccessToken(mAccessTokenObject, new Callback<AccessTokenObject>() {
            @Override
            public void success(AccessTokenObject accessTokenObject, Response response) {

                final String accessToken = accessTokenObject.getToken();

                LogApp.d(TAG, "AccessToken: " + String.valueOf(accessToken));

                newspaperService.getIssueList(accessToken, PUBLISHER_ID, PAGE_INDEX, PAGE_SIZE, new ResponseCallback() {
                    @Override
                    public void success(Response response) {

                        final String responseString = ServiceGenerator.responseToString(response);

                        final T resultObject = gson.fromJson(responseString, classOfT);

                        onTaskCompleted.onTaskCompleted(resultObject);
                        onTaskCompleted.onTaskCompleted(responseString);

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        onTaskCompleted.onTaskError(error.toString());

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {

                onTaskCompleted.onTaskError(error.toString());
            }
        });
    }

   /* public void getIssueList(Callback<T> mIssueListResponseCallback) {

        final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint(MILLIYET_BASE_API).build();

        Service<T> mService = (Service<T>) restadapter.create(Service.class);

        mService.getMenuList(UtilityMethods.userAuthHeader(), ObjectHolderService.PUBLICATION_ID, R_TYPE, mIssueListResponseCallback);

    }

    public void getIssueListResponse(ResponseCallback mResponseCallback) {

        final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint(MILLIYET_BASE_API).build();

        NewspaperService mService = restadapter.create(NewspaperService.class);

        mService.getIssueList(UtilityMethods.userAuthHeader(), ObjectHolderService.PUBLICATION_ID, R_TYPE, mResponseCallback);

    }*/


    /*
    public void getIssueDetail(String iID, Callback<Object> mIssueDetailResponse)
    {
        final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint(MILLIYET_BASE_API).build();

        NewspaperService mNewspaperService = restadapter.create(NewspaperService.class);

        mNewspaperService.getIssueDetail(UtilityMethods.userAuthHeader(), PUBLISHER_ID, iID, R_TYPE, mIssueDetailResponse);
    }



    public void getIssueExtras(String iID, Callback<IssueListResponse> mIssueListResponse)
    {
        final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint(MILLIYET_BASE_API).build();

        NewspaperService mNewspaperService = restadapter.create(NewspaperService.class);

        mNewspaperService.getIssueExtras(UtilityMethods.userAuthHeader(), iID, R_TYPE, mIssueListResponse);
    }


    public void getIssueDetail(String iID, String rType, Callback<IssueDetailResponse> mIssueDetailResponse)
    {
        final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint(MILLIYET_BASE_API).build();

        NewspaperService mNewspaperService = restadapter.create(NewspaperService.class);

        mNewspaperService.getIssueDetail(UtilityMethods.userAuthHeader(), PUBLISHER_ID, iID, rType, mIssueDetailResponse);
    }


    public void getMenuList(String pNo,
                            String itemCount,
                            String year,
                            String month,
                            String day,
                            String rType,
                            Callback<IssueListResponse> mIssueListResponse)
    {

        final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint(MILLIYET_BASE_API).build();

        NewspaperService mNewspaperService = restadapter.create(NewspaperService.class);

        mNewspaperService.getMenuList(UtilityMethods.userAuthHeader(), PUBLISHER_ID, pNo, itemCount, year, month, day, rType, mIssueListResponse);
    }


    public void getIssueExtras(String iID, String rType, Callback<IssueListResponse> mIssueListResponse)
    {
        final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint(MILLIYET_BASE_API).build();

        NewspaperService mNewspaperService = restadapter.create(NewspaperService.class);

        mNewspaperService.getIssueExtras(UtilityMethods.userAuthHeader(), iID, rType, mIssueListResponse);
    }


    public void getApplication(Callback<AppDetailResponse> mAppDetailResponse)
    {
        final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint(MILLIYET_BASE_API).build();

        NewspaperService mNewspaperService = restadapter.create(NewspaperService.class);

        mNewspaperService.getApplication(UtilityMethods.userAuthHeader(), P_TYPE, APP_ID, R_TYPE, mAppDetailResponse);
    }


    public void getBreakingNews(Callback<BreakingNewsResponse> mBreakingNewsResponse)
    {
        final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint(MILLIYET_BASE_BREAKING_NEWS_API).build();

        NewspaperService mNewspaperService = restadapter.create(NewspaperService.class);

        mNewspaperService.getBreakingNews(UtilityMethods.userAuthHeader(), A_TYPE, TOP_COUNT, mBreakingNewsResponse);
    } */


}
