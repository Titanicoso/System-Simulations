package forces;

import interfaces.Force;
import model.Pair;
import model.Particle;

public class DampedOscillator implements Force {

    private static final double MASS = 70.0;
    private static final double K = 10000;
    private static final double GAMMA = 70.0;
    private static final double A = 1.0;

    @Override
    public Pair getForce(final Particle particle) {
        final double x = -K * particle.getX() - GAMMA * particle.getVy();
        return new Pair(x, 0);
    }

    @Override
    public Pair getD1(final Particle particle) {
        final double x = K * GAMMA * particle.getX() + (GAMMA * GAMMA - K) * particle.getVy();
        return new Pair(x, 0);
    }

    @Override
    public Pair getD2(final Particle particle) {
        final double x = (K * K - K * GAMMA * GAMMA) * particle.getX() + (2 * K * GAMMA - Math.pow(GAMMA, 3)) * particle.getVy();
        return new Pair(x, 0);
    }

    @Override
    public Pair getD3(final Particle particle) {
        final double x = (K * Math.pow(GAMMA, 3) - 2 * K * K * GAMMA) * particle.getX()
                + (K * K - 3 * K * GAMMA * GAMMA + Math.pow(GAMMA, 4)) * particle.getVy();
        return new Pair(x, 0);
    }

    @Override
    public Pair getAnalyticalSolution(final Particle particle, final double time) {
        final double x = A * Math.exp(-GAMMA * time / (2 * MASS)) *
                Math.cos(Math.sqrt(K / MASS - GAMMA * GAMMA / (4 * MASS * MASS)) * time);
        return new Pair(x, 0);
    }

}
