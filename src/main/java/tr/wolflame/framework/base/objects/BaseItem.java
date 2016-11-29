package tr.wolflame.framework.base.objects;

import java.io.Serializable;

import tr.wolflame.framework.base.util.helper.StaticFields;

/**
 * Created by SADIK on 24/02/16.
 */
public class BaseItem implements Serializable {

    static final long serialVersionUID = StaticFields.serialVersionUID;

    protected boolean isSelected;

    protected String title;

    protected int id = StaticFields.INVALID;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean input) {
        this.isSelected = input;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
