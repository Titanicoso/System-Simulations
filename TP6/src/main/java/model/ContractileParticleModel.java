package model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContractileParticleModel {

    private double maxVelocity;
    private double maxRadius;
    private Area area;

    public ContractileParticleModel(double maxVelocity, double maxRadius, Area area) {
        this.maxVelocity = maxVelocity;
        this.maxRadius = maxRadius;
        this.area = area;
    }

    public Particle evolve(final Particle particle, final List<Particle> neighbours,
                           final double dt) {
        final Pair target;
        if(area.isInHole(particle)) {
            target = new Pair(particle.getX(), 0);
        } else {
            target = area.computeTarget(particle);
        }

        List<Particle> particleNeighbours = neighbours;
        List<Particle> walls = area.getWallPositions(particle).stream()
                .filter(particle::isOverlapped).collect(Collectors.toList());

        if(particleNeighbours != null) {
            particleNeighbours.addAll(walls);
        } else {
            particleNeighbours = walls;
        }

        Particle predicted = new Particle(particle);

        if (particleNeighbours != null && !particleNeighbours.isEmpty()) {
            predicted.contractRadius();
            predicted.escapeVelocity(particleNeighbours, maxVelocity);
            predicted.move(dt);
            predicted.updateVelocity(target, maxRadius, maxVelocity);
        } else {
            predicted.move(dt);
            predicted.updateVelocity(target, maxRadius, maxVelocity);
            predicted.updateRadius(dt, maxRadius);
        }

        return predicted;
    }


}
