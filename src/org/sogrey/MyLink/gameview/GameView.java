package org.sogrey.MyLink.gameview;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Sogrey
 * 
 */
public class GameView extends View {

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public GameView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GameView(Context context) {
		this(context, null);
	}

	// 游戏逻辑的实现类
	 private GameService gameService;
	// 保存当前已经被选中的方块
	 private Block mSelectedBlock;
	// 连接信息对象
	 private LinkPoint mLinkPoint;
	// 画笔对象
	private Paint mPaint;
	// 选中标识的图片对象
	private Bitmap mSelectImage;
	private MotionEvent mEvent;
	int mPreX = -1, mPreY = -1, mCurX = -1, mCurY = -1;

	public void initView(Context context) {
		this.mPaint = new Paint();
		// 设置连接线的颜色
		this.mPaint.setColor(Color.RED);
		// 设置连接线的粗细
		this.mPaint.setStrokeWidth(3);
		this.mSelectImage = ImageUtil.getSelectImage(context);
	}

	public void setLinkPoint(LinkPoint mLinkPoint)
	{
		this.mLinkPoint = mLinkPoint;
	}

	public void setGameService(GameService gameService)
	{
		this.gameService = gameService;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if (this.gameService == null)
			return;
		Block[][] Blocks = gameService.getBlocks();
		if (Blocks != null)
		{
			// 遍历Blocks二维数组
			for (int i = 0; i < Blocks.length; i++)
			{
				for (int j = 0; j < Blocks[i].length; j++)
				{
					// 如果二维数组中该元素不为空（即有方块），将这个方块的图片画出来
					if (Blocks[i][j] != null)
					{
						// 得到这个Block对象
						Block Block = Blocks[i][j];
						// 根据方块左上角X、Y座标绘制方块
						canvas.drawBitmap(Block.getImage().getImage(),
							Block.getBeginX(), Block.getBeginY(), null);
//						Log.d("draw", "x="+Block.getBeginX()+",y="+Block.getBeginY());
					}
				}
			}
		}
		// 如果当前对象中有mLinkPoint对象, 即连接信息
		if (this.mLinkPoint != null)
		{
			// 绘制连接线
			drawLine(this.mLinkPoint, canvas);
			// 处理完后清空mLinkPoint对象
			this.mLinkPoint = null;
		}
		// 画选中标识的图片
		if (this.mSelectedBlock != null)
		{
			canvas.drawBitmap(this.mSelectImage, this.mSelectedBlock.getBeginX(),
				this.mSelectedBlock.getBeginY(), null);
		}
	}

	// 根据mLinkPoint绘制连接线的方法。
	private void drawLine(LinkPoint mLinkPoint, Canvas canvas)
	{
		// 获取mLinkPoint中封装的所有连接点
		List<Point> points = mLinkPoint.getLinkPoints();
		// 依次遍历mLinkPoint中的每个连接点
		for (int i = 0; i < points.size() - 1; i++)
		{
			// 获取当前连接点与下一个连接点
			Point currentPoint = points.get(i);
			Point nextPoint = points.get(i + 1);
			// 绘制连线
			canvas.drawLine(currentPoint.x , currentPoint.y,
				nextPoint.x, nextPoint.y, this.mPaint);
		}
	}

	// 设置当前选中方块的方法
	public void setSelectedBlock(Block block)
	{
		this.mSelectedBlock = block;
	}

	// 开始游戏方法
	public void startGame()
	{
		this.gameService.start();
		this.postInvalidate();
	}
	/** 点击事件 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getActionMasked() == MotionEvent.ACTION_UP) {// 鼠标抬起时处理
			mEvent = event;
		}
		Touch();
		return true;// 此处返回true表示我们自己已经对触摸事件进行了处理，不希望系统再进行处理
	}

		/** 点击事件处理 */
	private void Touch() {
		if (mEvent != null) {
			if (mPreX == -1 && mPreY == -1) {// 记录第一次点击
				mPreX = (int) mEvent.getX();
				mPreY = (int) mEvent.getY();
				Log.d("mEvent-pre", mPreX + ":" + mPreY);
				// TODO 记录
				return;
			} else {// 记录第二次点击
				mCurX = (int) mEvent.getX();
				mCurY = (int) mEvent.getY();
				Log.d("mEvent-cur", mCurX + ":" + mCurY);
				// 四个方向都要判断
				// TODO 判断有效连接
			}
		}
	}
}
