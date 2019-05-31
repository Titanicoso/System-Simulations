package forces;

import interfaces.Force;
import model.Area;
import model.Pair;
import model.Particle;

import java.util.List;

public class GravitationalForce implements Force {

    private static final double EARTH_GRAVITATIONAL_CONSTANT = 9.8;


	@Override
	public void calculate(List<Particle> particles, Area area) { }

    @Override
    public Pair recalculateForce(Particle particle, List<Particle> particles, Area area) {
	    return getForce(particle);
    }

    @Override
    public Pair getForce(final Particle particle) {
        return new Pair(0, -particle.getMass() * EARTH_GRAVITATIONAL_CONSTANT);
    }

    @Override
    public Pair getD1(final Particle particle) {
        return new Pair(0,0);
    }

    @Override
    public Pair getD2(final Particle particle) {
       return new Pair(0,0);
    }

    @Override
    public Pair getD3(final Particle particle) {
        return new Pair(0,0);
    }

    @Override
    public Pair getAnalyticalSolution(final Particle particle, final double time) {
        return null;
    }

    @Override
    public boolean isVelocityDependant() {
        return false;
    }

}
