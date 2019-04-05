package model;

public class Collision{
	
	private Particle particle1;
	private Particle particle2;
	private CollisionType type;
	
	public Collision(Particle particle1, Particle particle2, CollisionType type) {
		this.particle1 = particle1;
		this.particle2 = particle2;
		this.type = type;
	}	
	
	public void collide() {
		switch (type) {
			case PARTICLE_VS_HWALL:
				particle1.setVy(-particle1.getVy());
				break;
			case PARTICLE_VS_VWALL:
				particle1.setVx(-particle1.getVx());
				break;
			case PARTICLE_VS_PARTICLE:
				double[] dr = {particle1.getX() - particle2.getX(), particle1.getY() - particle2.getY()};
				double[] dv = {particle1.getVx() - particle2.getVx(), particle1.getVy() - particle2.getVy()};
				double sigma = particle1.getRadius() + particle2.getRadius();
				double vXr = dr[0] * dv[0] + dr[1] * dv[1];
				double j = (2 * particle1.getMass() * particle2.getMass() * vXr) / (sigma * (particle1.getMass() + particle2.getMass()));
				particle1.setVx(particle1.getVx() + j * dr[0] / (sigma * particle1.getMass()));
				particle1.setVy(particle1.getVy() + j * dr[1] / (sigma * particle1.getMass()));
				particle2.setVx(particle2.getVx() - j * dr[0] / (sigma * particle2.getMass()));
				particle1.setVy(particle2.getVy() - j * dr[1] / (sigma * particle2.getMass()));
				break;
		}
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
