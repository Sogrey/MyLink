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
			// 创建一个Block集合, 该集合里面存放初始化游戏时所需的Block对象
			List<Block> blocksList = new ArrayList<Block>();
			for (int i = 1; i < Blocks.length - 1; i++)
			{
				for (int j = 1; j < Blocks[i].length - 1; j++)
				{
					// 先构造一个Block对象, 只设置它在Block[][]数组中的索引值，
					// 所需要的BlockImage由其父类负责设置。
					Block Block = new Block(i, j);
					// 添加到Block集合中
					blocksList.add(Block);
				}
			}
			return blocksList;
		}
	public Block[][] create(Config config)
	{
		// 创建Block[][]数组
		Block[][] Blocks = new Block[config.getXSize()][config.getXSize()];
		// 返回非空的Block集合, 该集合由子类去创建
		List<Block> notNullBlocks = createBlocks(config, Blocks);
		// 根据非空Block对象的集合的大小来取图片
		List<BlockImage> playImages = ImageUtil.getPlayImages(config.getContext(),
			notNullBlocks.size());
		// 所有图片的宽、高都是相同的
		int imageWidth = playImages.get(0).getImage().getWidth();
		int imageHeight = playImages.get(0).getImage().getHeight();
		// 遍历非空的Block集合
		for (int i = 0; i < notNullBlocks.size(); i++)
		{
			// 依次获取每个Block对象
			Block Block = notNullBlocks.get(i);
			Block.setImage(playImages.get(i));
			// 计算每个方块左上角的X、Y座标
			Block.setBeginX(Block.getIndexX() * imageWidth
				+ config.getBeginImageX());
			Block.setBeginY(Block.getIndexY() * imageHeight
				+ config.getBeginImageY());
			// 将该方块对象放入方块数组的相应位置处
			Blocks[Block.getIndexX()][Block.getIndexY()] = Block;
		}
		return Blocks;
	}
}
