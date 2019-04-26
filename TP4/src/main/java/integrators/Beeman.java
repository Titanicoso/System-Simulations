package integrators;

import interfaces.Force;
import model.Pair;
import model.Particle;

import java.util.List;

public class Beeman {

    public Particle evolve(final Particle particle, final double dt,
                           final List<Particle> particles, final Force force, Pair[] previous) {

        if(previous == null) {
            previous = calculateEuler(particle, -dt, force);
        }

        final Pair initialForce = force.getForce(particle);
        final double mass = particle.getMass();
        final Pair previousForce = force.getForce(new Particle(particle.getId(), previous[0].getX(),
                previous[0].getY(), previous[1].getX(), previous[1].getY(), mass));
        final Pair initialVelocity = particle.getVelocity();

        final Pair newPosition = new Pair(initialForce)
                .multiplyByScalar(2 * dt * dt / (3 * mass))
                .sum(particle.getPosition())
                .sum(initialVelocity.getX() * dt, initialVelocity.getY() * dt)
                .substract(new Pair(previousForce).multiplyByScalar(dt * dt / (6 * mass)));

        final Pair newForce;
        if (force.isVelocityDependant()) {
        	 final Pair intermediateVelocity = new Pair(initialForce)
                     .multiplyByScalar(3 * dt / (2 * mass))
                     .sum(initialVelocity)
                     .substract(new Pair(previousForce).multiplyByScalar(dt / (2 * mass)));
        	 newForce = force.getForce(new Particle(particle.getId(), newPosition.getX(),
        			 newPosition.getY(), intermediateVelocity.getX(), intermediateVelocity.getY(), mass));
        } else {
          	 newForce = force.getForce(new Particle(particle.getId(), newPosition.getX(),
        			 newPosition.getY(), initialVelocity.getX(), initialVelocity.getY(), mass));
        }

        final Pair newVelocity = initialForce
                .multiplyByScalar(5 * dt / (6 * mass))
                .sum(initialVelocity)
                .sum(newForce.multiplyByScalar(dt / (3 * mass)))
                .substract(previousForce.multiplyByScalar(dt / (6 * mass)));

        particle.setPosition(newPosition);
        particle.setVelocity(newVelocity);

        return particle;
    }

    private Pair[] calculateEuler(final Particle particle, final double dt,
                                  final Force force) {

        final Pair initialForce = force.getForce(particle);
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
