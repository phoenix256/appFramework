package tr.wolflame.framework.base.objects;

import android.support.annotation.DrawableRes;
import android.view.Menu;

import tr.wolflame.framework.base.util.helper.StaticFields;

/**
 * Created by SADIK on 22/02/16.
 */
public class LeftMenuItem extends BaseItem {

    public static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on

    public static final int TYPE_ITEM = 1;     // IF the view under inflation and population is header or Item

    public static final int TYPE_FOOTER = 2;     // IF the view under inflation and population is header or Item

    public static final int TYPE_OTHER = 3;     // IF the view under inflation and population is header or Item

    private String subTitle;

    @DrawableRes
    private int icon = StaticFields.INVALID;

    private int itemType;

    private int itemId;

    private int groupId = Menu.NONE;

    private int order = Menu.NONE;

    private boolean isSubMenu;

    public LeftMenuItem() {
        this.itemType = TYPE_ITEM;
    }

    @DrawableRes
    public int getIcon() {
        return icon;
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isSubMenu() {
        return isSubMenu;
    }

    public void setSubMenu(boolean subMenu) {
        isSubMenu = subMenu;
    }
}
