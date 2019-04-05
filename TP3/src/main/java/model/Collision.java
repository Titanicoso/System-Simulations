package model;

public class Collision implements Comparable<Collision> {
	
	private Particle particle1;
	private Particle particle2;
	private CollisionType type;
	private double time;
	
	public Collision(Particle particle1, Particle particle2, CollisionType type, double time) {
		this.particle1 = particle1;
		this.particle2 = particle2;
		this.type = type;
		this.time = time;
	}

	public Particle getParticle1() {
		return particle1;
	}

	public void setParticle1(Particle particle1) {
		this.particle1 = particle1;
	}

	public Particle getParticle2() {
		return particle2;
	}

	public void setParticle2(Particle particle2) {
		this.particle2 = particle2;
	}

	public CollisionType getType() {
		return type;
	}

	public void setType(CollisionType type) {
		this.type = type;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	@Override
	public int compareTo(Collision o) {
		return Double.compare(this.time, o.getTime());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((particle1 == null) ? 0 : particle1.hashCode());
		result = prime * result + ((particle2 == null) ? 0 : particle2.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Collision other = (Collision) obj;
		if (type != other.type)
			return false;
		if (particle1 == null) {
			if (other.particle1 != null && other.particle2 == null)
				return false;
		}
		if (particle2 == null) {
			if (other.particle1 != null && other.particle2 == null)
				return false;
		}
		if(!particle1.equals(other.getParticle1()) &&
				!particle1.equals(other.getParticle2()))
			return false;
		if(!particle2.equals(other.getParticle1()) &&
				!particle2.equals(other.getParticle2()))
			return false;
		return true;
	}
	

}
