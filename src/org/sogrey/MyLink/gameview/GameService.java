package org.sogrey.MyLink.gameview;

/**
 * ��Ϸ�߼��ӿ�
 */
public interface GameService {
	/**
	 * ������Ϸ��ʼ�ķ���
	 */
	void start();

	/**
	 * ����һ���ӿڷ���, ���ڷ���һ����ά����
	 * 
	 * @return ��ŷ������Ķ�ά����
	 */
	Block[][] getBlocks();

	/**
	 * �жϲ���Block[][]�������Ƿ񻹴��ڷǿյ�Block����
	 * 
	 * @return �����ʣBlock���󷵻�true, û�з���false
	 */
	boolean hasBlocks();

	/**
	 * ��������x�����y����, ���ҳ�һ��Block����
	 * 
	 * @param touchX
	 *            �������x����
	 * @param touchY
	 *            �������y����
	 * @return ���ض�Ӧ��Block����, û�з���null
	 */
	Block findBlock(float touchX, float touchY);

	/**
	 * �ж�����Block�Ƿ��������, ��������, ����LinkInfo����
	 * 
	 * @param p1
	 *            ��һ��Block����
	 * @param p2
	 *            �ڶ���Block����
	 * @return �����������������LinkInfo����, �������Block����������, ����null
	 */
	LinkPoint link(Block p1, Block p2);
}
