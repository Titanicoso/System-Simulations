
public class Particle {

	private int id;
	private double x;
	private double y;
	private double ratio;
	
	public Particle(int id, double x, double y, double ratio) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.ratio = ratio;
	}

	public double distance(final Particle particle, final Area area) {
		return area.isPeriodic() ? periodicDistance(particle, area) : distance(particle);
	}

	private double distance(final Particle particle) {
		return Math.hypot(
				(particle.getX() - this.getX()),
				(particle.getY() - this.getY())
		) - (this.getRatio() + particle.getRatio());
	}

	private double periodicDistance(final Particle particle, final Area area) {

		final double dy = Math.abs(particle.getY() - this.getY());
		final double dx = Math.abs(particle.getX() - this.getX());

		final double cdx = Math.abs(dx - area.getLength());
		final double cdy = Math.abs(dy - area.getLength());


		return Math.hypot(
				Math.min(dx, cdx),
				Math.min(dy, cdy)
		) - (this.getRatio() + particle.getRatio());
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
	
	public double getRatio() {
		return ratio;
	}
	
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
		
}
