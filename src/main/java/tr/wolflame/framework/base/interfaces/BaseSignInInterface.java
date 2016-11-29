package tr.wolflame.framework.base.interfaces;

/**
 * Created by sadikaltintoprak on 25/11/2016.
 */

public interface BaseSignInInterface {

    void onUserSignedIn();
    void onSignInCancelled();
    void onSignInNoNetwork();
    void onUserNotSignedIn();
}
