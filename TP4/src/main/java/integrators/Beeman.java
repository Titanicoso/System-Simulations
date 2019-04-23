package integrators;

import interfaces.Force;
import model.Pair;
import model.Particle;

import java.util.List;

public class Beeman {

    public Particle evolve(final Particle particle, final double dt,
                           final List<Particle> particles, final Force force, Pair[] previous) {

        if(previous == null) {
            previous = calculateEuler(particle, -dt, particles, force);
        }

        final Pair initialForce = force.getForce(particle, particles);
        final double mass = particle.getMass();
        final Pair previousForce = force.getForce(new Particle(particle.getId(), previous[0].getX(),
                previous[0].getY(), previous[1].getX(), previous[1].getY(), mass), particles);
        final Pair initialVelocity = particle.getVelocity();

        final Pair newPosition = new Pair(initialForce)
                .multiplyByScalar(2 * dt * dt / (3 * mass))
                .sum(particle.getPosition())
                .sum(initialVelocity.getX() * dt, initialVelocity.getY() * dt)
                .substract(new Pair(previousForce).multiplyByScalar(dt * dt / 6));

        final Pair newForce = initialForce;

//        final Pair newVelocity = new Pair(initialForce)
//                .multiplyByScalar(2 * dt * dt / (3 * mass))
//                .sum(particle.getPosition())
//                .sum(initialVelocity.getX() * dt, initialVelocity.getY() * dt)
//                .substract(new Pair(previousForce).multiplyByScalar(dt * dt / 6));

        particle.setPosition(newPosition);
//        particle.setVelocity(newVelocity);

        return particle;
    }

    private Pair[] calculateEuler(final Particle particle, final double dt,
                                 final List<Particle> particles, final Force force) {

        final Pair initialForce = force.getForce(particle, particles);
        final double mass = particle.getMass();

        final Pair newVelocity = new Pair(initialForce)
                .multiplyByScalar(dt / mass)
                .sum(particle.getVelocity());

        final Pair newPosition = initialForce
                .multiplyByScalar(dt * dt / (2 * mass))
                .sum(newVelocity.getX() * dt, newVelocity.getY() * dt)
                .sum(particle.getPosition());

        return new Pair[] {newPosition, newVelocity};
    }
}
