
public class Area {

	private double length;
	private double interactionRatio;
	private Particle[] particles;
	private boolean periodic;
	
	public Area(double length,  double interactionRatio, Particle[] particles, boolean periodic) {
		this.length = length;
		this.interactionRatio = interactionRatio;
		this.particles = particles;
		this.periodic = periodic;
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

	public boolean isPeriodic() {
		return periodic;
	}

	public void setPeriodic(boolean periodic) {
		this.periodic = periodic;
	}
}
