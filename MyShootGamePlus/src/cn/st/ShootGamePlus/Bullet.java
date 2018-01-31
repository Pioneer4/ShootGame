package cn.st.ShootGamePlus;

public class Bullet extends FlyingObject{
	private  int speed = 3;
	
	public Bullet(int x, int y) {
		this.image = ShootGame.bullet;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.x = x;
		this.y = y;
	}
	
	public void step() {
		y -= speed;
	}
	
	public boolean outOfBounds() {
		return (this.y <= -this.height);
	}
}
