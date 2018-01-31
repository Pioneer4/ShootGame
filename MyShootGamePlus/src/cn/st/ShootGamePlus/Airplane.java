package cn.st.ShootGamePlus;

import java.util.Random;


public class Airplane extends FlyingObject implements Enemy, EnemyScore{
	private int life = 2;
	private static final int AIRPLANE_SCORE = 1;
	private int ySpeed = 3;
	
	public Airplane() {
		this.image = ShootGame.airplane;
		this.width = image.getWidth();
		this.height = image.getHeight();
		
		Random rand = new Random();
		this.x = rand.nextInt(ShootGame.WIDTH - this.width);
		this.y = -this.height;
	}
	
	@Override
	public void step() {
		this.y += ySpeed;
	}
	
	@Override
	public boolean outOfBounds() {
		return (this.y >= ShootGame.HEIGHT);
	}
	
	@Override
	public boolean shootByBullet(Bullet b) {
		int x1 = this.x;
		int x2 = this.x + this.width;
		int y1 = this.y;
		int y2 = this.y + this.height;
		int x = b.x;
		int y = b.y;
		if (x>=x1 && x<=x2 && y >=y1 && y<=y2) {
			this.life--;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean whetherDied() {
		return (life <= 0);
	}
	
	@Override
	public int giveScore() {
		return AIRPLANE_SCORE;
	}
}
