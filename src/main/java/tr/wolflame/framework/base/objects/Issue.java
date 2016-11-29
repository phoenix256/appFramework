package tr.wolflame.framework.base.objects;

/**
 * Created by SADIK on 14/09/15.
 */
public class Issue extends BaseItem {

    private int ID;

    private String Name;

    private String Date;
    private TabletImage TabletImage;
    private PhoneImage PhoneImage;
    private Preview Preview;
    private PDF PDF;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public TabletImage getTabletImage() {
        return TabletImage;
    }

    public void setTabletImage(TabletImage tabletImage) {
        TabletImage = tabletImage;
    }

    public PhoneImage getPhoneImage() {
        return PhoneImage;
    }

    public void setPhoneImage(PhoneImage phoneImage) {
        PhoneImage = phoneImage;
    }

    public Preview getPreview() {
        return Preview;
    }

    public void setPreview(Preview preview) {
        Preview = preview;
    }

    public PDF getPDF() {
        return PDF;
    }

    public void setPDF(PDF PDF) {
        this.PDF = PDF;
    }

    @Override
    public String getTitle() {
        return Name;
    }

    @Override
    public void setTitle(String title) {
        Name = title;
        super.setTitle(title);
    }
}
