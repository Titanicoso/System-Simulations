package integrators;

import interfaces.Force;
import model.Area;
import model.Pair;
import model.Particle;

import java.util.List;

public class VelocityVerlet {

    public Particle evolve(final Particle particle, final double dt,
                           final List<Particle> particles, final Force force, Area area) {

        final Pair initialForce = force.getForce(particle);
        final Pair initialVelocity = particle.getVelocity();
        final double mass = particle.getMass();

        final Pair newPosition = new Pair(
                initialForce.getX() * dt * dt / mass + particle.getX() + initialVelocity.getX() * dt,
                initialForce.getY() * dt * dt / mass + particle.getY() + initialVelocity.getY() * dt
        );

        final Pair intermediateVelocity = new Pair(
                initialForce.getX() * dt / (2 * mass) + initialVelocity.getX(),
                initialForce.getY() * dt / (2 * mass) + initialVelocity.getY()
        );

        final Pair newForce = force.recalculateForce(
                new Particle(particle.getId(), newPosition.getX(), newPosition.getY(),
                intermediateVelocity.getX(), intermediateVelocity.getY(), mass), particles, area
        );

        final Pair newVelocity = new Pair(
                intermediateVelocity.getX() + newForce.getX() * dt / (2 * mass),
                intermediateVelocity.getY() + newForce.getY() * dt / (2 * mass)
        );

        particle.setPosition(newPosition);
        particle.setVelocity(newVelocity);

        return particle;
    }



}
