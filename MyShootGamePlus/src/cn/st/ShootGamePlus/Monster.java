package cn.st.ShootGamePlus;

import java.util.Random;

public class Monster extends FlyingObject implements Enemy, EnemyAward {
	private int life = 5;
	private int xSpeed = 1;
	private int ySpeed = 2;
	
	private int awardType = AWARD_FIRE_FREQ;
	
	public Monster() {
		this.image = ShootGame.monster;
		this.width = image.getWidth();
		this.height = image.getHeight();
		
		Random rand = new Random();
		this.x = rand.nextInt(ShootGame.WIDTH - this.width);
		this.y = -this.height;
		
		if (1 == rand.nextInt(2)) {
			xSpeed = -xSpeed;
		}
	}
	
	@Override
	public void step() {
		if (x >= (ShootGame.WIDTH-this.width)) {
			xSpeed = -xSpeed;	
		}
		if (x <= 0) {
			xSpeed = -xSpeed;
		}
		x += xSpeed;
		y += ySpeed;
	}
	
	@Override
	public boolean outOfBounds() {
		return (y >= ShootGame.HEIGHT);
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
	public int giveAward() {
		return this.awardType;
	}
}
