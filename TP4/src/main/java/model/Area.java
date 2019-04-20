package model;

import java.util.List;

public class Area {

	private double length;
	private double height;
	private List<Particle> particles;
	
	public Area(double length, List<Particle> particles) {
		this.length = length;
		this.particles = particles;
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

}
