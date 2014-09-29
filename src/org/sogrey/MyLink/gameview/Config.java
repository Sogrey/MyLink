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
	// 设置连连看的每个方块的图片的宽、高
	public static final int PIECE_WIDTH =27;
	public static final int PIECE_HEIGHT = 27;
	// 记录游戏的总事件（100秒）.
	public static int DEFAULT_TIME = 200;	
	// Piece[][]数组第一维的长度
	private int xSize;
	// Piece[][]数组第二维的长度
	private int ySize;
	// Board中第一张图片出现的x座标
	private int beginImageX;
	// Board中第一张图片出现的y座标
	private int beginImageY;
	// 记录游戏的总时间, 单位是秒
	private long gameTime;
	private Context context;

	/**
	 * 提供一个参数构造器
	 * 
	 * @param xSize Piece[][]数组第一维长度
	 * @param ySize Piece[][]数组第二维长度
	 * @param beginImageX Board中第一张图片出现的x座标
	 * @param beginImageY Board中第一张图片出现的y座标
	 * @param gameTime 设置每局的时间, 单位是秒
	 * @param context 应用上下文
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
