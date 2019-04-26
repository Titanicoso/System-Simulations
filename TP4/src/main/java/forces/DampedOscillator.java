package forces;

import java.util.List;

import interfaces.Force;
import model.Pair;
import model.Particle;

public class DampedOscillator implements Force {

    private static final double K = 10000;
    private static final double GAMMA = 70.0;
    private static final double A = 1.0;
    private static final boolean velocityDependant = true;
    
	@Override
	public void calculate(List<Particle> particles) { }

    @Override
    public Pair getForce(final Particle particle) {
        final double x = -K * particle.getX() - GAMMA * particle.getVx();
        return new Pair(x, 0);
    }

    @Override
    public Pair getD1(final Particle particle) {
        final double x = K * GAMMA * particle.getX() + (GAMMA * GAMMA - K) * particle.getVx();
        return new Pair(x, 0);
    }

    @Override
    public Pair getD2(final Particle particle) {
        final double x = (K * K - K * GAMMA * GAMMA) * particle.getX() + (2 * K * GAMMA - Math.pow(GAMMA, 3)) * particle.getVx();
        return new Pair(x, 0);
    }

    @Override
    public Pair getD3(final Particle particle) {
        final double x = (K * Math.pow(GAMMA, 3) - 2 * K * K * GAMMA) * particle.getX()
                + (K * K - 3 * K * GAMMA * GAMMA + Math.pow(GAMMA, 4)) * particle.getVx();
        return new Pair(x, 0);
    }

    @Override
    public Pair getAnalyticalSolution(final Particle particle, final double time) {
        final double x = A * Math.exp(-GAMMA * time / (2 * particle.getMass())) *
                Math.cos(Math.sqrt(K / particle.getMass() - GAMMA * GAMMA / (4 * particle.getMass() * particle.getMass())) * time);
        return new Pair(x, 0);
    }

    @Override
    public boolean isVelocityDependant() {
        return velocityDependant;
    }

}
