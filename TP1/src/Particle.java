
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
