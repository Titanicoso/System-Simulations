package integrators;

import interfaces.Force;
import model.Pair;
import model.Particle;

import java.util.List;

public class GearPredictorCorrector {

    private static double[][] ALPHA = {
            {3.0/20, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60},
            {3.0/16, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60}
    };

    public Particle evolve(final Particle particle, final double dt,
                           final List<Particle> particles, final Force force) {
        final int alphaIndex = force.isVelocityDependant() ? 1 : 0;

        final double mass = particle.getMass();
        final Pair r = new Pair(particle.getPosition());
        final Pair r1 = new Pair(particle.getVelocity());
        final Pair r2 = force.getForce(particle, particles).multiplyByScalar(1/mass);
        final Pair r3 = force.getD1(particle, particles).multiplyByScalar(1/mass);
        final Pair r4 = force.getD2(particle, particles).multiplyByScalar(1/mass);
        final Pair r5 = force.getD3(particle, particles).multiplyByScalar(1/mass);

        final Pair predictedR = new Pair(r)
                .sum(r1.getX() * dt, r1.getY() * dt)
                .sum(r2.getX() * (Math.pow(dt, 2) / 2), r2.getY() * (Math.pow(dt, 2) / 2))
                .sum(r3.getX() * (Math.pow(dt, 3) / 6), r3.getY() * (Math.pow(dt, 3) / 6))
                .sum(r4.getX() * (Math.pow(dt, 4) / 24), r4.getY() * (Math.pow(dt, 4) / 24))
                .sum(r5.getX() * (Math.pow(dt, 5) / 125), r5.getY() * (Math.pow(dt, 5) / 125));

        final Pair predictedR1 = new Pair(r1)
                .sum(r2.getX() * dt, r1.getY() * dt)
                .sum(r3.getX() * (Math.pow(dt, 2) / 2), r2.getY() * (Math.pow(dt, 2) / 2))
                .sum(r4.getX() * (Math.pow(dt, 3) / 6), r3.getY() * (Math.pow(dt, 3) / 6))
                .sum(r5.getX() * (Math.pow(dt, 4) / 24), r4.getY() * (Math.pow(dt, 4) / 24));

        final Pair predictedR2 = new Pair(r2)
                .sum(r3.getX() * dt, r1.getY() * dt)
                .sum(r4.getX() * (Math.pow(dt, 2) / 2), r2.getY() * (Math.pow(dt, 2) / 2))
                .sum(r5.getX() * (Math.pow(dt, 3) / 6), r3.getY() * (Math.pow(dt, 3) / 6));

        final Pair acceleration = force.getForce(new Particle(particle.getId(), predictedR.getX(), predictedR.getY(),
                predictedR1.getX(), predictedR1.getY(), mass), particles);

        final Pair deltaR2 = acceleration.multiplyByScalar(1/mass).substract(predictedR2).multiplyByScalar(dt * dt * 1/2);

        final Pair correctedR = predictedR.sum(ALPHA[0][alphaIndex] * deltaR2.getX(), ALPHA[0][alphaIndex] * deltaR2.getY());
        final Pair correctedR1 = predictedR1.sum(ALPHA[1][alphaIndex] * deltaR2.getX() / dt, ALPHA[1][alphaIndex] * deltaR2.getY() / dt);

        particle.setPosition(correctedR);
        particle.setVelocity(correctedR1);

        return particle;
    }
}
