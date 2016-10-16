package utils;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import static nf.framework.fragment.AbsListAdapter.circleOptions;

/**
 * Created by hujiahong on 16/10/16.
 */

public class ImageManger {
    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new FadeInBitmapDisplayer(0))
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .build();
    public static void asyncLoadImage(ImageView imageView, String imagePath) {
        asyncLoadImage(imageView, imagePath,null);
    }
    public static void asyncLoadCircleImage(ImageView imageView, String imagePath) {
        ImageLoader.getInstance().displayImage(imagePath, imageView,circleOptions);
    }
    public static void asyncLoadImage(ImageView imageView, String imagePath,ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(imagePath, imageView,options,listener);
    }
}
