package tr.wolflame.framework.base.util.runtimepermission;

public class PermissionObject {

        private String permissionKey;
        private PermissionCallback mPermissionCallback;

        public PermissionObject(String permissionKey, PermissionCallback mPermissionCallback) {
            this.permissionKey = permissionKey;
            this.mPermissionCallback = mPermissionCallback;
        }


        public String getPermissionKey() {
            return permissionKey;
        }

        public void setPermissionKey(String permissionKey) {
            this.permissionKey = permissionKey;
        }


    public PermissionCallback getmPermissionCallback() {
        return mPermissionCallback;
    }

    public void setmPermissionCallback(PermissionCallback mPermissionCallback) {
        this.mPermissionCallback = mPermissionCallback;
    }
}