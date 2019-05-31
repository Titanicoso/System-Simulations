package model;

import java.util.List;

public class Particle {

	private int id;
	private Pair position;
	private Pair velocity;
	private double radius;
	private double interactionRadius;

	private final static double TAO = 0.5;
	private final static double BETA = 0.9;
	
	public Particle(final int id, final double x, final double y, final double vx, final double vy,
					final double radius, final double interactionRadius) {
		this.id = id;
		this.position = new Pair(x, y);
		this.velocity = new Pair(vx, vy);
		this.radius = radius;
		this.interactionRadius = interactionRadius;
	}

	public Particle(final Particle particle) {
		this.id = particle.getId();
		this.position = new Pair(particle.getX(), particle.getY());
		this.velocity = new Pair(particle.getVx(), particle.getVy());
		this.radius = particle.getRadius();
		this.interactionRadius = particle.getInteractionRadius();
	}
	
	public boolean isOverlapped(final Particle particle) {
		return interactionRadius + particle.getInteractionRadius() - distance(particle) >= 0;
	}

	public double getVelocityModule() {
		return Math.hypot(getVx(), getVy());
	}
	
	public double distance(final Particle particle) {
		return Math.hypot(
				(particle.getX() - this.getX()),
				(particle.getY() - this.getY())
		);
	}

	public void move(final double dt) {
		position.sum(getVx() * dt, getVy() * dt);
	}

	public void updateRadius(final double dt, final double maxRadius) {
		if (interactionRadius < maxRadius) {
			interactionRadius += maxRadius / (TAO / dt);
		}
	}

	public void updateVelocity(final Pair target, final double maxRadius, final double maxVelocity) {
		final double dx = target.getX() - getX();
		final double dy = target.getY() - getY();
		final double distance = target.distance(position);
		final double mod = maxVelocity * Math.pow((interactionRadius - radius) / (maxRadius - radius), BETA);
		velocity.setX(mod * dx / distance);
		velocity.setY(mod * dy / distance);
	}

	public void contractRadius() {
		interactionRadius = radius;
	}

	public void escapeVelocity(final List<Particle> particles, final double maxVelocity) {
		final double dx = particles.parallelStream()
				.mapToDouble(neighbour -> neighbour.getX() - getX())
				.sum();
		final double dy = particles.parallelStream()
				.mapToDouble(neighbour -> neighbour.getY() - getY())
				.sum();
		final double distance = particles.parallelStream()
				.mapToDouble(neighbour -> neighbour.distance(this))
				.sum();
		velocity.setX(maxVelocity * -dx / distance);
		velocity.setY(maxVelocity * -dy / distance);
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

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getInteractionRadius() {
		return interactionRadius;
	}

	public void setInteractionRadius(double interactionRadius) {
		this.interactionRadius = interactionRadius;
	}

	@Override
	public String toString() {
		return "Particle [position=" + position + "]";
	}
}
