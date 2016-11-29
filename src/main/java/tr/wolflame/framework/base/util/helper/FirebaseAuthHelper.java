package tr.wolflame.framework.base.util.helper;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;

import tr.wolflame.framework.BuildConfig;
import tr.wolflame.framework.base.util.Utils;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

/**
 * Created by sadikaltintoprak on 24/11/2016.
 */

public class FirebaseAuthHelper {

    public static final int INVALID_ID = -1;

    private static final String TAG = "FirebaseAuthHelper";

    public static boolean isUserSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    @Nullable
    public static FirebaseUser getCurrentFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void initLogInActivity(Activity activity) {
        initLogInActivity(activity, INVALID_ID, INVALID_ID);
    }

    public static void initLogInActivity(Activity activity, @DrawableRes int logoId) {
        initLogInActivity(activity, logoId, INVALID_ID);
    }

    public static void initLogInActivityWithStyle(Activity activity, @StyleRes int styleId) {
        initLogInActivity(activity, INVALID_ID, styleId);
    }

    public static void initLogInActivity(Activity activity, @DrawableRes int logoId, @StyleRes int styleId) {

        if (Utils.isActivityValid(activity)) {
            final AuthUI.IdpConfig facebookIdp = new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER)
                    .setPermissions(Collections.singletonList("user_friends"))
                    .build();

            final AuthUI.SignInIntentBuilder signInIntentBuilder = AuthUI.getInstance()
                    .createSignInIntentBuilder();

            signInIntentBuilder
                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                            facebookIdp))
                    .setIsSmartLockEnabled(!BuildConfig.DEBUG);

            if (styleId != INVALID_ID)
                signInIntentBuilder.setTheme(styleId);

            if (logoId != INVALID_ID)
                signInIntentBuilder.setLogo(logoId);

            // not signed in
            activity.startActivityForResult(signInIntentBuilder.build(),
                    RC_SIGN_IN);
        } else {
            Log.e(TAG, "activity is not valid");
        }
    }
}
