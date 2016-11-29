package tr.wolflame.framework.base.util.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import tr.wolflame.framework.R;
import tr.wolflame.framework.base.util.LogApp;


/**
 * Created by SADIK on 02/07/15.
 */
public abstract class OnlineDetailActivity extends AppCompatActivity {

    public static final String KEY_HTML = "htmlUrl";

    protected final String TAG = this.getClass().getSimpleName();

    protected HTML5WebView mWebView;

    protected Toolbar toolbar;

    protected FrameLayout frameBaseView;

    public static void start(Context context, Class<?> cls, String inputUrl) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(KEY_HTML, inputUrl);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initContentView(R.layout.layout_online_detail);
        setContentView(initLayoutRes());

        initToolBar();

        frameBaseView = (FrameLayout) findViewById(initFrameBaseViewId());

        if (frameBaseView != null) {

            initWebView();

            final Bundle mBundle = getIntent().getExtras();

            if (mBundle != null) {

                final String htmlUrl = mBundle.getString(KEY_HTML, "");

                LogApp.d(TAG, String.valueOf(htmlUrl));

                if (!TextUtils.isEmpty(htmlUrl)) {

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
                                LogApp.e(TAG, "WebView.setWebContentsDebuggingEnabled true");
                                WebView.setWebContentsDebuggingEnabled(true);
                            }
                        }
                    } catch (Exception e) {
                        LogApp.e(TAG, String.valueOf(e.toString()));
                    }

                    if (isVideo()) {
                        final String contentHTML = String.format(getResources().getString(R.string.iframeHTML), String.valueOf(htmlUrl), "");

                        final String customHtml = String.format(getResources().getString(R.string.embedLinkContentHTML), contentHTML, "");

                        LogApp.d(TAG, "customHTML: " + String.valueOf(customHtml));

                        //mWebView.requestFocus();
                        //mWebView.loadData(customHtml, "text/html", "UTF-8");
                        mWebView.loadDataWithBaseURL(null, customHtml, "text/html", "UTF-8", null);
                    } else {
                        mWebView.loadUrl(htmlUrl);
                    }

                } else {
                    LogApp.e(TAG, "html is empty");
                    finishAct();
                }

            } else {
                LogApp.e(TAG, "bundle is null");
                finishAct();
            }
        } else {
            LogApp.e(TAG, "baseFrameView is null");
            finishAct();
        }

    }


    private void initToolBar() {

        final int toolbarViewId = initToolbarRes();

        toolbar = (Toolbar) findViewById(toolbarViewId);

        if (toolbar == null)
            return;

        final int toolbarLogoResId = initToolbarLogoResId();

        if (toolbarLogoResId != 0) {
            toolbar.setLogo(toolbarLogoResId);
        }

        toolbar.setTitle("");
        toolbar.setSubtitle("");

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAct();
            }
        });

    }

    @DrawableRes
    protected int initToolbarLogoResId() {
        return StaticFields.INVALID;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView != null && mWebView.canGoBack()) {
            //if Back key pressed and webview can navigate to previous page
            mWebView.goBack();
            // go back to previous page
            return true;
        } else {
            finishAct();
            // finish the activity
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initWebView() {
        mWebView = new HTML5WebView(OnlineDetailActivity.this) {
            @Override
            protected WebViewClient initWebViewClient() {
                return new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        LogApp.d(TAG, "Processing webView url click...");
                        view.loadUrl(url);
                        return true;
                    }

                    public void onPageFinished(WebView view, String url) {
                        LogApp.d(TAG, "Finished loading URL: " + url);
                    }

                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        LogApp.e(TAG, "Error loading URL: " + error.toString());
                        Toast.makeText(OnlineDetailActivity.this, getResources().getString(R.string.error_no_content), Toast.LENGTH_SHORT).show();
                        finishAct();
                        super.onReceivedError(view, request, error);
                    }
                };
            }
        };


        final FrameLayout.LayoutParams mLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        //mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mLayoutParams.gravity = Gravity.CENTER;

        mWebView.setLayoutParams(mLayoutParams);

        final FrameLayout resultLayout = mWebView.getLayout();
        resultLayout.setLayoutParams(mLayoutParams);

        frameBaseView.addView(resultLayout);

        //mWebView.setBackgroundColor(ContextCompat.getColor(OnlineDetailActivity.this, android.R.color.white));

    }

    protected void finishAct() {
        try {

            if (mWebView != null) {
                mWebView.loadUrl("");
                mWebView.stopLoading();
                if (mWebView.inCustomView()) {
                    mWebView.hideCustomView();
                }
                mWebView.destroy();
            }
        } catch (Exception e) {
            LogApp.e(TAG, String.valueOf(e.toString()));
        }
        finish();
    }

    @IdRes
    protected int initToolbarRes() {
        return R.id.toolbar;
    }

    @LayoutRes
    protected abstract int initLayoutRes();

    @IdRes
    protected abstract int initFrameBaseViewId();


    protected abstract boolean isVideo();
}