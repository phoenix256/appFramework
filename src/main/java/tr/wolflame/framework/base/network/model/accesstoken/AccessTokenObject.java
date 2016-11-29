package tr.wolflame.framework.base.network.model.accesstoken;

/**
 * Created by SADIK on 11/09/15.
 */
public class AccessTokenObject {

    private String Status;
    private String Token;
    private String AppID;
    private String UDID;
    private String Secret;
    private String ExpiredDate;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getAppID() {
        return AppID;
    }

    public void setAppID(String appID) {
        AppID = appID;
    }

    public String getUDID() {
        return UDID;
    }

    public void setUDID(String UDID) {
        this.UDID = UDID;
    }

    public String getExpiredDate() {
        return ExpiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        ExpiredDate = expiredDate;
    }

    public String getSecret() {
        return Secret;
    }

    public void setSecret(String secret) {
        Secret = secret;
    }
}
