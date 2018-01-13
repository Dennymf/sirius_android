package ru.mail.techotrack.lection8;

import android.media.Image;
import android.provider.ContactsContract;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vlad on 07/04/16.
 */
public class ImageData {


   public static class Image {
      private String url;
      private String descr;

      public Image(String url) {
         this.url = url;
         this.descr = url;
      }

      public String getImage() {
         return url;
      }
      public String getText() {
         return descr;
      }
   }

   private List<Image> list;

   private static ImageData _instance;
   protected ImageData() {}

   public static void createInstance() {
     if (null == _instance) {
        _instance = new ImageData();
        _instance.init();
     }
   }

   static public ImageData instance() {
      return _instance;
   }

   public List<Image> getImages() {
      return list;
   }

   public Image getImage(int pos) {
      return list.get(pos);
   }

   private void init() {
      list = new ArrayList<>();
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/omar/normal/1.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/omar/normal/2.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/omar/normal/3.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/omar/normal/4.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/omar/normal/5.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/omar/normal/6.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/omar/normal/7.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/omar/normal/8.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/drewer/normal/1.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/drewer/normal/2.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/drewer/normal/3.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/drewer/normal/4.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/drewer/normal/5.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/drewer/normal/6.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/drewer/normal/7.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/drewer/normal/8.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/drewer/normal/9.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/drewer/normal/10.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ted/normal/Ted-Lansing-01.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ted/normal/Ted-Lansing-02.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ted/normal/Ted-Lansing-03.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ted/normal/Ted-Lansing-04.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ted/normal/Ted-Lansing-05.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ted/normal/Ted-Lansing-06.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ted/normal/Ted-Lansing-07.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ted/normal/Ted-Lansing-08.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-01.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-02.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-03.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-04.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-05.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-06.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-07.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-08.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-09.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-10.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-11.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-12.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-13.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-14.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-15.jpg"));
      list.add(new Image("http://images.hdrsoft.com/gallery/photographers/images_squared/ducsu/normal/photomatix-gallery-ducsu-16.jpg"));
   }
}
