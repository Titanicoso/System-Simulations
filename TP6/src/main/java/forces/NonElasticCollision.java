package forces;

import java.util.List;
import java.util.Map;

import interfaces.Force;
import model.Area;
import model.CellIndexMethod;
import model.Pair;
import model.Particle;

public class NonElasticCollision implements Force {

    private static final double KN = 1e5;
    private static final double GAMMA = 70.0;
    private Map<Integer, BundleOfJoy> bundlesOfJoy;
//    private CellIndexMethod cim;
    private static final boolean velocityDependant = true;
    
	@Override
	public void calculate(List<Particle> particles, Area area) {

        bundlesOfJoy.clear();
        for (int i = 0; i < particles.size(); i++) {
            if (!bundlesOfJoy.containsKey(i)) {
                BundleOfJoy boj = new BundleOfJoy();
                bundlesOfJoy.put(i, boj);
            }
            Particle p1 = particles.get(i);
            for (int j = i + 1; j < particles.size(); j++) {
                if (!bundlesOfJoy.containsKey(j)) {
                    BundleOfJoy boj = new BundleOfJoy();
                    bundlesOfJoy.put(j, boj);
                }
                Particle p2 = particles.get(j);
                if (p1.isOverlapped(p2)) {
                    double dx = (p1.getX() - p2.getX());
                    double distance = p1.distance(p2);
                    double dy = (p1.getY() - p2.getY());
                    double radiusSum = p1.getRadius() + p2.getRadius();

                    double f = -KN * (radiusSum - distance) - GAMMA * p1.relativeVelocityModule(p2);
                    Pair force = new Pair(f * dx/distance, f * dy/distance);

                    BundleOfJoy boj1 = bundlesOfJoy.get(i);
                    BundleOfJoy boj2 = bundlesOfJoy.get(j);
                    boj1.force.sum(force);
                    boj2.force.substract(force);
                }
            }
//            calculateWallInteractions(p1, area);
        }
    }

    private void calculateWallInteractions(Particle particle, Area area) {

        List<Particle> walls;

        walls = area.getWallPositions(particle);

        for (Particle wall: walls) {
            if (particle.isOverlapped(wall)) {
                double dx = (particle.getX() - wall.getX());
                double distance = particle.distance(wall);
                double dy = (particle.getY() - wall.getY());
                double radiusSum = particle.getRadius() + wall.getRadius();

                double f = -KN * (radiusSum - distance) - GAMMA * particle.relativeVelocityModule(wall);
                Pair force = new Pair(f * dx/distance, f * dy/distance);

                BundleOfJoy boj = bundlesOfJoy.get(particle.getId());
                boj.force.sum(force);
            }
        }
    }

    @Override
    public Pair recalculateForce(Particle particle, List<Particle> particles, Area area) {

        Pair forces = new Pair(0,0);
        for (Particle p : particles) {
            if (particle.getId() != p.getId()) {
                double dx = (particle.getX() - p.getX());
                double distance = particle.distance(p);
                double dy = (particle.getY() - p.getY());
                double radiusSum = particle.getRadius() + p.getRadius();

                double f = -KN * (radiusSum - distance) - GAMMA * particle.relativeVelocityModule(p);
                Pair force = new Pair(f * dx/distance, f * dy/distance);

                forces.sum(force);
            }

        }

//        List<Particle> walls = area.getWallPositions(particle);

//        for (Particle wall: walls) {
//            if (particle.isOverlapped(wall)) {
//                double dx = (particle.getX() - wall.getX());
//                double distance = particle.distance(wall);
//                double dy = (particle.getY() - wall.getY());
//                double radiusSum = particle.getRadius() + wall.getRadius();
//
//                double f = -KN * (radiusSum - distance) - GAMMA * particle.relativeVelocityModule(wall);
//                Pair force = new Pair(f * dx/distance, f * dy/distance);
//
//                forces.sum(force);
//            }
//        }

        return forces;
    }

    @Override
    public Pair getForce(Particle particle) {
        return new Pair(bundlesOfJoy.get(particle.getId()).force);
    }

    @Override
    public Pair getD1(Particle particle) {
        return new Pair(bundlesOfJoy.get(particle.getId()).d1);
    }

    @Override
    public Pair getD2(Particle particle) {
        return new Pair(bundlesOfJoy.get(particle.getId()).d2);
    }

    @Override
    public Pair getD3(Particle particle) {
        return new Pair(bundlesOfJoy.get(particle.getId()).d3);
    }

    @Override
    public Pair getAnalyticalSolution(final Particle particle, final double time) {
        return null;
    }

    @Override
    public boolean isVelocityDependant() {
        return velocityDependant;
    }

    private class BundleOfJoy {
        double potential;
        Pair force;
        Pair d1;
        Pair d2;
        Pair d3;

        BundleOfJoy() {
            this.potential = 0;
            this.force = new Pair(0, 0);
            this.d1 = new Pair(0, 0);
            this.d2 = new Pair(0, 0);
            this.d3 = new Pair(0, 0);
        }
    }

}
