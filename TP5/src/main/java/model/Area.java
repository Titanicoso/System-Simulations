package model;

import java.util.ArrayList;
import java.util.List;

public class Area {

	private double length;
	private double height;
	private double holeLength;
	private List<Particle> particles;
	private List<Integer> outParticles;
	
	public Area(double length, double height, double holeLength, List<Particle> particles) {
		this.length = length;
		this.height = height;
		this.holeLength = holeLength;
		this.particles = particles;
		this.outParticles = new ArrayList<>();
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

	public List<Integer> getOutParticles() {
		return outParticles;
	}

	public void setOutParticles(List<Integer> outParticles) {
		this.outParticles = outParticles;
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

	public List<Particle> getWallPositions(Particle particle) {
		final List<Particle> wallPositions = new ArrayList<>();
		final double holeStart = length / 2 - holeLength / 2;
		final double holeEnd = length / 2 + holeLength / 2;
		final double x = particle.getX();
		final double y = particle.getY();
		final double radius = particle.getRadius();

		if(x >= holeStart && x <= holeEnd) {
			wallPositions.add(new Particle(-1, holeStart, 1.0/10, 0, 0, 0, 0));
			wallPositions.add(new Particle(-1, holeEnd, 1.0/10, 0, 0, 0, 0));
		} else {
			wallPositions.add(new Particle(-1, x, 1.0/10, 0, 0, 0, 0));
		}
		wallPositions.add(new Particle(-1, x, height, 0, 0, 0, 0));
		wallPositions.add(new Particle(-1,0, y, 0, 0, 0, 0));
		wallPositions.add(new Particle(-1, length, y, 0, 0, 0, 0));

		return wallPositions;
	}
}
