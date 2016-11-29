package tr.wolflame.framework.base.objects;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

import tr.wolflame.framework.base.util.helper.StaticFields;

/**
 * Created by SADIK on 09/02/16.
 */
public class ExtraPair implements Serializable {
    static final long serialVersionUID = StaticFields.serialVersionUID;

    private String key;

    private Serializable serializable;

    private Parcelable parcelableObj;

    private ArrayList<Serializable> serializableList;

    private ArrayList<Parcelable> parcelables;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Serializable getSerializable() {
        return serializable;
    }

    public void setSerializable(Serializable serializable) {
        this.serializable = serializable;
    }

    public Parcelable getParcelableObj() {
        return parcelableObj;
    }

    public void setParcelableObj(Parcelable parcelable) {
        this.parcelableObj = parcelable;
    }

    public ArrayList<Serializable> getSerializableList() {
        return serializableList;
    }

    public void setSerializableList(ArrayList<Serializable> serializableList) {
        this.serializableList = serializableList;
    }

    public ArrayList<Parcelable> getParcelables() {
        return parcelables;
    }

    public void setParcelables(ArrayList<Parcelable> parcelables) {
        this.parcelables = parcelables;
    }
}
