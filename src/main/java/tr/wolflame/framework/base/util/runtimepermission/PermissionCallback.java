package tr.wolflame.framework.base.util.runtimepermission;

import java.util.ArrayList;

public interface PermissionCallback {

    void onPermissionGranted(ArrayList<String> permissionKeyList);

    void listOfPreGrantedPermissions(ArrayList<String> permissionKeyList);

    void listOfDeniedPermissions(ArrayList<String> permissionKeyList, boolean isAllDenialsNoMore);

    void onPermissionDenied(ArrayList<String> permissionKeyList);

    void onPermissionDeniedWithNoMore(ArrayList<String> permissionKeyList);

    void onRunTimePermissionNotNeeded();
}
