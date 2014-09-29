/**
 * 
 */
package org.sogrey.MyLink.gameview;

import android.content.Context;

/**
 * @author Sogrey
 *
 */
public class Config {
	// ������������ÿ�������ͼƬ�Ŀ���
	public static final int PIECE_WIDTH =27;
	public static final int PIECE_HEIGHT = 27;
	// ��¼��Ϸ�����¼���100�룩.
	public static int DEFAULT_TIME = 200;	
	// Piece[][]�����һά�ĳ���
	private int xSize;
	// Piece[][]����ڶ�ά�ĳ���
	private int ySize;
	// Board�е�һ��ͼƬ���ֵ�x����
	private int beginImageX;
	// Board�е�һ��ͼƬ���ֵ�y����
	private int beginImageY;
	// ��¼��Ϸ����ʱ��, ��λ����
	private long gameTime;
	private Context context;

	/**
	 * �ṩһ������������
	 * 
	 * @param xSize Piece[][]�����һά����
	 * @param ySize Piece[][]����ڶ�ά����
	 * @param beginImageX Board�е�һ��ͼƬ���ֵ�x����
	 * @param beginImageY Board�е�һ��ͼƬ���ֵ�y����
	 * @param gameTime ����ÿ�ֵ�ʱ��, ��λ����
	 * @param context Ӧ��������
	 */
	public Config(int xSize, int ySize, int beginImageX,
		int beginImageY, long gameTime, Context context)
	{
		this.xSize = xSize;
		this.ySize = ySize;
		this.beginImageX = beginImageX;
		this.beginImageY = beginImageY;
		this.gameTime = gameTime;
		this.context = context;
	}

	public int getXSize() {
		return xSize;
	}

	public void setXSize(int xSize) {
		this.xSize = xSize;
	}

	public int getYSize() {
		return ySize;
	}

	public void setYSize(int ySize) {
		this.ySize = ySize;
	}

	public int getBeginImageX() {
		return beginImageX;
	}

	public void setBeginImageX(int beginImageX) {
		this.beginImageX = beginImageX;
	}

	public int getBeginImageY() {
		return beginImageY;
	}

	public void setBeginImageY(int beginImageY) {
		this.beginImageY = beginImageY;
	}

	public long getGameTime() {
		return gameTime;
	}

	public void setGameTime(long gameTime) {
		this.gameTime = gameTime;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	
}
