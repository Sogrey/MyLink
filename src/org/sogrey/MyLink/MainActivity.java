package org.sogrey.MyLink;

import java.util.Timer;
import java.util.TimerTask;

import org.sogrey.MyLink.gameview.Block;
import org.sogrey.MyLink.gameview.Config;
import org.sogrey.MyLink.gameview.GameServiceImpl;
import org.sogrey.MyLink.gameview.GameView;
import org.sogrey.MyLink.gameview.LinkPoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		Callback, OnTouchListener {

	/** 胜利对话框ID */
	public static final int DIALOG_WIN = 0x100;
	/** 失败对话框ID */
	public static final int DIALOG_LOST = 0x200;
	/** Handle消息码 */
	public static final int HANDLE_WHAT = 0x400;

	protected TextView mTxtTime;
	protected ProgressBar mPgbTime;
	protected Button mBtnStart;
	private GameView mGameView;
	// 游戏配置对象
	private Config mConfig;
	private GameServiceImpl mGameService;
	private Timer mTimer;
	private boolean mIsPlay;
	// 剩余游戏时间
	private int mTime;
	private Block mBlockSelected = null;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initComps();
		initView();

	}

	@Override
	protected void onPause() {
		// 暂停游戏
		stopTimer();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// 如果处于游戏状态中
		if (mIsPlay) {
			// 以剩余时间重写开始游戏
			startGame(mTime);
		}
		super.onResume();
	}

	private void initComps() {
		mTimer = new Timer();
		mHandler = new Handler(this);
	}

	private void initView() {
		mTxtTime = (TextView) findViewById(R.id.txt_has_time);
		mPgbTime = (ProgressBar) findViewById(R.id.pgb_has_time);
		mPgbTime.setMax(Config.DEFAULT_TIME);

		mConfig = new Config(11, 11, 2, 2, 100000, this);
		// 得到游戏区域对象
		mGameView = (GameView) findViewById(R.id.view_gameView);
		mBtnStart = (Button) findViewById(R.id.btn_start);
		mBtnStart.setOnClickListener(this);
		mGameService = new GameServiceImpl(this.mConfig);
		mGameView.setGameService(mGameService);
		mGameView.setOnTouchListener(this);

	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_start:
			init();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			touchDown(event);
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			touchUp(event);
		}
		return true;
	}

	private void touchUp(MotionEvent event) {
		mGameView.postInvalidate();
	}

	private void touchDown(MotionEvent event) {
		Block[][] blocks = mGameService.getBlocks();
		// 获取点击坐标
		float x = event.getX();
		float y = event.getY();
		Log.d("touch", x + "," + y);
		Block curBlocks = mGameService.findBlock(x, y);
		// 如果当前没有点击任何图片，则直接返回
		if (curBlocks == null) {
			return;
		}
		// 设置当前图块选中
		mGameView.setSelectedBlock(curBlocks);
		// 已选择为空，表示这是第一次选择
		if (mBlockSelected == null) {
			mBlockSelected = curBlocks;
			mGameView.postInvalidate();
			return;
		}

		// 已选择不为空，表示这是第二次选择，需做连线判断
		if (mBlockSelected != null) {
			LinkPoint linkPoint = mGameService.link(mBlockSelected, curBlocks);
			if (linkPoint == null) {
				mBlockSelected = curBlocks;
				return;
			} else {

				// 前后选择是同一个，则直接返回
				if (mBlockSelected.equals(curBlocks))
					return;
				if (!mBlockSelected.isSameImage(curBlocks))
					// 无法连接，把当前块设为选中块
					mBlockSelected = curBlocks;
					else
				{// 选择的是同一幅图不同块
					// canLink(linkPoint, mBlockSelected
					// , curBlocks, blocks);

					Point p1Point = mBlockSelected.getCenter();
					// 获取p2的中心点
					Point p2Point = curBlocks.getCenter();
					// 如果两个Block在同一行
					if (mBlockSelected.getIndexX() == curBlocks.getIndexX()) {
						// TODO 判断中间有无障碍物
						if (Math.abs(mBlockSelected.getIndexY()
								- curBlocks.getIndexY()) == 1) {// 两个相邻
							canLink(linkPoint, mBlockSelected, curBlocks,
									blocks);
						} else if (mBlockSelected.getIndexY() > curBlocks
								.getIndexY()) {// 两个不相邻
//							for (int i = mBlockSelected.getIndexY() - 1; i > curBlocks
//									.getIndexY(); i--) {
								// 中间没有障碍物,有则直接break
//								if (blocks[mBlockSelected.getIndexX()][i] != null) {
//									break;
//								}
//								if (i == curBlocks.getIndexY() + 1) {

									canLink(linkPoint, mBlockSelected,
											curBlocks, blocks);
//								}
//							}
						} else if (mBlockSelected.getIndexY() < curBlocks
								.getIndexY()) {// 两个不相邻
//							for (int i = mBlockSelected.getIndexY() + 1; i < curBlocks
//									.getIndexY(); i++) {
//								// 中间没有障碍物
//								if (blocks[mBlockSelected.getIndexX()][i] == null) {
//									break;
//								}
//								if (i == curBlocks.getIndexY() - 1) {

									canLink(linkPoint, mBlockSelected,
											curBlocks, blocks);
//								}
//							}
						}
						Toast.makeText(this, "直连", Toast.LENGTH_SHORT).show();
					}
					// 如果两个Block在同一列
					else if (mBlockSelected.getIndexY() == curBlocks
							.getIndexY()) {
						// TODO 判断中间有无障碍物
						if (Math.abs(mBlockSelected.getIndexX()
								- curBlocks.getIndexX()) == 1) {// 两个相邻
							canLink(linkPoint, mBlockSelected, curBlocks,
									blocks);
						} else if (mBlockSelected.getIndexX() > curBlocks
								.getIndexX()) {//
//							for (int i = mBlockSelected.getIndexX() - 1; i > curBlocks
//									.getIndexX(); i--) {
//								// 中间没有障碍物
//								if (blocks[mBlockSelected.getIndexY()][i] == null) {
//									break;
//								}
//								if (i == curBlocks.getIndexX() + 1) {

									canLink(linkPoint, mBlockSelected,
											curBlocks, blocks);
//								}
//							}
						} else if (mBlockSelected.getIndexX() < curBlocks
								.getIndexX()) {//
//							for (int i = mBlockSelected.getIndexX() + 1; i < curBlocks
//									.getIndexX(); i++) {
//								// 中间没有障碍物
//								if (blocks[mBlockSelected.getIndexY()][i] == null) {
//									break;
//								}
//								if (i == curBlocks.getIndexX() - 1) {

									canLink(linkPoint, mBlockSelected,
											curBlocks, blocks);
//								}
//							}
						}
						Toast.makeText(this, "直连", Toast.LENGTH_SHORT).show();
					}
					// TODO 判断一个拐点
					// (x2,y1)1 *-----A (x1,y1)
					//          |     |
					//          |     |
					// (x2,y2)  B-----* 2(x1,y2)
					
					// TODO 判断两个拐点
					//         6*-----*6'
					//          |  5' |
					//    1 *------*--A---*4'    
					//      |   |  |  |   |
					//      |   |  |  |   |
					//      |  3*--|--*3' |
					//      |   |  |  |   |
					//   1' *---B--*--|---*4 
					//          |  5  |
					//        2 *-----*2'
					//      
					else if (mBlockSelected.getIndexX() > curBlocks.getIndexX()) {// 当前选择在已选择左边
						// 上拐点（curBlocks.getIndexX()，mBlockSelected.getIndexY()）
						// 判断这两条线上是否都为null
						// 下拐点（mBlockSelected.getIndexX()，curBlocks.getIndexY()）
						// 判断这两条线上是否都为null
//						Point point = isNullLine(blocks, curBlocks,
//								mBlockSelected);
						canLink(linkPoint, mBlockSelected, curBlocks, blocks);
//						Log.d("point", point.x + ",,," + point.y);
//						Toast.makeText(this,
//								"point" + point.x + ",,," + point.y,
//								Toast.LENGTH_SHORT).show();
					} else if (mBlockSelected.getIndexX() < curBlocks
							.getIndexX()) {// 当前选择在已选择右边
//						Point point = isNullLine(blocks, mBlockSelected,
//								curBlocks);
						canLink(linkPoint, mBlockSelected, curBlocks, blocks);
//						Log.d("point", point.x + ",,," + point.y);
//						Toast.makeText(this,
//								"point" + point.x + ",,," + point.y,
//								Toast.LENGTH_SHORT).show();
					}
				} 

			}
		}
	}

	/**
	 * 判断连点之间是否都为null
	 * 
	 * @param blocks
	 *            整个格子数组
	 * @param blockA
	 *            左边格子
	 * @param blockB
	 *            右边格子 默认 blockA.getIndexX()>blockB.getIndexX()
	 * 
	 *            A--------B
	 * */
	private Point isNullLine(Block[][] blocks, Block blockA, Block blockB) {// blockA在blockB左边
		boolean isNullX = false, isNullY = false;
		Point point = null;
		if (blockA.getIndexY() > blockB.getIndexY()) {// 第一个格子在第二个格子右下方,先横后竖
			// A------*
			// |
			// |
			// B
			for (int i = blockA.getIndexY(); i <= blockB.getIndexY() - 1; i++) {// 判断竖行
				if (blocks[blockB.getIndexX()][i] != null) {
					break;
				}
				if (i == blockB.getIndexY() - 1) {
					isNullY = true;
				}
			}
			for (int i = blockA.getIndexX() + 1; i <= blockB.getIndexX(); i++) {// 判断横行
				if (blocks[i][blockA.getIndexY()] != null) {
					break;
				}
				if (i == blockB.getIndexX()) {
					isNullX = true;
				}
			}
			if (isNullY && isNullX) {
				return point = blocks[blockB.getIndexX()][blockA.getIndexY()]
						.getCenter();
			}
			isNullX = false;
			isNullY = false;
			// A
			// |
			// |
			// *------B
			for (int i = blockA.getIndexY() + 1; i <= blockB.getIndexY(); i++) {// 判断竖行
				if (blocks[blockA.getIndexX()][i] != null) {
					break;
				}
				if (i == blockB.getIndexY()) {
					isNullX = true;
				}
			}
			for (int i = blockA.getIndexX(); i <= blockB.getIndexX() - 1; i++) {// 判断横行
				if (blocks[i][blockB.getIndexY()] != null) {
					break;
				}
				if (i == blockB.getIndexX() - 1) {
					isNullY = true;
				}
			}
			if (isNullY && isNullX) {
				return point = blocks[blockA.getIndexX()][blockB.getIndexY()]
						.getCenter();
			}
			isNullX = false;
			isNullY = false;

		} else {// 第一个格子在第二个格子右上方,先横后竖
				// B
				// |
				// |
				// A------*
			for (int i = blockB.getIndexY() + 1; i <= blockA.getIndexY(); i++) {// 判断竖行
				if (blocks[blockB.getIndexX()][i] != null) {
					break;
				}
				if (i == blockA.getIndexY()) {
					isNullY = true;
				}
			}
			for (int i = blockA.getIndexX() + 1; i <= blockB.getIndexX(); i++) {// 判断横行
				if (blocks[i][blockA.getIndexY()] != null) {
					break;
				}
				if (i == blockB.getIndexX()) {
					isNullX = true;
				}
			}
			if (isNullY && isNullX) {
				return point = blocks[blockB.getIndexX()][blockA.getIndexY()]
						.getCenter();
			}
			isNullX = false;
			isNullY = false;
			// *------B
			// |
			// |
			// A
			for (int i = blockB.getIndexY(); i <= blockA.getIndexY() - 1; i++) {// 判断竖行
				if (blocks[blockA.getIndexX()][i] != null) {
					break;
				}
				if (i == blockA.getIndexY() - 1) {
					isNullX = true;
				}
			}
			for (int i = blockA.getIndexX(); i <= blockB.getIndexX() - 1; i++) {// 判断横行
				if (blocks[i][blockB.getIndexY()] != null) {
					break;
				}
				if (i == blockB.getIndexX() - 1) {
					isNullY = true;
				}
			}
			if (isNullY && isNullX) {
				return point = blocks[blockA.getIndexX()][blockB.getIndexY()]
						.getCenter();
			}
		}
		return point;
	}

	/**
	 * 成功连接后处理
	 * 
	 * @param linkInfo
	 *            连接信息
	 * @param preBlock
	 *            前一个选中方块
	 * @param currentPiece
	 *            当前选择方块
	 * @param blocks
	 *            系统中还剩的全部方块
	 */
	private void canLink(LinkPoint linkPoint, Block preBlock, Block curBlock,
			Block[][] blocks) {
		mGameView.setLinkPoint(linkPoint);
		// 将gameView中的选中方块设为null
		mGameView.setSelectedBlock(null);
		mGameView.postInvalidate();
		// 将两个Piece对象从数组中删除
		blocks[preBlock.getIndexX()][preBlock.getIndexY()] = null;
		blocks[curBlock.getIndexX()][curBlock.getIndexY()] = null;
		// 将选中的方块设置null。
		mBlockSelected = null;
		// 判断是否还有剩下的方块, 如果没有, 游戏胜利
		if (!mGameService.hasBlocks()) {
			// 游戏胜利
			showDialog(DIALOG_WIN);
			// 停止定时器
			stopTimer();
			// 更改游戏状态
			mIsPlay = false;
		}
	}

	/** 初始化界面 */
	private void init() {
		startGame(Config.DEFAULT_TIME);
	}

	private void startGame(int gameTime) {
		// 如果之前的mTimer还未取消，取消mTimer
		if (this.mTimer != null) {
			stopTimer();
		}
		// 重新设置游戏时间
		this.mTime = gameTime;
		// 如果游戏剩余时间与总游戏时间相等，即为重新开始新游戏
		if (gameTime == Config.DEFAULT_TIME) {
			// 开始新的游戏游戏
			mGameView.startGame();
		}
		mIsPlay = true;
		this.mTimer = new Timer();
		// 启动计时器 ， 每隔1秒发送一次消息
		this.mTimer.schedule(new TimerTask() {
			public void run() {
				mHandler.sendEmptyMessage(HANDLE_WHAT);
			}
		}, 0, 1000);
		this.mBlockSelected = null;
	}

	/** 停止计时器 */
	private void stopTimer() {
		// 停止定时器
		this.mTimer.cancel();
		this.mTimer = null;
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case HANDLE_WHAT:
			mTime--;
			mPgbTime.setProgress(mTime);
			// 时间小于0, 游戏失败
			if (mTime < 0) {
				stopTimer();
				// 更改游戏的状态
				mIsPlay = false;
				showDialog(DIALOG_LOST);
				break;
			}
			break;
		}
		return true;//
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case DIALOG_WIN:// 胜利
			builder.setTitle("WIN");
			builder.setMessage("游戏胜利，重新开始");
			break;
		case DIALOG_LOST:// 失败
			builder.setTitle("LOST");
			builder.setMessage("游戏失败，重新开始");
			break;

		default:
			break;
		}
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				startGame(Config.DEFAULT_TIME);
			}
		});
		return builder.create();
	}

}
