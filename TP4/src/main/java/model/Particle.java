package model;

public class Particle {

	private int id;
	private Pair position;
	private Pair velocity;
	private double mass;
	
	public Particle(final int id, final double x, final double y, final double vx, final double vy, final double mass) {
		this.id = id;
		this.position = new Pair(x, y);
		this.velocity = new Pair(vx, vy);
		this.mass = mass;
	}
	
	public boolean isOverlapped(final Particle particle) {
		return particle.getX() == getX() && particle.getY() == getY();
	}

	public double getVelocityModule() {
		return Math.hypot(getVx(), getVy());
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Pair getPosition() {
		return position;
	}

	public Pair getVelocity() {
		return velocity;
	}

    public void setPosition(Pair position) {
        this.position = position;
    }

    public void setVelocity(Pair velocity) {
        this.velocity = velocity;
    }

    public double getX() {
		return position.getX();
	}
	
	public double getY() {
		return position.getY();
	}

	public double getVx() {
		return velocity.getX();
	}

	public double getVy() {
		return velocity.getY();
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	@Override
	public String toString() {
		return "Particle [position=" + position + "]";
	}
}
