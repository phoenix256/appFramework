package tr.wolflame.framework.base.util.helper;

import retrofit.mime.TypedString;

/**
 * Created by SADIK on 14/08/15.
 */
public class TypedJsonString extends TypedString {
    public TypedJsonString(String body) {
        super(body);
    }

    @Override
    public String mimeType() {
        return "application/json";
    }
}