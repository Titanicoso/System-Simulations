
public class Area {

	private double length;
	private double interactionRatio;
	private Particle[] particles;
	
	public Area(double length,  double interactionRatio, Particle[] particles) {
		this.length = length;
		this.interactionRatio = interactionRatio;
		this.particles = particles;
	}
	
	public double getLength() {
		return length;
	}
	
	public void setLength(double length) {
		this.length = length;
	}
	
	public double getInteractionRatio() {
		return interactionRatio;
	}
	
	public void setInteractionRatio(double interactionRatio) {
		this.interactionRatio = interactionRatio;
	}

	public Particle[] getParticles() {
		return particles;
	}

	public void setParticles(Particle[] particles) {
		this.particles = particles;
	}
	
}
