package cn.st.ShootGamePlus;

public interface EnemyAward {
	public static final int AWARD_DOUBLIELIFE = 0;
	public static final int AWARD_LIFE = 1;
	public static final int AWARD_FIRE_FREQ = 2;
	
	public int giveAward();
}
