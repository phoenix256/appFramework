package tr.wolflame.framework.base.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.ui.ResultCodes;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;

import tr.wolflame.framework.R;
import tr.wolflame.framework.base.adapters.BaseItemAdapter;
import tr.wolflame.framework.base.adapters.ViewPagerAdapter;
import tr.wolflame.framework.base.fragments.BaseFragment;
import tr.wolflame.framework.base.interfaces.BaseAppCompatActivityInterface;
import tr.wolflame.framework.base.interfaces.BaseSignInInterface;
import tr.wolflame.framework.base.network.RestBuilderGenerator;
import tr.wolflame.framework.base.objects.ExtraPair;
import tr.wolflame.framework.base.objects.FragmentPair;
import tr.wolflame.framework.base.objects.LeftMenuItem;
import tr.wolflame.framework.base.util.LogApp;
import tr.wolflame.framework.base.util.helper.MultiStateFrameLayout;
import tr.wolflame.framework.base.util.helper.OnTaskCompleted;
import tr.wolflame.framework.base.util.helper.SearchRunnable;
import tr.wolflame.framework.base.util.helper.StaticFields;
import tr.wolflame.framework.base.util.helper.TextViewPlus;
import tr.wolflame.framework.base.util.runtimepermission.PermissionCallback;
import tr.wolflame.framework.base.util.runtimepermission.RuntimePermissionManager;


