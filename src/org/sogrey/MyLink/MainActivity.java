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

	/** ʤ���Ի���ID */
	public static final int DIALOG_WIN = 0x100;
	/** ʧ�ܶԻ���ID */
	public static final int DIALOG_LOST = 0x200;
	/** Handle��Ϣ�� */
	public static final int HANDLE_WHAT = 0x400;

	protected TextView mTxtTime;
	protected ProgressBar mPgbTime;
	protected Button mBtnStart;
	private GameView mGameView;
	// ��Ϸ���ö���
	private Config mConfig;
	private GameServiceImpl mGameService;
	private Timer mTimer;
	private boolean mIsPlay;
	// ʣ����Ϸʱ��
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
		// ��ͣ��Ϸ
		stopTimer();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// ���������Ϸ״̬��
		if (mIsPlay) {
			// ��ʣ��ʱ����д��ʼ��Ϸ
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
		// �õ���Ϸ�������
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
		// ��ȡ�������
		float x = event.getX();
		float y = event.getY();
		Log.d("touch", x + "," + y);
		Block curBlocks = mGameService.findBlock(x, y);
		// �����ǰû�е���κ�ͼƬ����ֱ�ӷ���
		if (curBlocks == null) {
			return;
		}
		// ���õ�ǰͼ��ѡ��
		mGameView.setSelectedBlock(curBlocks);
		// ��ѡ��Ϊ�գ���ʾ���ǵ�һ��ѡ��
		if (mBlockSelected == null) {
			mBlockSelected = curBlocks;
			mGameView.postInvalidate();
			return;
		}

		// ��ѡ��Ϊ�գ���ʾ���ǵڶ���ѡ�����������ж�
		if (mBlockSelected != null) {
			LinkPoint linkPoint = mGameService.link(mBlockSelected, curBlocks);
			if (linkPoint == null) {
				mBlockSelected = curBlocks;
				return;
			} else {

				// ǰ��ѡ����ͬһ������ֱ�ӷ���
				if (mBlockSelected.equals(curBlocks))
					return;
				if (!mBlockSelected.isSameImage(curBlocks))
					// �޷����ӣ��ѵ�ǰ����Ϊѡ�п�
					mBlockSelected = curBlocks;
					else
				{// ѡ�����ͬһ��ͼ��ͬ��
					// canLink(linkPoint, mBlockSelected
					// , curBlocks, blocks);

					Point p1Point = mBlockSelected.getCenter();
					// ��ȡp2�����ĵ�
					Point p2Point = curBlocks.getCenter();
					// �������Block��ͬһ��
					if (mBlockSelected.getIndexX() == curBlocks.getIndexX()) {
						// TODO �ж��м������ϰ���
						if (Math.abs(mBlockSelected.getIndexY()
								- curBlocks.getIndexY()) == 1) {// ��������
							canLink(linkPoint, mBlockSelected, curBlocks,
									blocks);
						} else if (mBlockSelected.getIndexY() > curBlocks
								.getIndexY()) {// ����������
//							for (int i = mBlockSelected.getIndexY() - 1; i > curBlocks
//									.getIndexY(); i--) {
								// �м�û���ϰ���,����ֱ��break
//								if (blocks[mBlockSelected.getIndexX()][i] != null) {
//									break;
//								}
//								if (i == curBlocks.getIndexY() + 1) {

									canLink(linkPoint, mBlockSelected,
											curBlocks, blocks);
//								}
//							}
						} else if (mBlockSelected.getIndexY() < curBlocks
								.getIndexY()) {// ����������
//							for (int i = mBlockSelected.getIndexY() + 1; i < curBlocks
//									.getIndexY(); i++) {
//								// �м�û���ϰ���
//								if (blocks[mBlockSelected.getIndexX()][i] == null) {
//									break;
//								}
//								if (i == curBlocks.getIndexY() - 1) {

									canLink(linkPoint, mBlockSelected,
											curBlocks, blocks);
//								}
//							}
						}
						Toast.makeText(this, "ֱ��", Toast.LENGTH_SHORT).show();
					}
					// �������Block��ͬһ��
					else if (mBlockSelected.getIndexY() == curBlocks
							.getIndexY()) {
						// TODO �ж��м������ϰ���
						if (Math.abs(mBlockSelected.getIndexX()
								- curBlocks.getIndexX()) == 1) {// ��������
							canLink(linkPoint, mBlockSelected, curBlocks,
									blocks);
						} else if (mBlockSelected.getIndexX() > curBlocks
								.getIndexX()) {//
//							for (int i = mBlockSelected.getIndexX() - 1; i > curBlocks
//									.getIndexX(); i--) {
//								// �м�û���ϰ���
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
//								// �м�û���ϰ���
//								if (blocks[mBlockSelected.getIndexY()][i] == null) {
//									break;
//								}
//								if (i == curBlocks.getIndexX() - 1) {

									canLink(linkPoint, mBlockSelected,
											curBlocks, blocks);
//								}
//							}
						}
						Toast.makeText(this, "ֱ��", Toast.LENGTH_SHORT).show();
					}
					// TODO �ж�һ���յ�
					// (x2,y1)1 *-----A (x1,y1)
					//          |     |
					//          |     |
					// (x2,y2)  B-----* 2(x1,y2)
					
					// TODO �ж������յ�
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
					else if (mBlockSelected.getIndexX() > curBlocks.getIndexX()) {// ��ǰѡ������ѡ�����
						// �Ϲյ㣨curBlocks.getIndexX()��mBlockSelected.getIndexY()��
						// �ж������������Ƿ�Ϊnull
						// �¹յ㣨mBlockSelected.getIndexX()��curBlocks.getIndexY()��
						// �ж������������Ƿ�Ϊnull
//						Point point = isNullLine(blocks, curBlocks,
//								mBlockSelected);
						canLink(linkPoint, mBlockSelected, curBlocks, blocks);
//						Log.d("point", point.x + ",,," + point.y);
//						Toast.makeText(this,
//								"point" + point.x + ",,," + point.y,
//								Toast.LENGTH_SHORT).show();
					} else if (mBlockSelected.getIndexX() < curBlocks
							.getIndexX()) {// ��ǰѡ������ѡ���ұ�
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
	 * �ж�����֮���Ƿ�Ϊnull
	 * 
	 * @param blocks
	 *            ������������
	 * @param blockA
	 *            ��߸���
	 * @param blockB
	 *            �ұ߸��� Ĭ�� blockA.getIndexX()>blockB.getIndexX()
	 * 
	 *            A--------B
	 * */
	private Point isNullLine(Block[][] blocks, Block blockA, Block blockB) {// blockA��blockB���
		boolean isNullX = false, isNullY = false;
		Point point = null;
		if (blockA.getIndexY() > blockB.getIndexY()) {// ��һ�������ڵڶ����������·�,�Ⱥ����
			// A------*
			// |
			// |
			// B
			for (int i = blockA.getIndexY(); i <= blockB.getIndexY() - 1; i++) {// �ж�����
				if (blocks[blockB.getIndexX()][i] != null) {
					break;
				}
				if (i == blockB.getIndexY() - 1) {
					isNullY = true;
				}
			}
			for (int i = blockA.getIndexX() + 1; i <= blockB.getIndexX(); i++) {// �жϺ���
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
			for (int i = blockA.getIndexY() + 1; i <= blockB.getIndexY(); i++) {// �ж�����
				if (blocks[blockA.getIndexX()][i] != null) {
					break;
				}
				if (i == blockB.getIndexY()) {
					isNullX = true;
				}
			}
			for (int i = blockA.getIndexX(); i <= blockB.getIndexX() - 1; i++) {// �жϺ���
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

		} else {// ��һ�������ڵڶ����������Ϸ�,�Ⱥ����
				// B
				// |
				// |
				// A------*
			for (int i = blockB.getIndexY() + 1; i <= blockA.getIndexY(); i++) {// �ж�����
				if (blocks[blockB.getIndexX()][i] != null) {
					break;
				}
				if (i == blockA.getIndexY()) {
					isNullY = true;
				}
			}
			for (int i = blockA.getIndexX() + 1; i <= blockB.getIndexX(); i++) {// �жϺ���
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
			for (int i = blockB.getIndexY(); i <= blockA.getIndexY() - 1; i++) {// �ж�����
				if (blocks[blockA.getIndexX()][i] != null) {
					break;
				}
				if (i == blockA.getIndexY() - 1) {
					isNullX = true;
				}
			}
			for (int i = blockA.getIndexX(); i <= blockB.getIndexX() - 1; i++) {// �жϺ���
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
	 * �ɹ����Ӻ���
	 * 
	 * @param linkInfo
	 *            ������Ϣ
	 * @param preBlock
	 *            ǰһ��ѡ�з���
	 * @param currentPiece
	 *            ��ǰѡ�񷽿�
	 * @param blocks
	 *            ϵͳ�л�ʣ��ȫ������
	 */
	private void canLink(LinkPoint linkPoint, Block preBlock, Block curBlock,
			Block[][] blocks) {
		mGameView.setLinkPoint(linkPoint);
		// ��gameView�е�ѡ�з�����Ϊnull
		mGameView.setSelectedBlock(null);
		mGameView.postInvalidate();
		// ������Piece�����������ɾ��
		blocks[preBlock.getIndexX()][preBlock.getIndexY()] = null;
		blocks[curBlock.getIndexX()][curBlock.getIndexY()] = null;
		// ��ѡ�еķ�������null��
		mBlockSelected = null;
		// �ж��Ƿ���ʣ�µķ���, ���û��, ��Ϸʤ��
		if (!mGameService.hasBlocks()) {
			// ��Ϸʤ��
			showDialog(DIALOG_WIN);
			// ֹͣ��ʱ��
			stopTimer();
			// ������Ϸ״̬
			mIsPlay = false;
		}
	}

	/** ��ʼ������ */
	private void init() {
		startGame(Config.DEFAULT_TIME);
	}

	private void startGame(int gameTime) {
		// ���֮ǰ��mTimer��δȡ����ȡ��mTimer
		if (this.mTimer != null) {
			stopTimer();
		}
		// ����������Ϸʱ��
		this.mTime = gameTime;
		// �����Ϸʣ��ʱ��������Ϸʱ����ȣ���Ϊ���¿�ʼ����Ϸ
		if (gameTime == Config.DEFAULT_TIME) {
			// ��ʼ�µ���Ϸ��Ϸ
			mGameView.startGame();
		}
		mIsPlay = true;
		this.mTimer = new Timer();
		// ������ʱ�� �� ÿ��1�뷢��һ����Ϣ
		this.mTimer.schedule(new TimerTask() {
			public void run() {
				mHandler.sendEmptyMessage(HANDLE_WHAT);
			}
		}, 0, 1000);
		this.mBlockSelected = null;
	}

	/** ֹͣ��ʱ�� */
	private void stopTimer() {
		// ֹͣ��ʱ��
		this.mTimer.cancel();
		this.mTimer = null;
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case HANDLE_WHAT:
			mTime--;
			mPgbTime.setProgress(mTime);
			// ʱ��С��0, ��Ϸʧ��
			if (mTime < 0) {
				stopTimer();
				// ������Ϸ��״̬
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
		case DIALOG_WIN:// ʤ��
			builder.setTitle("WIN");
			builder.setMessage("��Ϸʤ�������¿�ʼ");
			break;
		case DIALOG_LOST:// ʧ��
			builder.setTitle("LOST");
			builder.setMessage("��Ϸʧ�ܣ����¿�ʼ");
			break;

		default:
			break;
		}
		builder.setNegativeButton("ȡ��", null);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				startGame(Config.DEFAULT_TIME);
			}
		});
		return builder.create();
	}

}
