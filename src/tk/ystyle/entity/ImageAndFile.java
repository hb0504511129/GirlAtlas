package tk.ystyle.entity;

/**
 * Created by 小奕 on 2014-08-08 16:50.
 */
public class ImageAndFile {
    private String url;
    private String filename;

    public ImageAndFile(String url, String filename) {
        this.url = url;
        this.filename = filename;
    }

    public ImageAndFile() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "ImageAndFile{" +
                "url='" + url + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }
}
