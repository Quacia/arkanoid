package com.inc.arkanoid.v7;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class MainFrame extends JFrame {
	// 프레임 정보 및 이벤트리스너
	public static final int W = 600;
	public static final int H = 700;

	JPanel mainPanel;

	Block block;
	Pad pad;
	Ball ball;
	
	Thread itemThread;
	Thread bulletThread;
	
	boolean paused;
	boolean setBullet;
	
	
	//볼 방향 설정.
	private boolean xCheck = true; //xCheck가 true이면 오른쪽으로.
	private boolean yCheck = true; //yCheck가 true이면 위로.
	
	//게임 초기화 여부.
	private boolean initial = true;
	
	int score = 0;
	int level = 1;
	int life = 3;
	
	int ballSpeed = 7;
	
	ArrayList<Item> itemList = new ArrayList<>();
	ArrayList<Bullet> bulletList = new ArrayList<>();

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
		if(initial) {
			block = new Block(level);
		}
		pad = new Pad();
		pad.PAD_W = 60; //Pad 폭 재설정
		ballSpeed = 7; //ballSpeed초기화
		itemList.clear();
		bulletList.clear();
		setBullet = false;
		ball = new Ball();
		
		ball.setStart(false);

		// pad 초기 위치.
		Point padPoint = new Point((W - 100) / 2 - pad.PAD_W / 2, H - 100);
		pad.setPadPoint(padPoint);

		//ball 초기 위치.
		Point ballPoint = new Point(padPoint.x + pad.PAD_W / 2 - ball.BALL_SIZE / 2, padPoint.y - ball.BALL_SIZE);
		ball.setP(ballPoint);
		
	}

	private void initComponent() {

		mainPanel = new JPanel() {

			@Override
			protected void paintComponent(Graphics g) {
				g.clearRect(0, 0, W, H);
				
				synchronized(block.blockList) {
					for (Block b : block.blockList) {
						g.setColor(b.getC());
						g.fillRect(b.getP().x, b.getP().y, Block.BLOCK_W, Block.BLOCK_H);
						blockThread(b);
					}
					if(itemList != null) {
						for(Item item : itemList) {
							g.setColor(item.getC());
							g.fillOval(item.getP().x, item.getP().y, 10, 10);
						}
					}
					if(bulletList !=null) {
						for(Bullet bullet : bulletList) {
							g.setColor(Color.DARK_GRAY);
							g.fillOval(bullet.getP().x, bullet.getP().y, 10, 10);
						}
					}
					
				}
				
				g.setColor(Color.GRAY);
				g.fillOval(ball.getP().x, ball.getP().y, Ball.BALL_SIZE, Ball.BALL_SIZE);
				g.setColor(Color.DARK_GRAY);
				g.fillRect(pad.getPadPoint().x, pad.getPadPoint().y, pad.PAD_W, pad.PAD_H);

				g.setColor(Color.BLACK);
				g.drawRect(500, 0, 200, H); // 레벨, 점수판 위치
				g.drawString("레벨: " + level, 510, 50);
				g.drawString("점수: " + score, 510, 100);
				g.drawString("LIFE: "+ life, 510, 75);	
			}
		};
		

		add(mainPanel);
		
	}
	
	private void initEvent() {
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					if (pad.getPadPoint().x > 0) {
						pad.moveL();
					}
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if (pad.getPadPoint().x + pad.PAD_W < W - 100) {
						pad.moveR();
					}
				}

				if(e.getKeyCode() ==KeyEvent.VK_UP) {
					if(setBullet) {
						bulletThread(new Point(pad.getPadPoint().x + pad.PAD_W/2, pad.getPadPoint().y));
						bulletThread.start();
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (ball.isStart() == false) {
						ballThread();
					}else {
						paused = true;
					}
				}
				if (ball.isStart() == false) {
					ball.setP(new Point(pad.getPadPoint().x + pad.PAD_W / 2 - ball.BALL_SIZE / 2, ball.getP().y));
					mainPanel.repaint();
				}
				
				
			}

		});
	}
	

	private void ballThread() {
		new Thread(() -> {
			ball.setStart(true);

			while (true) {
				if(paused) {
					JOptionPane.showMessageDialog(null, "Paused!");
					paused = false;
				}
				collision();
				
				//볼이 화면을 벗어나면 life 감소, life 0이 되면 Game Over
				if (ball.getP().y + ball.BALL_SIZE >= H) {
					if(life == 1) {
						score = 0;
						level = 1;
						life = 3;
						initial = true;
						block = null;
						block.blockList.clear();
						initInitialize();
						JOptionPane.showMessageDialog(null, "GAME OVER");
						return;
					}else {
						initial = false;
						initInitialize();
						life--;
						return;
					}
				}
				
				
				if(block.blockList.isEmpty() == true) {
					if(level == 3) {
						JOptionPane.showMessageDialog(null, "CLEAR!");
						return;
					}else {
						level++;
						initial = true;
						initInitialize();
						return;
					}
				}
				
				if (xCheck) {
					ball.moveR();
				} else {
					ball.moveL();
				}

				if (yCheck) {
					ball.moveUp();
				} else {
					ball.moveDown();
				}

				try {
					Thread.sleep(ballSpeed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mainPanel.repaint();
				
			}

		}).start();
	}

	private void blockThread(Block b) {
		new Thread(()-> {
			int x1 = b.getP().x;
			int x2 = b.getP().x + Block.BLOCK_W;
			int y1 = b.getP().y;
			int y2 = b.getP().y + Block.BLOCK_H;
			
			if(x1 <= ball.getP().x + ball.BALL_SIZE && ball.getP().x <= x2 && y1 <= ball.getP().y+ball.BALL_SIZE && ball.getP().y <= y2) {
				synchronized(Block.blockList) {
					int itemNum = ((int)(Math.random() * 10));//블록이 깨질때마다 0~9의 난수 생성
					//int itemNum = 0;
					if(itemNum == 0)  {
						itemThread(new Point(b.getP().x + Block.BLOCK_W/2, b.getP().y));
						itemThread.start();
					}
					Block.blockList.remove(b);
					score++;
				}
				int ranNum = ((int)(Math.random() * 2));
				if(ranNum == 0) {
					xCheck = false;
				}else {
					xCheck = true;
				}
				
				if(yCheck) {
					yCheck = false;
				}else {
					yCheck = true;
				}
				
				return;
			}
		}).start();
	}
	private void collision() {
		//패드나 벽에 부딪혔을 경우 튕기는 메서드.
		if (ball.getP().x == 0 || ball.getP().x + ball.BALL_SIZE == W - 100) {
			// ball이 벽에 부딪힐 경우 튕기기
			if (xCheck) {
				xCheck = false;
			} else {
				xCheck = true;
			}
		}

		if (ball.getP().y == 0) {
			// ball이 천장에 닿으면 방향 반대로 바꿔주기
			yCheck = false;
		}
		
		int x1 = pad.getPadPoint().x;
		int x2 = pad.getPadPoint().x + pad.PAD_W;
		int y1 = pad.getPadPoint().y;
		int y2 = pad.getPadPoint().y + pad.PAD_H;
		
		if(x1 < ball.getP().x + ball.BALL_SIZE && ball.getP().x < x2 && y1 < ball.getP().y+ball.BALL_SIZE && ball.getP().y < y2) {
			//ball이 패드와 부딪힐 경우 x방향은 랜덤하게 바뀜
			int ranNum = ((int)(Math.random() * 2));
			if(ranNum == 0) {
				xCheck = true;
			}else {
				xCheck = false;
			}
			
			//y방향은 무조건 true;
			yCheck = true;
		}
		
	}
	
	private void itemThread(Point p) {
		itemThread = new Thread(() -> {
			Item item = new Item(p);
			itemList.add(item);
			while(true) {
				
				if(item.getP().y > H) {
					itemList.remove(item);
					return; //화면 밑으로 내려가면 쓰레드 종료.
				}
				
				item.moveDown();
				
				int x1 = pad.getPadPoint().x;
				int x2 = pad.getPadPoint().x + pad.PAD_W;
				int y1 = pad.getPadPoint().y;
				int y2 = pad.getPadPoint().y + pad.PAD_H;
				//double distance = Point.distance(item.x, item.y, pad.getPadPoint().x, pad.getPadPoint().y);
				if(x1 < item.getP().x + 10 && item.getP().x < x2 && item.getP().y + 10 == y1 ) {
					switch(item.getItemType()) {
						case 0 : ballSpeed += 3; break;
						case 1 : pad.PAD_W += 20; break;
						case 2 : ballSpeed -= 3; break;
						case 3 : setBullet = true; break;
					}
					itemList.remove(item);
					return;
				}
				
				try {
					Thread.sleep(7);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				mainPanel.repaint();
			}
		});
	}
	
	private void bulletThread(Point p) {
		bulletThread = new Thread(()->{
			Bullet bullet =  new Bullet();
			bullet.setP(p);
			bulletList.add(bullet);
			while(true) {
				
				if(bullet.getP().y < 0) {
					bulletList.remove(bullet);
					return;
					}
				
				bullet.moveUp();
				synchronized(Block.blockList) {
					for(Block b : Block.blockList) {
						int x1 = b.getP().x;
						int x2 = b.getP().x + Block.BLOCK_W;
						int y1 = b.getP().y;
						int y2 = b.getP().y + Block.BLOCK_H;
						if(x1 < bullet.getP().x + 10 && bullet.getP().x < x2 && bullet.getP().y + 10 == y1 ) {
							synchronized(Block.blockList) {
								int itemNum = ((int)(Math.random() * 10));//블록이 깨질때마다 0~9의 난수 생성
								//int itemNum = 0;
								if(itemNum == 0)  {
									itemThread(new Point(b.getP().x + Block.BLOCK_W/2, b.getP().y));
									itemThread.start();
								}
								Block.blockList.remove(b);
								score++;
								
							}
							
							synchronized(bulletList) {
								bulletList.remove(bullet);
							}
							return;
						}
					}
				}
				if(ball.getP().y + ball.BALL_SIZE >= H) {
					return;
				}
				try {
					Thread.sleep(7);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				mainPanel.repaint();
			}
		});
	}

	public static void main(String[] args) {
		new MainFrame();
	}

}
