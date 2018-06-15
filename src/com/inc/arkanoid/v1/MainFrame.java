package com.inc.arkanoid.v1;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	// 프레임 정보 및 이벤트리스너
	public static final int W = 600;
	public static final int H = 700;

	JPanel mainPanel;
	Block block;
	Pad pad;
	Ball ball;

	int score = 0;
	int level = 1;

	public MainFrame() {
		setTitle("ARKANOID");
		setBounds(100, 100, W, H);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		init();

		setVisible(true);
	}

	private void init() {
		initInitialize();
		initComponent();
		initEvent();
	}

	private void initInitialize() {
		block = new Block(level);
		pad = new Pad();
		ball = new Ball();

		// pad 초기 위치.
		Point padPoint = new Point((W - 100) / 2 - pad.PAD_W / 2, H - 100);
		pad.setPadPoint(padPoint);

		Point ballPoint = new Point(padPoint.x + pad.PAD_W / 2 - ball.BALL_SIZE / 2, padPoint.y - ball.BALL_SIZE);
		ball.setP(ballPoint);
	}

	private void initComponent() {

		mainPanel = new JPanel() {

			@Override
			protected void paintComponent(Graphics g) {
				g.clearRect(0, 0, W, H);
				synchronized (block.pointList) {
					for (Point p : block.pointList) {
						synchronized (block.pointList) {
							g.drawRect(p.x, p.y, Block.BLOCK_W, Block.BLOCK_H);
							collision(p, Block.BLOCK_W, Block.BLOCK_H);
						}
					}
				}
				g.drawRect(pad.getPadPoint().x, pad.getPadPoint().y, pad.PAD_W, pad.PAD_H);
				g.drawOval(ball.getP().x, ball.getP().y, Ball.BALL_SIZE, Ball.BALL_SIZE);

				g.drawRect(500, 0, 100, H); // 레벨, 점수판 위치
				g.drawString("레벨: " + level, 510, 50);
				g.drawString("점수: " + score, 510, 100);
			}

		};

		add(mainPanel);
	}

	private void initEvent() {
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					if (pad.getPadPoint().x >= 0) {
						pad.moveL();
					}
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if (pad.getPadPoint().x + pad.PAD_W <= W - 110) {
						pad.moveR();
					}
				}

				if (ball.isStart() == false) {
					ball.setP(new Point(pad.getPadPoint().x + pad.PAD_W / 2 - ball.BALL_SIZE / 2, ball.getP().y));
				}

				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (ball.isStart() == false) {
						ballThread();
					}
				}

				mainPanel.repaint();
			}

		});
	}

	private void ballThread() {
		new Thread(() -> {
			ball.setStart(true);

			while (true) {

				collision();
				collision(pad.getPadPoint(), pad.PAD_W, pad.PAD_H);

				if (ball.getP().y + ball.BALL_SIZE >= H) {
					JOptionPane.showMessageDialog(null, "GAME OVER");
					return;
				}

				if (ball.isxCheck()) {
					ball.moveR();
				} else {
					ball.moveL();
				}

				if (ball.isyCheck()) {
					ball.moveUp();
				} else {
					ball.moveDown();
				}

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mainPanel.repaint();
			}

		}).start();
	}

	private void collision() {
		// 벽에 부딪혔을 경우 튕기는 메서드.
		if (ball.getP().x == 0 || ball.getP().x + ball.BALL_SIZE == W - 100) {
			// ball이 벽에 부딪힐 경우 튕기기
			if (ball.isxCheck()) {
				ball.setxCheck(false);
			} else {
				ball.setxCheck(true);
			}
		}

		if (ball.getP().y == 0) {
			// ball이 천장에 닿으면 방향 반대로 바꿔주기
			ball.setyCheck(false);
		}
	}

	private void collision(Point p, int width, int height) {
		// ball이 block이나 pad에 부딪힐 때 호출되는 메서드
		// 기본적으로는 벽에 부딪힐 때와 같은 원리지만, 부딪힐 때 x축 진행 방향이 랜덤하게 바뀐다는 것에 차이가 있다.

		// 충돌하는 사각형의 꼭지점 정보.
		int x1 = p.x;
		int x2 = p.x + width;
		int y1 = p.y;
		int y2 = p.y + height;

		// ball의 네 꼭지점.
		int ball1 = ball.getP().x;
		int ball2 = ball.getP().x + ball.BALL_SIZE;
		int ball3 = ball.getP().y;
		int ball4 = ball.getP().y + ball.BALL_SIZE;

		// 사각형 충돌 감지
		if (x1 <= ball2 && ball1 <= x2 && y1 <= ball4 && ball3 <= y2) {

			// x축 방향 랜덤하게 변경.
			synchronized (block.pointList) {
				if (block.pointList.indexOf(p) == -1) {
					ball.setyCheck(true);
				} else {
					int direction = ((int) (Math.random() * 2));
					if (direction == 0) {
						ball.setxCheck(true);
					} else {
						ball.setxCheck(false);
					}

					if (ball.isyCheck()) {
						ball.setyCheck(false);
					} else {
						ball.setyCheck(true);
					}

					// pointList에 충돌한 객체의 point가 있을 경우 point삭제
					block.pointList.remove(p);
					score++;

					// return;
				}
			}
		}

	}

	public static void main(String[] args) {
		new MainFrame();
	}

}
