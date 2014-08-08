package tk.ystyle.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by 小奕 on 2014-08-08 13:21.
 */
public class PhotoAlbum {
    private String title;
    private String url;
    private String mainImg;
    private String number;
    private String createTime;
    private List<Image> images;

    public PhotoAlbum(String title, String url, String mainImg, String number, String createTime, List<Image> images) {
        this.title = title;
        this.url = url;
        this.mainImg = mainImg;
        this.number = number;
        this.createTime = createTime;
        this.images = images;
    }

    public PhotoAlbum() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "PhotoAlbum{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", mainImg='" + mainImg + '\'' +
                ", number='" + number + '\'' +
                ", createTime='" + createTime + '\'' +
                ", images=" + images +
                '}';
    }
}
