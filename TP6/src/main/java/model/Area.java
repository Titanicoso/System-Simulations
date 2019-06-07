package model;

import java.util.ArrayList;
import java.util.List;

public class Area {

	private double length;
	private double height;
	private double holeLength;
	private double extraSpace;
	private List<Particle> particles;
	
	public Area(double length, double height, double holeLength, double extraSpace, List<Particle> particles) {
		this.length = length;
		this.height = height;
		this.holeLength = holeLength;
		this.particles = particles;
		this.extraSpace = extraSpace;
	}
	
	public double getLength() {
		return length;
	}
	
	public void setLength(double length) {
		this.length = length;
	}

	public List<Particle> getParticles() {
		return particles;
	}

	public void setParticles(List<Particle> particles) {
		this.particles = particles;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getHoleLength() {
		return holeLength;
	}

	public void setHoleLength(double holeLength) {
		this.holeLength = holeLength;
	}

	public double getExtraSpace() {
		return extraSpace;
	}

	public void setExtraSpace(double extraSpace) {
		this.extraSpace = extraSpace;
	}

	public List<Particle> getWallPositions(Particle particle) {
		final List<Particle> wallPositions = new ArrayList<>();
		final double holeStart = length / 2 - holeLength / 2;
		final double holeEnd = length / 2 + holeLength / 2;
		final double x = particle.getX();
		final double y = particle.getY();
		final double radius = particle.getRadius();

		if(x >= holeStart && x <= holeEnd && holeLength != 0) {
			wallPositions.add(new Particle(-1, holeStart, extraSpace, 0, 0, 0, 0));
			wallPositions.add(new Particle(-2, holeEnd, extraSpace, 0, 0, 0, 0));
		} else {
			wallPositions.add(new Particle(-3, x, extraSpace, 0, 0, 0, 0));
		}
		wallPositions.add(new Particle(-4, x, height, 0, 0, 0, 0));
		wallPositions.add(new Particle(-5,0, y, 0, 0, 0, 0));
		wallPositions.add(new Particle(-6, length, y, 0, 0, 0, 0));

		return wallPositions;
	}

	public boolean isInHole(final Particle particle) {
		final double holeStart = length / 2 - holeLength / 2;
		final double holeEnd = length / 2 + holeLength / 2;
		final double x = particle.getX();
		return x >= holeStart + particle.getInteractionRadius()
				&& x <= holeEnd - particle.getInteractionRadius();
	}

	public Pair computeTarget(final Particle particle, double maxRadius) {
		final double holeStart = length / 2 - holeLength / 2 + particle.getRadius();
		final double holeEnd = length / 2 + holeLength / 2 - particle.getRadius();
		final double effective = holeEnd - holeStart;
		final double x = particle.getX();

		double lambda = (x - holeStart) / effective;

		if(particle.getY() < extraSpace)
			return new Pair(x, 0);

		if(lambda <= 0) {
			return new Pair(holeStart, extraSpace);
		} else if(lambda >= 1) {
			return new Pair(holeEnd, extraSpace);
		}

		return new Pair(x, 0);
	}
}
