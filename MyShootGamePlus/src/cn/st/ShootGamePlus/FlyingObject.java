package cn.st.ShootGamePlus;

import java.awt.image.BufferedImage;

public abstract class FlyingObject {
	protected BufferedImage image;
	protected int width;
	protected int height;
	protected int x;
	protected int y;
	
	public abstract void step();
	public abstract boolean outOfBounds();
}
