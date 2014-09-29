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

	// ��Ϸ�߼���ʵ����
	 private GameService gameService;
	// ���浱ǰ�Ѿ���ѡ�еķ���
	 private Block mSelectedBlock;
	// ������Ϣ����
	 private LinkPoint mLinkPoint;
	// ���ʶ���
	private Paint mPaint;
	// ѡ�б�ʶ��ͼƬ����
	private Bitmap mSelectImage;
	private MotionEvent mEvent;
	int mPreX = -1, mPreY = -1, mCurX = -1, mCurY = -1;

	public void initView(Context context) {
		this.mPaint = new Paint();
		// ���������ߵ���ɫ
		this.mPaint.setColor(Color.RED);
		// ���������ߵĴ�ϸ
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
			// ����Blocks��ά����
			for (int i = 0; i < Blocks.length; i++)
			{
				for (int j = 0; j < Blocks[i].length; j++)
				{
					// �����ά�����и�Ԫ�ز�Ϊ�գ����з��飩������������ͼƬ������
					if (Blocks[i][j] != null)
					{
						// �õ����Block����
						Block Block = Blocks[i][j];
						// ���ݷ������Ͻ�X��Y������Ʒ���
						canvas.drawBitmap(Block.getImage().getImage(),
							Block.getBeginX(), Block.getBeginY(), null);
//						Log.d("draw", "x="+Block.getBeginX()+",y="+Block.getBeginY());
					}
				}
			}
		}
		// �����ǰ��������mLinkPoint����, ��������Ϣ
		if (this.mLinkPoint != null)
		{
			// ����������
			drawLine(this.mLinkPoint, canvas);
			// ����������mLinkPoint����
			this.mLinkPoint = null;
		}
		// ��ѡ�б�ʶ��ͼƬ
		if (this.mSelectedBlock != null)
		{
			canvas.drawBitmap(this.mSelectImage, this.mSelectedBlock.getBeginX(),
				this.mSelectedBlock.getBeginY(), null);
		}
	}

	// ����mLinkPoint���������ߵķ�����
	private void drawLine(LinkPoint mLinkPoint, Canvas canvas)
	{
		// ��ȡmLinkPoint�з�װ���������ӵ�
		List<Point> points = mLinkPoint.getLinkPoints();
		// ���α���mLinkPoint�е�ÿ�����ӵ�
		for (int i = 0; i < points.size() - 1; i++)
		{
			// ��ȡ��ǰ���ӵ�����һ�����ӵ�
			Point currentPoint = points.get(i);
			Point nextPoint = points.get(i + 1);
			// ��������
			canvas.drawLine(currentPoint.x , currentPoint.y,
				nextPoint.x, nextPoint.y, this.mPaint);
		}
	}

	// ���õ�ǰѡ�з���ķ���
	public void setSelectedBlock(Block block)
	{
		this.mSelectedBlock = block;
	}

	// ��ʼ��Ϸ����
	public void startGame()
	{
		this.gameService.start();
		this.postInvalidate();
	}
	/** ����¼� */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getActionMasked() == MotionEvent.ACTION_UP) {// ���̧��ʱ����
			mEvent = event;
		}
		Touch();
		return true;// �˴�����true��ʾ�����Լ��Ѿ��Դ����¼������˴�����ϣ��ϵͳ�ٽ��д���
	}

		/** ����¼����� */
	private void Touch() {
		if (mEvent != null) {
			if (mPreX == -1 && mPreY == -1) {// ��¼��һ�ε��
				mPreX = (int) mEvent.getX();
				mPreY = (int) mEvent.getY();
				Log.d("mEvent-pre", mPreX + ":" + mPreY);
				// TODO ��¼
				return;
			} else {// ��¼�ڶ��ε��
				mCurX = (int) mEvent.getX();
				mCurY = (int) mEvent.getY();
				Log.d("mEvent-cur", mCurX + ":" + mCurY);
				// �ĸ�����Ҫ�ж�
				// TODO �ж���Ч����
			}
		}
	}
}
