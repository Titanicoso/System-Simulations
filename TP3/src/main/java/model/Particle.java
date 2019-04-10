package model;

public class Particle {

	private int id;
	private double x;
	private double y;
	private double radius;
	private double vx;
	private double vy;
	private double mass;
	private boolean isBig;
	
	public Particle(int id, double x, double y, double radius, double vx, double vy, double mass, boolean isBig) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.vx = vx;
		this.vy = vy;
		this.mass = mass;
		this.isBig = isBig;
	}
	
	public boolean isOverlapped(Particle particle) {
		return Math.pow(x - particle.getX(), 2) + Math.pow(y - particle.getY(), 2)
					<= Math.pow(radius + particle.getRadius(), 2);
	}
	
	public Double calculateCollisionTime(Particle particle) {
		double[] dr = {particle.getX() - x, particle.getY() - y};
		double[] dv = {particle.getVx() - vx, particle.getVy() - vy};
		double sigma = radius + particle.getRadius();
		double vXr = dr[0] * dv[0] + dr[1] * dv[1];
		double vXv = Math.pow(dv[0], 2) + Math.pow(dv[1], 2);
		double d = Math.pow(vXr, 2) - vXv * (Math.pow(dr[0], 2) + Math.pow(dr[1], 2) 
						- Math.pow(sigma, 2));
		
		if (vXr >= 0)
			return null;
		
		if (d < 0)
			return null;

		double t1 = -(vXr + Math.sqrt(d))/(vXv);
		double t2 = -(vXr - Math.sqrt(d))/(vXv);

		if(t1 < 0)
			return t2;
		
		return Math.min(t1, t2);
	}
	
	public Double calculateCollisionTime(CollisionType type, double length) {
		double radical;
		if (type == CollisionType.PARTICLE_VS_VWALL) {
			radical = vx > 0 ? length - radius : radius;
			return Double.compare(vx, 0D) == 0 ? null : (radical - x)/vx; 
		}
		
		radical = vy > 0 ? length - radius : radius;
		return Double.compare(vy, 0D) == 0 ? null : (radical - y)/vy; 
	}
	
	public void evolvePosition(double time) {
		x = x + vx * time;
		y = y + vy * time;
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
	
	public double getRadius() {
		return radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
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

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public boolean isBig() {
		return isBig;
	}

	public void setBig(boolean isBig) {
		this.isBig = isBig;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Particle other = (Particle) obj;
		if (id != other.id)
			return false;
		return true;
	}
		
}
