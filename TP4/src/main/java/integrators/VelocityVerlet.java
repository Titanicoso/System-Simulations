package integrators;

import interfaces.Force;
import model.Pair;
import model.Particle;

import java.util.List;

public class VelocityVerlet {

    public Particle evolve(final Particle particle, final double dt,
                           final List<Particle> particles, final Force force) {

        final Pair initialForce = force.getForce(particle);
        final Pair initialVelocity = particle.getVelocity();
        final double mass = particle.getMass();

        final Pair newPosition = new Pair(initialForce)
                .multiplyByScalar(dt * dt / mass)
                .sum(particle.getPosition())
                .sum(initialVelocity.getX() * dt, initialVelocity.getY() * dt);

        final Pair intermediateVelocity = initialForce
                .multiplyByScalar(dt / (2 * mass))
                .sum(particle.getVelocity());

        final Pair newForce = force.getForce(new Particle(particle.getId(), newPosition.getX(), newPosition.getY(),
                intermediateVelocity.getX(), intermediateVelocity.getY(), mass));

        final Pair newVelocity = intermediateVelocity.sum(newForce.multiplyByScalar(dt / (2 * mass)));

        particle.setPosition(newPosition);
        particle.setVelocity(newVelocity);

        return particle;
    }



}
