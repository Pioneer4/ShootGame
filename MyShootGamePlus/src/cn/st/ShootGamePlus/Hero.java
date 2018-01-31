package cn.st.ShootGamePlus;

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject{
	private int changeImageIndex = 0;
	private BufferedImage[] images = {ShootGame.hero0, ShootGame.hero1};
	private  int doubleFire = 0;
	private  int life = 3;
	
	public Hero() {
		this.image = images[0];
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.x = 150;
		this.y = 400;
	}
	
	@Override
	public void step() {
		
	}
	
	public  void step(int xMouseCoordinate, int yxMouseCoordinate) {
		this.x = xMouseCoordinate - this.width/2;
		this.y = yxMouseCoordinate - this.height/2;
	}
	
	@Override
	public  boolean outOfBounds() {
		return false;
	}
	
	public void changeImage() {
		this.image = images[(changeImageIndex++)%images.length];
	}
	
	public Bullet[] launchBullets() {
		if (doubleFire > 0) {
			Bullet[] bullets = new Bullet[3];
			bullets[0] = new Bullet(this.x+this.width/7, this.y+40);
			bullets[1] = new Bullet(this.x+this.width*4/5, this.y+40);
			bullets[2] = new Bullet(this.x+this.width/2, this.y-15);
			doubleFire -= 2;
			return bullets;
		} else {
			Bullet[] bullets = new Bullet[1];
			bullets[0] = new Bullet(this.x+this.width/2, this.y-15);
			return bullets;
		}
	}
	
	public void addDoubleFire() {
		doubleFire += 40;
	}
	
	public void clrDoubleFire() {
		doubleFire = 0;
	}
	
	public void addLife() {
		life++;
	}
	
	public void substractLife() {
		life--;
	}
	
	public int returnLife() {
		return life;
	}
	
	public boolean hitByEnemy(FlyingObject enemy) {
		int x1 = enemy.x - this.width;
		int x2 = enemy.x + enemy.width;
		int y1 = enemy.y - this.height;
		int y2 = enemy.y + enemy.height;
		
		return (x>=x1 && x<=x2 && y>=y1 && y<=y2);
	}
	
}
