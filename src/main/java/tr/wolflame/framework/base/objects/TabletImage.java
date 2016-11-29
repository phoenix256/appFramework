package tr.wolflame.framework.base.objects;

/**
 * Created by SADIK on 14/09/15.
 */
public class TabletImage {

    private String URL;
    private int FileSize;
    private int Width;
    private int Height;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getFileSize() {
        return FileSize;
    }

    public void setFileSize(int fileSize) {
        FileSize = fileSize;
    }

    public int getWidth() {
        return Width;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }
}
