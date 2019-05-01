package integrators;

import interfaces.Force;
import interfaces.Integrator;
import model.Area;
import model.Pair;
import model.Particle;

import java.util.List;

public class VelocityVerlet implements Integrator {

    @Override
    public Particle evolve(final Particle particle, final double dt,
                           final List<Particle> particles, final Force force, Area area) {

        final Pair initialForce = force.getForce(particle);
        final Pair initialVelocity = particle.getVelocity();
        final double mass = particle.getMass();

        final Pair newPosition = new Pair(
                particle.getX() + dt * particle.getVx() + 0.5 * dt * dt * initialForce.getX() / mass,
                particle.getY() + dt * particle.getVy() + 0.5 * dt * dt * initialForce.getY() / mass
        );

        final Pair newForce = force.recalculateForce(
                new Particle(particle.getId(), newPosition.getX(), newPosition.getY(),
                        initialVelocity.getX(), initialVelocity.getY(), mass), particles, area
        );

        final Pair newVelocity = new Pair(
                particle.getVx() + 0.5 * dt * (newForce.getX() + initialForce.getX()) / mass,
                particle.getVy() + 0.5 * dt * (newForce.getY() + initialForce.getY()) / mass
        );

        return new Particle(particle.getId(), newPosition.getX(), newPosition.getY(), newVelocity.getX(), newVelocity.getY(), mass);
    }



}
