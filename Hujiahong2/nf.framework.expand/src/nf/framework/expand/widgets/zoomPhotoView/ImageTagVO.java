package nf.framework.expand.widgets.zoomPhotoView;

import java.io.Serializable;

import android.graphics.Rect;
import android.graphics.RectF;

public class ImageTagVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7375145002634100419L;

	String name;
	
	int x,y;
	
	int style;
	
	String link;
	
	RectF rectF;
	
	float scale=1.0f;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return (int) (this.scale*x);
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return (int) (this.scale*y);
	}

	public void setY(int y) {
		this.y =y;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public RectF getRectF() {
		return rectF;
	}

	public void setRectF(RectF rectF) {
		this.rectF = rectF;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
}
