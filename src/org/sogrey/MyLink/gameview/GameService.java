package org.sogrey.MyLink.gameview;

/**
 * 游戏逻辑接口
 */
public interface GameService {
	/**
	 * 控制游戏开始的方法
	 */
	void start();

	/**
	 * 定义一个接口方法, 用于返回一个二维数组
	 * 
	 * @return 存放方块对象的二维数组
	 */
	Block[][] getBlocks();

	/**
	 * 判断参数Block[][]数组中是否还存在非空的Block对象
	 * 
	 * @return 如果还剩Block对象返回true, 没有返回false
	 */
	boolean hasBlocks();

	/**
	 * 根据鼠标的x座标和y座标, 查找出一个Block对象
	 * 
	 * @param touchX
	 *            鼠标点击的x座标
	 * @param touchY
	 *            鼠标点击的y座标
	 * @return 返回对应的Block对象, 没有返回null
	 */
	Block findBlock(float touchX, float touchY);

	/**
	 * 判断两个Block是否可以相连, 可以连接, 返回LinkInfo对象
	 * 
	 * @param p1
	 *            第一个Block对象
	 * @param p2
	 *            第二个Block对象
	 * @return 如果可以相连，返回LinkInfo对象, 如果两个Block不可以连接, 返回null
	 */
	LinkPoint link(Block p1, Block p2);
}
