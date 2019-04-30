package integrators;

import interfaces.Force;
import interfaces.Integrator;
import model.Area;
import model.Pair;
import model.Particle;

import java.util.List;

public class GearPredictorCorrector implements Integrator {

    private static double[][] ALPHA = {
            {3.0/20, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60},
            {3.0/16, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60}
    };

    @Override
    public Particle evolve(final Particle particle, final double dt,
                           final List<Particle> particles, final Force force , Area area) {
        final int alphaIndex = force.isVelocityDependant() ? 1 : 0;

        final double mass = particle.getMass();
        final Pair r = particle.getPosition();
        final Pair r1 = particle.getVelocity();
        final Pair r2 = force.getForce(particle).multiplyByScalar(1/mass);
        final Pair r3 = force.getD1(particle).multiplyByScalar(1/mass);
        final Pair r4 = force.getD2(particle).multiplyByScalar(1/mass);
        final Pair r5 = force.getD3(particle).multiplyByScalar(1/mass);

        final Pair predictedR = new Pair(
                r.getX() + r1.getX() * dt + r2.getX() * (Math.pow(dt, 2) / 2) +
                        r3.getX() * (Math.pow(dt, 3) / 6) + r4.getX() * (Math.pow(dt, 4) / 24) +
                        r5.getX() * (Math.pow(dt, 5) / 120),
                r.getY() + r1.getY() * dt + r2.getY() * (Math.pow(dt, 2) / 2) +
                        r3.getY() * (Math.pow(dt, 3) / 6) + r4.getY() * (Math.pow(dt, 4) / 24) +
                        r5.getY() * (Math.pow(dt, 5) / 120)
        );

        final Pair predictedR1 = new Pair(
                r1.getX() + r2.getX() * dt + r3.getX() * (Math.pow(dt, 2) / 2) +
                        r4.getX() * (Math.pow(dt, 3) / 6) + r5.getX() * (Math.pow(dt, 4) / 24),
                r1.getY() + r2.getY() * dt + r3.getY() * (Math.pow(dt, 2) / 2) +
                        r4.getY() * (Math.pow(dt, 3) / 6) + r5.getY() * (Math.pow(dt, 4) / 24)
        );

        final Pair predictedR2 = new Pair(
                r2.getX() + r3.getX() * dt + r4.getX() * (Math.pow(dt, 2) / 2) +
                        r5.getX() * (Math.pow(dt, 3) / 6),
                r2.getY() + r3.getY() * dt + r4.getY() * (Math.pow(dt, 2) / 2) +
                        r5.getY() * (Math.pow(dt, 3) / 6)
        );

        final Pair acceleration = force.recalculateForce(
                new Particle(particle.getId(), predictedR.getX(), predictedR.getY(),
                predictedR1.getX(), predictedR1.getY(), mass), particles, area
        );

        final Pair deltaR2 = new Pair(
                (acceleration.getX() / mass - predictedR2.getX()) * dt * dt / 2,
                (acceleration.getY() / mass - predictedR2.getY()) * dt * dt / 2
        );

        final Pair correctedR = new Pair(
                predictedR.getX() + ALPHA[0][alphaIndex] * deltaR2.getX(),
                predictedR.getY() + ALPHA[0][alphaIndex] * deltaR2.getY()
        );

        final Pair correctedR1 = new Pair(
                predictedR1.getX() + ALPHA[1][alphaIndex] * deltaR2.getX() / dt,
                predictedR1.getY() + ALPHA[1][alphaIndex] * deltaR2.getY() / dt
        );

        particle.setPosition(correctedR);
        particle.setVelocity(correctedR1);

        return particle;
    }
}
