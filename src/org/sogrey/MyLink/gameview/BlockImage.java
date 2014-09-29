/**
 * 
 */
package org.sogrey.MyLink.gameview;

import android.graphics.Bitmap;

/**
 * @author Sogrey
 *
 */
public class BlockImage {
	private Bitmap image;
	private int imageId;
	// 有参数的构造器
	public BlockImage(Bitmap image, int imageId)
	{
		super();
		this.image = image;
		this.imageId = imageId;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	
}