/**
 * Created by SADIK on 05/02/16.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements BaseAppCompatActivityInterface, NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener, MultiStateFrameLayout.RetryListener {

    protected final String TAG = this.getClass().getSimpleName();

    protected MultiStateFrameLayout multiStateFrameLayout;

    protected SwipeRefreshLayout swipeRefreshLayout;

    protected RuntimePermissionManager mRuntimePermissionManager;

    protected BaseSignInInterface baseSignInInterface;

    private static final long DELAY_MILLISECOND = 0;

    protected Toolbar toolbar;
    protected TabLayout tabLayout;
    protected ViewPager viewPager;

    private ExtraPair extraPair;

    protected RecyclerView leftMenuRecyclerView;        // Declaring RecyclerView
    protected DrawerLayout leftMenuDrawer;              // Declaring DrawerLayout

    protected ActionBarDrawerToggle leftMenuDrawerToggle;

    protected NavigationView navigationView;

    protected BottomBar bottomBar;

    protected Menu menu;

    protected MaterialSearchView searchView;

    protected BaseFragment searchResultFragment;
    protected View searchResultView;

    private static final long SEARCH_DELAY_MS = 750L;
    private final Handler handler = new Handler();
    private SearchRunnable searchRunnable;

    private ImageView splashIcon;
    private TextViewPlus splashTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(initLayoutId());

        if (getResources().getBoolean(R.bool.portrait_only) && isPhoneLayoutForcedPortrait()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        initExtraPair();

        initSplashUi();

        initToolBar();

        initDrawer();

        initMultiState();

        initSwipeRefreshLayout();

        initViewPager();

        initBottomBar(savedInstanceState);

        createAction();
    }

    private void initToolBar() {

        final int toolbarViewId = initToolbarViewId();

        toolbar = (Toolbar) findViewById(toolbarViewId);

        if (toolbar == null)
            return;

        final int toolbarLogoResId = initToolbarLogoResId();
        final String toolbarTitle = initToolbarTitle();
        final String toolbarSubtitle = initToolbarSubtitle();

        if (toolbarLogoResId != StaticFields.INVALID) {
            toolbar.setLogo(toolbarLogoResId);
        }

        if (TextUtils.isEmpty(toolbarTitle)) {
            toolbar.setTitle("");
        } else {
            toolbar.setTitle(" " + toolbarTitle);
        }

        if (TextUtils.isEmpty(toolbarSubtitle)) {
            toolbar.setSubtitle("");
        } else {
            toolbar.setSubtitle(" " + toolbarSubtitle);
        }

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            if (isHomeAsUpEnabled())
                actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setAppBarLayoutHeight();
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(initViewPagerId());

        if (viewPager != null) {
            setupViewPager();
            initTabLayout();
        }
    }

    public void setupViewPager() {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        for (FragmentPair fragmentPair : initFragmentPairList()) {
            adapter.addFragment(fragmentPair);
        }
        viewPager.setAdapter(adapter);
    }

    public ViewPagerAdapter getViewPagerAdapter() {
        if (viewPager != null)
            return ((ViewPagerAdapter) viewPager.getAdapter());
        return null;
    }

    private void initTabLayout() {

        tabLayout = (TabLayout) findViewById(initTabLayoutId());

        if (tabLayout != null && viewPager != null)
            tabLayout.setupWithViewPager(viewPager);
    }

    private void initBottomBar(Bundle savedInstanceState) {

        final int bottomTabMenuResource = getBottomTabMenuResource();

        if (bottomTabMenuResource != StaticFields.INVALID) {

            final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(getBottomTabCoordinatorToAttach());
            final NestedScrollView nestedScrollView = (NestedScrollView) findViewById(getBottomTabNestedScrollToAttach());

            if (coordinatorLayout != null && nestedScrollView != null) {
                // Instead of attach(), use attachShy():
                bottomBar = BottomBar.attachShy(coordinatorLayout, nestedScrollView, savedInstanceState);
            } else {
                bottomBar = BottomBar.attach(this, savedInstanceState);
            }


            bottomBar.setItemsFromMenu(bottomTabMenuResource, initOnTabMenuClickListener());

            /*final int colorRes = initBottomBarActiveTabColor();

            if (colorRes != StaticFields.INVALID)
                bottomBar.setActiveTabColor(colorRes);*/


            /*//I don't want to set items from a menu resource!
            bottomBar.setItems(
                    new BottomBarTab(R.drawable.ic_recents, "Recents"),
                    new BottomBarTab(R.drawable.ic_favorites, "Favorites"),
                    new BottomBarTab(R.drawable.ic_nearby, "Nearby")
            );

            // Listen for tab changes
            bottomBar.setOnTabClickListener(new OnTabClickListener() {
                @Override
                public void onTabSelected(int position) {
                    // The user selected a tab at the specified position
                }

                @Override
                public void onTabReSelected(int position) {
                    // The user reselected a tab at the specified position!
                }
            });*/

            // Setting colors for different tabs when there's more than three of them.
            // You can set colors for tabs in three different ways as shown below.
            //bottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
            //bottomBar.mapColorForTab(1, 0xFF5D4037);
            //bottomBar.mapColorForTab(2, "#7B1FA2");
            //bottomBar.mapColorForTab(3, "#FF5252");
            //bottomBar.mapColorForTab(4, "#FF9800");




            /*// Make a Badge for the first tab, with red background color and a value of "13".
            final BottomBarBadge unreadMessages = bottomBar.makeBadgeForTabAt(0, "#FF0000", 13);

            // Control the badge's visibility
            unreadMessages.show();
            unreadMessages.hide();

            // Change the displayed count for this badge.
            unreadMessages.setCount(4);

            // Change the show / hide animation duration.
            unreadMessages.setAnimationDuration(200);

            // If you want the badge be shown always after unselecting the tab that contains it.
            unreadMessages.setAutoShowAfterUnSelection(true);

            // Disable the left bar on tablets and behave exactly the same on mobile and tablets instead.
            bottomBar.noTabletGoodness();

            // Show all titles even when there's more than three tabs.
            bottomBar.useFixedMode();

            // Use the dark theme.
            bottomBar.useDarkTheme();

            // Set the color for the active tab. Ignored on mobile when there are more than three tabs.
            bottomBar.setActiveTabColor("#009688");

            // Use custom text appearance in tab titles.
            bottomBar.setTextAppearance(R.style.MyTextAppearance);

            // Use custom typeface that's located at the "/src/main/assets" directory. If using with
            // custom text appearance, set the text appearance first.
            bottomBar.setTypeFace("MyFont.ttf");*/

        }
    }

    @MenuRes
    protected int getBottomTabMenuResource() {
        return StaticFields.INVALID;
    }

    @IdRes
    protected int getBottomTabCoordinatorToAttach() {
        return StaticFields.INVALID;
    }

    @IdRes
    protected int getBottomTabNestedScrollToAttach() {
        return StaticFields.INVALID;
    }

    protected OnMenuTabClickListener initOnTabMenuClickListener() {
        return new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        };
    }

    @ColorRes
    protected int initBottomBarActiveTabColor() {
        return StaticFields.INVALID;
    }

    private void initDrawer() {

        leftMenuDrawer = (DrawerLayout) findViewById(initLeftMenuDrawerLayoutId());        // Drawer object Assigned to the view

        if (leftMenuDrawer != null) {
            leftMenuDrawerToggle = new ActionBarDrawerToggle(this, leftMenuDrawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                    // open I am not going to put anything here)
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    // Code here will execute once drawer is closed
                }

            };

            // Drawer Toggle Object Made
            leftMenuDrawer.setDrawerListener(leftMenuDrawerToggle); // Drawer Listener set to the Drawer toggle
            leftMenuDrawerToggle.syncState();               // Finally we set the drawer toggle sync State


            navigationView = (NavigationView) findViewById(initLeftNagivationViewId());

            if (navigationView != null) {
                navigationView.setNavigationItemSelectedListener(this);
            }

        }

    }


    protected Menu getNavigationViewMenu() {
        return (navigationView != null) ? navigationView.getMenu() : null;
    }

    @IdRes
    protected int initLeftMenuDrawerLayoutId() {
        return R.id.drawerLayout;
    }


    @IdRes
    protected int initLeftNagivationViewId() {
        return R.id.nav_view;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        handleNavigationItem(item);

        if (leftMenuDrawer != null) {
            leftMenuDrawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }


    /**
     * @param item Left Menu Navigation item handler
     */
    protected void handleNavigationItem(MenuItem item) {

    }

    protected void setNavigationViewItemSelected(MenuItem item) {
        if (item != null) {
            try {
                onNavigationItemSelected(item);
                navigationView.setCheckedItem(item.getItemId());

            } catch (Exception e) {
                LogApp.e(TAG, String.valueOf(e.toString()));
            }
        } else {
            LogApp.e(TAG, "MenuItem is NULL");
        }
    }

    protected void swapNavigationMenuItems(ArrayList<? extends LeftMenuItem> groupItems, ArrayList<? extends LeftMenuItem> extraActionItems, String extraActionTitle) {
        if (navigationView != null) {

            final Menu menu = navigationView.getMenu();

            if (menu != null) {
                menu.clear();

                if (groupItems != null && !groupItems.isEmpty()) {
                    for (int i = 0; i < groupItems.size(); i++) {
                        final LeftMenuItem menuItem = groupItems.get(i);

                        menu.add(menuItem.getGroupId(), menuItem.getItemId(), menuItem.getOrder(), menuItem.getTitle());

                        int iconRes = menuItem.getIcon();

                        if (iconRes != StaticFields.INVALID)
                            menu.getItem(i).setIcon(iconRes);

                        if (i == groupItems.size() - 1) {
                            menu.setGroupCheckable(menuItem.getGroupId(), true, true);
                        }
                    }
                }

                if (extraActionItems != null && !extraActionItems.isEmpty()) {

                    SubMenu extras = menu.addSubMenu(extraActionTitle);

                    for (int i = 0; i < extraActionItems.size(); i++) {
                        final LeftMenuItem menuItem = extraActionItems.get(i);

                        extras.add(menuItem.getGroupId(), menuItem.getItemId(), menuItem.getOrder(), menuItem.getTitle());
                        //extras.add(menuItem.getTitle());

                        int iconRes = menuItem.getIcon();

                        if (iconRes != StaticFields.INVALID)
                            extras.getItem(i).setIcon(iconRes);

                    }

                }

            }
        }
    }

    /**
     * Action handler after activity created, also with a delay if it is wanted.
     */
    private void createAction() {

        if (initDelayMillisecond() <= DELAY_MILLISECOND) {
            onCreated();
        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            onCreated();
                        }
                    });
                }
            }, initDelayMillisecond());

        }

    }


    protected boolean isPhoneLayoutForcedPortrait() {
        return true;
    }

    /**
     * @return DELAY for action handler
     */
    protected long initDelayMillisecond() {
        return DELAY_MILLISECOND;
    }

    protected boolean isHomeAsUpEnabled() {
        return true;
    }

    @DrawableRes
    protected int initToolbarLogoResId() {
        return StaticFields.INVALID;
    }

    @IdRes
    protected int initToolbarViewId() {
        return R.id.toolbar;
    }

    protected String initToolbarTitle() {
        return StaticFields.EMPTY_STR;
    }

    protected String initToolbarSubtitle() {
        return StaticFields.EMPTY_STR;
    }

    /**
     * @return viewpagerId initialization if it is wanted.
     * {@link #initTabLayoutId()} must be also overridden.
     */
    @IdRes
    protected int initViewPagerId() {
        return R.id.viewpager;
    }

    /**
     * @return tabLayoutId initialization if it is wanted.
     * {@link #initViewPagerId()} must be also overridden.
     */
    @IdRes
    protected int initTabLayoutId() {
        return R.id.tabs;
    }

    protected ArrayList<FragmentPair> initFragmentPairList() {
        return new ArrayList<>();
    }

    private void initExtraPair() {

        final Bundle extras = this.getIntent().getExtras();

        if (extras != null) {
            extraPair = (ExtraPair) extras.getSerializable(StaticFields.KEY_EXTRA_PAIR);
        }
    }

    protected ExtraPair getExtraPair() {
        return extraPair;
    }


    protected void replaceFragment(int resId, Fragment fragment) {
        replaceFragment(resId, fragment, false);
    }

    protected void replaceFragment(int resId, Fragment fragment, boolean addToBackStack) {

        try {
            final FragmentManager fragmentManager = getSupportFragmentManager();

            if (fragmentManager != null) {
                if (addToBackStack) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(resId, fragment)
                            .addToBackStack(fragment.getClass().getSimpleName())
                            .commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(resId, fragment)
                            .commit();
                }
            } else {
                LogApp.e(TAG, "fragmentManager is null");
            }

        } catch (Exception e) {
            LogApp.e(TAG, String.valueOf(e.toString()));

        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            // Necessary to restore the BottomBar's state, otherwise we would
            // lose the current tab on orientation change.
            if (bottomBar != null && outState != null)
                bottomBar.onSaveInstanceState(outState);

            super.onSaveInstanceState(outState);
        } catch (Exception e) {
            LogApp.e(TAG, String.valueOf(e.toString()));
        }
    }

    /**
     * @param permissionKeyArray has to be new String[]{ Manifest.permission.INTERNET,
     *                           Manifest.permission.WAKE_LOCK,
     *                           Manifest.permission.ACCESS_FINE_LOCATION,
     *                           Manifest.permission.ACCESS_WIFI_STATE,
     *                           Manifest.permission.ACCESS_NETWORK_STATE,
     *                           Manifest.permission.WRITE_EXTERNAL_STORAGE }.
     */
    public void requestPermission(final String[] permissionKeyArray, final OnTaskCompleted<ArrayList<String>> onTaskCompleted) {

        mRuntimePermissionManager = new RuntimePermissionManager(BaseAppCompatActivity.this);

        mRuntimePermissionManager.requestPermission(permissionKeyArray, new PermissionCallback() {

            @Override
            public void onPermissionGranted(ArrayList<String> permissionKeyList) {
                LogApp.d(TAG, String.valueOf("onPermissionGranted: ") + String.valueOf(permissionKeyList.toString()));
                onTaskCompleted.onTaskCompleted(permissionKeyList);
            }

            @Override
            public void listOfPreGrantedPermissions(ArrayList<String> permissionKeyList) {
                LogApp.d(TAG, String.valueOf("listOfPreGrantedPermissions: ") + String.valueOf(permissionKeyList.toString()));

            }

            @Override
            public void listOfDeniedPermissions(final ArrayList<String> permissionKeyList, boolean isAllDenialsNoMore) {
                LogApp.d(TAG, String.valueOf("listOfDeniedPermissions: ") + String.valueOf(permissionKeyList.toString()));
                LogApp.d(TAG, String.valueOf("isAllDenialsNoMore: ") + String.valueOf(isAllDenialsNoMore));

                mRuntimePermissionManager.showPermissionAlert(permissionKeyList, true, isAllDenialsNoMore, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        requestPermission(permissionKeyArray, onTaskCompleted);
                    }
                }, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        Toast.makeText(BaseAppCompatActivity.this, String.format(getResources().getString(R.string.permissionListWarning), mRuntimePermissionManager.getPermissionsText(permissionKeyList)), Toast.LENGTH_LONG).show();
                        onTaskCompleted.onTaskError("PermissionsDeniedWithClick");
                    }
                });
            }

            @Override
            public void onPermissionDenied(ArrayList<String> permissionKeyList) {
                LogApp.d(TAG, String.valueOf("onPermissionDenied: ") + String.valueOf(permissionKeyList.toString()));
            }

            @Override
            public void onPermissionDeniedWithNoMore(ArrayList<String> permissionKeyList) {
                LogApp.d(TAG, String.valueOf("onPermissionDeniedWithNoMore: ") + String.valueOf(permissionKeyList.toString()));
            }

            @Override
            public void onRunTimePermissionNotNeeded() {
                LogApp.d(TAG, String.valueOf("onRunTimePermissionNotNeeded"));
                onTaskCompleted.onTaskCompleted("onRunTimePermissionNotNeeded");
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRuntimePermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        LogApp.d(TAG, "permission onActivityResult");
        if (mRuntimePermissionManager != null)
            mRuntimePermissionManager.onActivityResult(requestCode, resultCode, data);

        if (baseSignInInterface != null) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                baseSignInInterface.onUserSignedIn();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in canceled
                baseSignInInterface.onSignInCancelled();
            } else if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
                // No network
                baseSignInInterface.onSignInNoNetwork();
            } else {
                // User is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message.
                baseSignInInterface.onUserNotSignedIn();
            }

        }
    }


    protected void finishActivityWithResult(Bundle inputData) {
        final Intent intent = new Intent();
        intent.putExtras(inputData);
        setResult(RESULT_OK, intent);
        finish();
    }

    @MenuRes
    protected int initToolbarMenu() {
        return StaticFields.INVALID;
    }

    @IdRes
    protected int initSearchViewId() {
        return StaticFields.INVALID;
    }

    protected boolean isVoiceSearchEnabled() {
        return true;
    }

    /**
     * @return view has to be a BaseFragment Id
     */
    @IdRes
    protected int initSearchResultFragmentId() {
        return StaticFields.INVALID;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        final int menuRes = initToolbarMenu();

        if (menuRes != StaticFields.INVALID) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(menuRes, menu);

            final int searchViewId = initSearchViewId();
            final int searchResultId = initSearchResultFragmentId();

            if (searchViewId != StaticFields.INVALID) {

                searchView = (MaterialSearchView) findViewById(searchViewId);

                if (searchResultId != StaticFields.INVALID) {
                    searchResultView = findViewById(searchResultId);
                    searchResultFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(searchResultId);
                }

                if (searchView != null) {

                    final MenuItem item = menu.findItem(initSearchMenuId());

                    if (item != null) {
                        searchView.setMenuItem(item);

                        searchView.setOnQueryTextListener(initOnQueryTextListener());

                        searchView.setOnSearchViewListener(initSearchViewListener());

                        searchView.setVoiceSearch(isVoiceSearchEnabled());

                        searchView.setBackgroundColor(ContextCompat.getColor(BaseAppCompatActivity.this, initSearchViewColor()));

                        if (searchResultView != null && searchResultFragment != null)
                            searchResultView.setVisibility(View.GONE);
                    }
                }

            }
        }

        return true;

    }

    @ColorRes
    protected int initSearchViewColor() {
        return R.color.colorAccent;
    }

    protected boolean searchResultIsFragment() {
        return searchResultView != null && searchResultFragment != null;
    }


    protected int initSearchMenuId() {
        return R.id.action_search;
    }

    protected MaterialSearchView.OnQueryTextListener initOnQueryTextListener() {
        return new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                LogApp.d(TAG, "onQueryTextSubmit: " + String.valueOf(query));

                if (searchResultIsFragment())
                    loadQuery(searchResultFragment, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                LogApp.d(TAG, "onQueryTextChange: " + String.valueOf(newText));

                if (searchResultIsFragment())
                    loadQuery(searchResultFragment, newText);

                return false;
            }
        };
    }

    protected MaterialSearchView.SearchViewListener initSearchViewListener() {
        return new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
                LogApp.d(TAG, "onSearchViewShown");

                if (searchResultIsFragment())
                    searchResultView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
                LogApp.d(TAG, "onSearchViewClosed");

                if (searchResultIsFragment())
                    searchResultView.setVisibility(View.GONE);
            }
        };
    }


    private void loadQuery(BaseFragment fragment, String query) {

        if (fragment != null) {
            final BaseItemAdapter adapter = fragment.getAdapter();
            LogApp.d(TAG, "query: " + String.valueOf(query));

            if (adapter != null) {
                handler.removeCallbacks(searchRunnable);
                searchRunnable = new SearchRunnable(adapter, query);
                handler.postDelayed(searchRunnable, SEARCH_DELAY_MS);
            }
        }

    }


    private void setAppBarLayoutHeight() {

        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(initAppBarLayoutId());

        if (appBarLayout != null) {
            float aspectRatio = this.initAppBarImageAspectRatio();
            if (aspectRatio != 0.0F) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                int height = (int) ((float) width * aspectRatio);
                int heightInDp = (int) TypedValue.applyDimension(0, (float) height, this.getResources().getDisplayMetrics());
                CoordinatorLayout.LayoutParams appBarParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                appBarParams.height = heightInDp;
            }
        }

    }

    @IdRes
    protected int initAppBarLayoutId() {
        return StaticFields.INVALID;
    }

    protected float initAppBarImageAspectRatio() {
        return StaticFields.INVALID;
    }

    private void initSwipeRefreshLayout() {

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(initSwipeRefreshLayoutId());

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setNestedScrollingEnabled(true);
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onRetry() {
    }

    @IdRes
    protected int initSwipeRefreshLayoutId() {
        return StaticFields.INVALID;
    }

    protected void setSwipeRefreshing(final boolean willBeRefreshed) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(willBeRefreshed);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchView != null && searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    public void onNetworkError(String error) {
        LogApp.e(TAG, String.valueOf(error));

        if (!TextUtils.isEmpty(error) && error.contains(RestBuilderGenerator.RETROFIT_TOO_MUCH_REQUEST_CODE))
            error = getResources().getString(R.string.error_no_content);

        if (multiStateFrameLayout != null && !isFinishing()) {
            multiStateFrameLayout.setViewState(MultiStateFrameLayout.ViewState.ERROR);
            multiStateFrameLayout.setErrorText(error);
        }
    }

    public void onSuccessfulResponse() {
        if (multiStateFrameLayout != null && !isFinishing()) {
            multiStateFrameLayout.setViewState(MultiStateFrameLayout.ViewState.CONTENT);
        }
    }

    @IdRes
    protected int initMultiStateFrameLayoutId() {
        return StaticFields.INVALID;
    }

    private void initMultiState() {
        multiStateFrameLayout = (MultiStateFrameLayout) findViewById(initMultiStateFrameLayoutId());

        if (multiStateFrameLayout != null) {
            multiStateFrameLayout.setErrorTintColorResId(this.getErrorTintColorResId());
            multiStateFrameLayout.setViewState(MultiStateFrameLayout.ViewState.CONTENT);
            multiStateFrameLayout.setRetryListener(this);
        }
    }

    public MultiStateFrameLayout getMultiStateFrameLayout() {
        return multiStateFrameLayout;
    }

    @ColorRes
    protected int getErrorTintColorResId() {
        return R.color.colorAccent;
    }

    protected void setBackgroundColor(@ColorInt int colorRes) {
        final View view = getRootView();
        if (view != null) {
            view.setBackgroundColor(colorRes);
        }
    }

    protected void setBackgroundDrawable(@DrawableRes int drawableRes) {
        final View view = getRootView();
        if (view != null) {
            view.setBackgroundResource(drawableRes);
        }
    }

    @Nullable
    private View getRootView() {
        return findViewById(android.R.id.content);
    }

    private void initSplashUi() {
        splashIcon = (ImageView) findViewById(initSplashIconResId());

        splashTv = (TextViewPlus) findViewById(initSplashTextResId());

        if (splashIcon != null) {
            final int splashIconRes = initSplashIcon();

            if (splashIconRes != StaticFields.INVALID)
                splashIcon.setImageResource(splashIconRes);
        }

        if (splashTv != null) {
            final String splashText = initSplashText();

            if (!TextUtils.isEmpty(splashText))
                splashTv.setText(initSplashText());
            else
                splashTv.setVisibility(View.GONE);
        }
    }

    @IdRes
    protected int initSplashIconResId() {
        return R.id.imageView_splashIcon;
    }

    @IdRes
    protected int initSplashTextResId() {
        return R.id.textView_splashText;
    }

    @DrawableRes
    protected int initSplashIcon() {
        return StaticFields.INVALID;
    }

    protected String initSplashText() {
        return StaticFields.EMPTY_STR;
    }

    public void setBaseSignInInterface(BaseSignInInterface baseSignInInterface) {
        this.baseSignInInterface = baseSignInInterface;
    }
}
