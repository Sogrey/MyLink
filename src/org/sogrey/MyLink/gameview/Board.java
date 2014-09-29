package org.sogrey.MyLink.gameview;

import java.util.ArrayList;
import java.util.List;
import org.sogrey.MyLink.gameview.Config;;

/**
 * @author Sogrey
 *
 */
public class Board {
	protected List<Block> createBlocks(Config config,
			Block[][] Blocks)
		{
			// ����һ��Block����, �ü��������ų�ʼ����Ϸʱ�����Block����
			List<Block> blocksList = new ArrayList<Block>();
			for (int i = 1; i < Blocks.length - 1; i++)
			{
				for (int j = 1; j < Blocks[i].length - 1; j++)
				{
					// �ȹ���һ��Block����, ֻ��������Block[][]�����е�����ֵ��
					// ����Ҫ��BlockImage���丸�ฺ�����á�
					Block Block = new Block(i, j);
					// ��ӵ�Block������
					blocksList.add(Block);
				}
			}
			return blocksList;
		}
	public Block[][] create(Config config)
	{
		// ����Block[][]����
		Block[][] Blocks = new Block[config.getXSize()][config.getXSize()];
		// ���طǿյ�Block����, �ü���������ȥ����
		List<Block> notNullBlocks = createBlocks(config, Blocks);
		// ���ݷǿ�Block����ļ��ϵĴ�С��ȡͼƬ
		List<BlockImage> playImages = ImageUtil.getPlayImages(config.getContext(),
			notNullBlocks.size());
		// ����ͼƬ�Ŀ��߶�����ͬ��
		int imageWidth = playImages.get(0).getImage().getWidth();
		int imageHeight = playImages.get(0).getImage().getHeight();
		// �����ǿյ�Block����
		for (int i = 0; i < notNullBlocks.size(); i++)
		{
			// ���λ�ȡÿ��Block����
			Block Block = notNullBlocks.get(i);
			Block.setImage(playImages.get(i));
			// ����ÿ���������Ͻǵ�X��Y����
			Block.setBeginX(Block.getIndexX() * imageWidth
				+ config.getBeginImageX());
			Block.setBeginY(Block.getIndexY() * imageHeight
				+ config.getBeginImageY());
			// ���÷��������뷽���������Ӧλ�ô�
			Blocks[Block.getIndexX()][Block.getIndexY()] = Block;
		}
		return Blocks;
	}
}
