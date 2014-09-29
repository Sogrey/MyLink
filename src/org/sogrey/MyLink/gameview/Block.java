package org.sogrey.MyLink.gameview;

import android.graphics.Point;


/**
 * @author Sogrey
 *
 */
public class Block {
	// ���淽����������Ӧ��ͼƬ
	private BlockImage image;
	// �÷�������Ͻǵ�x����
	private int beginX;
	// �÷�������Ͻǵ�y����
	private int beginY;
	// �ö�����Piece[][]�����е�һά������ֵ
	private int indexX;
	// �ö�����Piece[][]�����еڶ�ά������ֵ
	private int indexY;

	// ֻ���ø�Piece�����������е�����ֵ
	public Block(int indexX , int indexY)
	{
		this.indexX = indexX;
		this.indexY = indexY;
	}

	public int getBeginX() {
		return beginX;
	}

	public void setBeginX(int beginX) {
		this.beginX = beginX;
	}

	public int getBeginY() {
		return beginY;
	}

	public void setBeginY(int beginY) {
		this.beginY = beginY;
	}

	public int getIndexX() {
		return indexX;
	}

	public void setIndexX(int indexX) {
		this.indexX = indexX;
	}

	public int getIndexY() {
		return indexY;
	}

	public void setIndexY(int indexY) {
		this.indexY = indexY;
	}

	public BlockImage getImage() {
		return image;
	}

	public void setImage(BlockImage image) {
		this.image = image;
	}

	// ��ȡ��Block������
	public Point getCenter()
	{
		return new Point(getImage().getImage().getWidth() / 2
			+ getBeginX(), getBeginY()
			+ getImage().getImage().getHeight() / 2);
	}	
	// �ж�����Block�ϵ�ͼƬ�Ƿ���ͬ
	public boolean isSameImage(Block other)
	{
		if (image == null)
		{
			if (other.image != null)
				return false;
		}
		// ֻҪBlock��װͼƬID��ͬ��������Ϊ����Block��ȡ�
		return image.getImageId() == other.image.getImageId();
	}
	
	
}
