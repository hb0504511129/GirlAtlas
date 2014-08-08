package tk.ystyle;

import tk.ystyle.entity.ImageAndFile;
import tk.ystyle.entity.PhotoAlbum;
import tk.ystyle.service.GirlAtlasService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        GirlAtlasService service=new GirlAtlasService();
        service.login("lxy5266@live.com", "lxy506647253", "on");
        List<String> urls=service.getAllPagesURL();
        for (String url : urls) {
            List<PhotoAlbum> photoAlbums =service.getPhotoAlbums(url);
            for (PhotoAlbum photoAlbum : photoAlbums) {
                photoAlbum=service.getAPhotoAlbumImages(photoAlbum);
                List<ImageAndFile> imageAndFiles= service.downloadImagesByPhotoAlbum(photoAlbum);
                for (ImageAndFile file : imageAndFiles) {
                    service.downLoadImage(file);
                }
            }
        }
    }
}
