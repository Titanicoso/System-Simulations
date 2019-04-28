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

        final Pair previousForce = force.recalculateForce(
                new Particle(particle.getId(), previous[0].getX(),
                previous[0].getY(), previous[1].getX(), previous[1].getY(), mass), particles
        );

        final Pair initialVelocity = particle.getVelocity();

        final Pair newPosition = new Pair(
                initialForce.getX() * 2 * dt * dt / (3 * mass) + particle.getX() +
                        initialVelocity.getX() * dt - previousForce.getX() * dt * dt / (6 * mass),
                initialForce.getY() * 2 * dt * dt / (3 * mass) + particle.getY() +
                        initialVelocity.getY() * dt - previousForce.getY() * dt * dt / (6 * mass)
        );

        final Pair newForce;

        if (force.isVelocityDependant()) {
        	 final Pair intermediateVelocity = new Pair(
        	         initialForce.getX() * 3 * dt / (2 * mass) + initialVelocity.getX() -
                             previousForce.getX() * dt / (2 * mass),
                     initialForce.getY() * 3 * dt / (2 * mass) + initialVelocity.getY() -
                             previousForce.getY() * dt / (2 * mass)
             );

        	 newForce = force.recalculateForce(
        	         new Particle(particle.getId(), newPosition.getX(), newPosition.getY(),
                             intermediateVelocity.getX(), intermediateVelocity.getY(), mass), particles
             );
        } else {
          	 newForce = force.recalculateForce(
          	         new Particle(particle.getId(), newPosition.getX(), newPosition.getY(),
                             initialVelocity.getX(), initialVelocity.getY(), mass), particles
             );
        }

        final Pair newVelocity = new Pair(
                initialForce.getX() * 5 * dt / (6 * mass) + initialVelocity.getX() +
                        newForce.getX() * dt / (3 * mass) - previousForce.getX() * dt / (6 * mass),
                initialForce.getY() * 5 * dt / (6 * mass) + initialVelocity.getY() +
                        newForce.getY() * dt / (3 * mass) - previousForce.getY() * dt / (6 * mass)
        );

        particle.setPosition(newPosition);
        particle.setVelocity(newVelocity);

        return particle;
    }

    private Pair[] calculateEuler(final Particle particle, final double dt,
                                  final Force force) {

        final Pair initialForce = force.getForce(particle);
        final double mass = particle.getMass();

        final Pair newVelocity = new Pair(
                initialForce.getX() * dt / mass + particle.getVx(),
                initialForce.getY() * dt / mass + particle.getVy()
        );

        final Pair newPosition = new Pair(
                initialForce.getX() * dt * dt / (2 * mass) + newVelocity.getX() * dt + particle.getX(),
                initialForce.getY() * dt * dt / (2 * mass) + newVelocity.getY() * dt + particle.getY()
        );

        return new Pair[] {newPosition, newVelocity};
    }
}
