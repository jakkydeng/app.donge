package nf.framework.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;


public class CircleBitmapDisplayer implements BitmapDisplayer {
    protected final int margin;

    public CircleBitmapDisplayer() {
        this(0);
    }

    public CircleBitmapDisplayer(int margin) {
        this.margin = margin;
    }

    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if(!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        } else {
            imageAware.setImageDrawable(new CircleBitmapDisplayer.CircleDrawable(bitmap, this.margin));
        }
    }

    public class CircleDrawable extends Drawable {
        public static final String TAG = "CircleDrawable";
        protected final Paint paint;
        protected final int margin;
        protected final BitmapShader bitmapShader;
        protected float radius;
        protected Bitmap oBitmap;

        public CircleDrawable(Bitmap bitmap, int margin) {
            this.margin = margin;
            this.oBitmap = bitmap;
            this.bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
            this.paint = new Paint();
            this.paint.setAntiAlias(true);
            this.paint.setShader(this.bitmapShader);
        }

        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            this.computeBitmapShaderSize();
            this.computeRadius();
        }

        public void draw(Canvas canvas) {
            Rect bounds = this.getBounds();
            canvas.drawCircle((float)bounds.width() / 2.0F, (float)bounds.height() / 2.0F, this.radius, this.paint);
        }

        public int getOpacity() {
            return -3;
        }

        public void setAlpha(int alpha) {
            this.paint.setAlpha(alpha);
        }

        public void setColorFilter(ColorFilter cf) {
            this.paint.setColorFilter(cf);
        }

        public void computeBitmapShaderSize() {
            Rect bounds = this.getBounds();
            if(bounds != null) {
                Matrix matrix = new Matrix();
                float scaleX = (float)bounds.width() / (float)this.oBitmap.getWidth();
                float scaleY = (float)bounds.height() / (float)this.oBitmap.getHeight();
                float scale = scaleX > scaleY?scaleX:scaleY;
                matrix.postScale(scale, scale);
                this.bitmapShader.setLocalMatrix(matrix);
            }
        }

        public void computeRadius() {
            Rect bounds = this.getBounds();
            this.radius = bounds.width() < bounds.height()?(float)bounds.width() / 2.0F - (float)this.margin:(float)bounds.height() / 2.0F - (float)this.margin;
        }
    }
}