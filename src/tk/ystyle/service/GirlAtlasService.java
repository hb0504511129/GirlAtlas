package tk.ystyle.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tk.ystyle.entity.Image;
import tk.ystyle.entity.ImageAndFile;
import tk.ystyle.entity.PhotoAlbum;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 小奕 on 2014-08-08 13:06.
 */
public class GirlAtlasService {
    /**
     * 缓存
     */
    Map<String, String> cookies;

    public String getBaseUrl() {
        return "http://girl-atlas.com/";
    }

    public Map<String, String> getCookies() {
        return cookies.size()>0?cookies:login("lxy5266@live.com", "lxy506647253", "on");
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> login(String userName, String passwd, String rememberMe) {
        Connection.Response res = null;
        try {
            res = Jsoup.connect(getBaseUrl()+"j_spring_security_check")
                    .data(
                            "j_username", userName,
                            "j_password", passwd,
                            "_spring_security_remember_me", rememberMe
                            )
                    .method(Connection.Method.POST)
                    .timeout(1000*30)
                    .userAgent("Mozilla/5.0 (ArchLinux Linux 3.16) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.0 Chrome/30.0.1599.101 Safari/537.36")
                    .execute();
            setCookies(res.cookies());
            return cookies;
        } catch (IOException e) {
            System.out.println("登陆失败！！！");
            return null;
        }
    }

    public List<PhotoAlbum> getPhotoAlbums(String nextURL){
        List<PhotoAlbum> photoAlbums = new ArrayList<PhotoAlbum>();
        try {
            Document doc = getDocument(nextURL);
            Elements elements = doc.select("div.column.grid_6.grid");
            for (Element element : elements) {
                PhotoAlbum  photoAlbum = new PhotoAlbum();
                String title = element.attr("title");
                String url = element.select("div.bubble.play > a").first().attr("href");
                String mainImg = element.select("  div.column.grid_6.cell > a").first().attr("photo");
                String number = element.select("div.column.grid_6.bar > .byline > a").first().text();
                String createTime = element.select("div.column.grid_6.bar > .byline > .date").first().text();
                photoAlbum.setTitle(title);
                photoAlbum.setUrl(url);
                photoAlbum.setMainImg(mainImg);
                photoAlbum.setNumber(number);
                photoAlbum.setCreateTime(createTime);
                photoAlbums.add(photoAlbum);
            }
        } catch (IOException e) {
            System.out.println("获取相册失败！！！");
            return photoAlbums;
        }
        return photoAlbums;
    }

    private Document getDocument(String nextURL) throws IOException {
        return Jsoup.connect(nextURL)
                        .cookies(getCookies())
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (ArchLinux Linux 3.16) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.0 Chrome/30.0.1599.101 Safari/537.36")
                        .get();
    }

    public String getNextPagesUrl(String url){
        String nextURL=null;
        try {
            Document doc = getDocument(url);
           Element element =doc.select("a.btn-form.next").first();

            if (element!=null){
                nextURL = element.absUrl("href");//.attr("href");
            }
            return  nextURL;
        } catch (IOException e) {
            System.out.println("获取下一页的URL失败！！！");
            return nextURL;
        }
    }

    public  List<String> getAllPagesURL(){
        List<String> urls=new ArrayList<>();
        String nextUrl=getBaseUrl();
        do{
            urls.add(nextUrl);
            nextUrl = getNextPagesUrl(nextUrl);
        }while (nextUrl!=null && !"".equals(nextUrl));
        return urls;
    }

    public PhotoAlbum getAPhotoAlbumImages(PhotoAlbum photoAlbum){
        String url = photoAlbum.getUrl();
        try {
            Document doc = getDocument(url);
            Elements elements =doc.select("li.slide");
            List<Image> images=new ArrayList<>();
            for (Element element : elements) {
                Image image=new Image();
                String index = element.attr("index");
                String src = element.select("img").first().attr("src");
                if (src==null || "".equals(src)){
                    src = element.select("img").first().attr("delay");
                }
                image.setIndex(index);
                image.setUrl(src);
                images.add(image);
            }
            photoAlbum.setImages(images);
            return  photoAlbum;
        } catch (IOException e) {
            System.out.println("获取<<"+photoAlbum.getTitle()+">>图片失败！！！");
            return photoAlbum;
        }
    }

    public void downLoadImage(ImageAndFile imageAndFile){
        try {
            Connection.Response res = Jsoup.connect(imageAndFile.getUrl())
                   .timeout(30000)
                   .userAgent("Mozilla/5.0 (ArchLinux Linux 3.16) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.0 Chrome/30.0.1599.101 Safari/537.36")
                   .method(Connection.Method.GET)
                    .ignoreContentType(true)
                   .execute();
            byte[] image = res.bodyAsBytes();
            File file = new File(imageAndFile.getFilename());
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            FileImageOutputStream outputStream=new FileImageOutputStream(file);
            outputStream.write(image);
        } catch (IOException e) {
            System.out.println("下载图片失败！！！");
        }
    }

    public List<ImageAndFile> downloadImagesByPhotoAlbum(PhotoAlbum photoAlbum){
        List<ImageAndFile> urls = new ArrayList<>();
        String title = photoAlbum.getTitle();
        String category=null;
        if(title.indexOf("[")!=-1 || title.lastIndexOf("]")!=-1){
            category = title.substring(title.indexOf("[")+1, title.lastIndexOf("]"));
        }else {
            category = photoAlbum.getCreateTime();
        }

        String url = photoAlbum.getMainImg();
        ImageAndFile imageAndFile=new ImageAndFile(url,"./GirlAtlas/"+category+"/"+title+"/index.jpg");
        urls.add(imageAndFile);
        List<Image> images = photoAlbum.getImages();
        for (Image image : images) {
            StringBuffer str = new StringBuffer("./GirlAtlas/");
            str.append(category);
            str.append("/");
            str.append(title);
            str.append("/");
            str.append(image.getIndex());
            str.append(".jpg");
            ImageAndFile imageFile=new ImageAndFile(image.getUrl(),str.toString());
            urls.add(imageFile);
        }
        return urls;
    }
}
