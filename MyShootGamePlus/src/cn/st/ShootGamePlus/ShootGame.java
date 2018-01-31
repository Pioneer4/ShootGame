package cn.st.ShootGamePlus;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Graphics;

import java.util.Timer;
import java.util.TimerTask;

import java.awt.Color;
import java.awt.Font;

import java.util.Arrays;
import java.util.Random;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ShootGame extends JPanel {
	public static final int WIDTH = 400;
	public static final int HEIGHT = 666;
	
	private Hero hero = new Hero();
	private FlyingObject[] flyings = new FlyingObject[0];
	private Bullet[] bullets = new Bullet[0];
	
	private static int gameScore = 0;
	
	private static final int START = 0;
	private static final int RUNNING = 1;
	private static final int PAUSE = 2;
	private static final int OVER = 3;
	private int state = START;
	
	public static BufferedImage background;
	public static BufferedImage gameStart;
	public static BufferedImage gamePause;
	public static BufferedImage gameOver;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage monster;
	
	static {
		try {
			background = ImageIO.read(ShootGame.class.getResource("background.png"));
			gameStart = ImageIO.read(ShootGame.class.getResource("gameStart.png"));
			gamePause = ImageIO.read(ShootGame.class.getResource("gamePause.png"));
			gameOver = ImageIO.read(ShootGame.class.getResource("gameOver.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
			monster = ImageIO.read(ShootGame.class.getResource("monster.png"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ShootGame gamePlus =  new ShootGame();
		JFrameAndJPanelInit(gamePlus);
		gamePlus.action();
	}
	
	private int timeIndex = 0;
	
	public void action() {	
		Timer timer = new Timer();
		int interval = 10;
		
		timer.schedule(new TimerTask() {
			public void run() {
				if (state == RUNNING) {
					timeIndex++;
					
					allObjectssMove();
					checkHitAndClr();
					checkOutOfBounds();
					heroHitByEnemy();
					
					if (timeIndex % generateEnemyFreq == 0) {
						generateEnemy();
						System.out.println(flyings.length + " " + bullets.length);
					}
					if (timeIndex % 10 == 0) {
						hero.changeImage();
					}
					if (timeIndex % generateBulletFreq == 0) {
						generateBullet();
					}
				}
				repaint();
			}
		}, interval, interval);
		
		MouseAdapter listener = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				if (state == RUNNING) {
					int x = e.getX();
					int y = e.getY();
					hero.step(x, y);	
				}
			}
			
			public void mouseClicked(MouseEvent e) {
				switch (state) {
				case START:
					state = RUNNING;
					break;
				case OVER:
					state = START;
					hero = new Hero();
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					state = START;
					gameScore = 0;
					break;
				default:
					break;
				}
			}
			
			public void mouseEntered(MouseEvent e) {
				if (state == PAUSE) {
					state = RUNNING;
				}
			}
			
			public void mouseExited(MouseEvent e) {
				if (state == RUNNING) {
					state = PAUSE;
				}
			}
		};
		
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
	}
	
	private int generateEnemyFreq = 40;
	
	public void generateEnemy() {
		flyings = Arrays.copyOf(flyings, flyings.length+1);
		Random rand = new Random();
		int randNum = rand.nextInt(20);
		if (randNum <= 2) {
			flyings[flyings.length-1] = new Bee();
		} else if (randNum == 3) {
			flyings[flyings.length-1] = new Monster();
		}  else {
			flyings[flyings.length-1] = new Airplane();
		}
	}
	
	private int generateBulletFreq = 30;
	
	public void generateBullet() {
		Bullet[] bs = hero.launchBullets();
		bullets = Arrays.copyOf(bullets, bullets.length+bs.length);
		System.arraycopy(bs, 0, bullets, bullets.length-bs.length, bs.length);
	}
	
	public void allObjectssMove() {
		for (int i=0; i<flyings.length; i++) {
			flyings[i].step();
		}
		
		for (int i=0; i<bullets.length; i++) {
			bullets[i].step();
		}
	}
	
	public void checkHitAndClr() {
		for (int i=0; i<flyings.length; i++) {
			if (flyings[i] instanceof Enemy) {
				Enemy enemy = (Enemy)flyings[i];
				for (int j=0; j<bullets.length; j++) {
					if (enemy.shootByBullet(bullets[j])) {
						bullets[j] = bullets[bullets.length-1];
						bullets = Arrays.copyOf(bullets, bullets.length-1);
						if (enemy.whetherDied()) {
							if (flyings[i] instanceof EnemyScore) {
								EnemyScore eScore = (EnemyScore)flyings[i];
								gameScore += eScore.giveScore();
							}
							if (flyings[i] instanceof EnemyAward) {
								EnemyAward eAward = (EnemyAward)flyings[i];
								int awardType = eAward.giveAward();
								switch (awardType) {
								case EnemyAward.AWARD_DOUBLIELIFE:
									hero.addDoubleFire();
									break;
								case EnemyAward.AWARD_LIFE:
									hero.addLife();
									break;
								case EnemyAward.AWARD_FIRE_FREQ:
									if (generateBulletFreq-2 > 0) {
										if (generateBulletFreq-10 > 0) {
											generateBulletFreq -= 10;
											hero.addDoubleFire();
										} else {
											generateBulletFreq -= 2;
											hero.addDoubleFire();
										}
									} else {
										hero.addDoubleFire();
										hero.addDoubleFire();
										hero.addDoubleFire();
										hero.addDoubleFire();
									}
									break;
								default:
									break;
								}
							}
							flyings[i] = flyings[flyings.length-1];
							flyings = Arrays.copyOf(flyings, flyings.length-1);
							break;
						}
					}
				}
			}
		}
	}
	
	public void heroHitByEnemy() {
		for (int i=0; i<flyings.length; i++) {
			if (hero.hitByEnemy(flyings[i])) {
				hero.substractLife();
				hero.clrDoubleFire();
				generateBulletFreq = 30;
				flyings[i] = flyings[flyings.length-1];
				flyings = Arrays.copyOf(flyings, flyings.length-1);
				checkHeroDied();
			}
		}
	}
	
	public void checkHeroDied() {
		if (hero.returnLife() <=0 ) {
			state = OVER;
		}
	}
	
	public void checkOutOfBounds() {
		int index = 0;
		
		FlyingObject[] flyAlives = new FlyingObject[flyings.length]; 
		for (int i=0; i<flyings.length; i++) {
			if (!flyings[i].outOfBounds()) {
				flyAlives[index] = flyings[i];
				index++;
			}
		}
		flyings = Arrays.copyOf(flyAlives, index);
		index = 0;
		
		Bullet[] bulletsEffect = new Bullet[bullets.length]; 
		for (int i=0; i<bullets.length; i++) {
			if (!bullets[i].outOfBounds()) {
				bulletsEffect[index] = bullets[i];
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletsEffect, index);
		index = 0;
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, null);
		paintHero(g);
		paintAirplane(g);
		paintBullets(g);
		paintScoreAndLife(g);
		paintState(g);
	}
	
	public void paintHero(Graphics g) {
		g.drawImage(hero.image, hero.x, hero.y, null);
	}
	
	public void paintAirplane(Graphics g) {
		for (int i=0; i<flyings.length; i++) {
			FlyingObject f = flyings[i];
			g.drawImage(f.image, f.x, f.y, null);
		}
	}
	
	public void paintBullets(Graphics g) {
		for (int i=0; i<bullets.length; i++) {
			Bullet b = bullets[i];
			g.drawImage(b.image, b.x, b.y, null);
		}
	}
	
	public void paintScoreAndLife(Graphics g) {
		g.setColor(new Color(0xff0000));
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		g.drawString("SCORE : "+ gameScore, 10, 25);
		g.drawString("Life : " + hero.returnLife(), 10, 45);
	}
	
	public void paintState(Graphics g) {
		switch (state) {
		case START:
			g.setColor(new Color(0xEFEFff));
			g.setFont(new Font(Font.SERIF, Font.BOLD, 60));
			g.drawString("Plane War ", 60, 300);
			
			g.setColor(new Color(0x0000ff));
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
			g.drawString("Copyright @ 2017--2018 Walter, All rights reserved", 40, 620);
			break;
		case PAUSE:
			g.drawImage(gamePause, 0, 0, null);
			break;
		case OVER:
			g.drawImage(gameOver, 0, 0, null);
			break;
		default:
			break;
		}
	}
	
	public static void JFrameAndJPanelInit(JPanel j) {
		JFrame frame = new JFrame("Fly");
		frame.add(j);
		frame.setSize(WIDTH, HEIGHT);
		frame.setAlwaysOnTop(true);
		frame.setIconImage(new ImageIcon("images/icon.jpg").getImage()); 
		frame.setLocationRelativeTo(null); 
		frame.setVisible(true); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	}
}
