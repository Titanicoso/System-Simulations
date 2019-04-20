package model;

public class Particle {

	private int id;
	private double x;
	private double y;
	private double vx;
	private double vy;
	
	public Particle(final int id, final double x, final double y, final double vx, final double vy) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
	}
	
	public boolean isOverlapped(final Particle particle) {
		return particle.getX() == x && particle.getY() == y;
	}

	public double getVelocityModule() {
		return Math.hypot(vx, vy);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}

	public double getVx() {
		return vx;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public double getVy() {
		return vy;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}
		
}
