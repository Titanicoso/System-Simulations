package forces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.Force;
import model.Area;
import model.CellIndexMethod;
import model.Pair;
import model.Particle;

public class NonElasticCollision implements Force {

    private static final double KN = 1e5;
    private static final double KT = 2e5;
    private static final double GAMMA = 70.0;
    private Map<Integer, BundleOfJoy> bundlesOfJoy;
    private CellIndexMethod cim;
    private static final boolean velocityDependant = true;

    public NonElasticCollision(Area area, Double maxRadius) {
		cim = new CellIndexMethod(area, maxRadius);
	}

	@Override
    public void calculate(List<Particle> particles, Area area) {
        bundlesOfJoy = new HashMap<>();
        final Map<Integer, List<Particle>> neighbours = cim.findNeighbours(area);

        for (int i = 0; i < particles.size(); i++) {
            if (!bundlesOfJoy.containsKey(i)) {
                BundleOfJoy boj = new BundleOfJoy();
                bundlesOfJoy.put(i, boj);
            }
            Particle p1 = particles.get(i);

            if(neighbours.containsKey(i)) {
                List<Particle> particleNeighbours = neighbours.get(p1.getId());

                for (Particle neighbour : particleNeighbours) {
                    if (!bundlesOfJoy.containsKey(neighbour.getId())) {
                        BundleOfJoy boj = new BundleOfJoy();
                        bundlesOfJoy.put(neighbour.getId(), boj);
                    }

                    if (p1.isOverlapped(neighbour)) {
                        double dx = (p1.getX() - neighbour.getX());
                        double distance = p1.distance(neighbour);
                        double dy = (p1.getY() - neighbour.getY());
                        double radiusSum = p1.getRadius() + neighbour.getRadius();

                        double fn = -KN * (radiusSum - distance) - GAMMA * getOverlapDerivative(p1, neighbour);
                        //Pair relativeVelocity = p1.getRelativeVelocity(neighbour);
                        double ft = 0;
                        //double ft = -KT * (radiusSum - distance) *
                        //        (relativeVelocity.getX() * -dy / distance + relativeVelocity.getX() * dx / distance);
                        Pair force = new Pair(fn * dx / distance + ft * -dy / distance,
                                fn * dy / distance + ft * dx / distance);

                        BundleOfJoy boj1 = bundlesOfJoy.get(i);
                        BundleOfJoy boj2 = bundlesOfJoy.get(neighbour.getId());
                        boj2.force.sum(force);
                        boj1.force.substract(force);
                        boj1.pressure += fn;
                        boj2.pressure -= fn;
                    }
                }
            }
            calculateWallInteractions(p1, area);
            BundleOfJoy boj = bundlesOfJoy.get(i);
            boj.pressure /= 2 * Math.PI * p1.getRadius();
            p1.setPressure(boj.pressure);
        }
    }

	public void calculate1(List<Particle> particles, Area area) {

        bundlesOfJoy = new HashMap<>();
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
                if (p1.isOverlapped(p2) && p1.getY() > 0 && p2.getY() > 0) {
                    double dx = (p1.getX() - p2.getX());
                    double distance = p1.distance(p2);
                    double dy = (p1.getY() - p2.getY());
                    double radiusSum = p1.getRadius() + p2.getRadius();

                    double fn = -KN * (radiusSum - distance)/* - GAMMA * getOverlapDerivative(p1, neighbour)*/;
                    Pair relativeVelocity = p1.getRelativeVelocity(p2);
                    double ft = -KT * (radiusSum - distance) *
                            (relativeVelocity.getX() * -dy / distance + relativeVelocity.getX() * dx / distance);
                    Pair force = new Pair(fn * dx / distance + ft * -dy / distance,
                            fn * dy / distance + ft * dx / distance);

                    BundleOfJoy boj1 = bundlesOfJoy.get(i);
                    BundleOfJoy boj2 = bundlesOfJoy.get(j);
                    boj2.force.sum(force);
                    boj1.force.substract(force);
                }
            }
            calculateWallInteractions(p1, area);
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

                double fn = -KN * (radiusSum - distance) - GAMMA * getOverlapDerivative(particle, wall);
                /*Pair relativeVelocity = particle.getRelativeVelocity(wall);
                double ft = -KT * (radiusSum - distance) *
                        (relativeVelocity.getX() * -dy / distance + relativeVelocity.getX() * dx / distance);*/
                double ft = 0;
                Pair force = new Pair(fn * dx / distance + ft * -dy / distance,
                        fn * dy / distance + ft * dx / distance);

                BundleOfJoy boj = bundlesOfJoy.get(particle.getId());
                boj.force.substract(force);
                boj.pressure += fn;
            }
        }
    }


    @Override
    public Pair recalculateForce(Particle particle, List<Particle> particles, Area area) {
        Pair forces = new Pair(0,0);
		List<Particle> neighbours = cim.predictParticleNeighbours(particle, area);
		for (int i = 0; i < neighbours.size(); i++) {
			Particle p = neighbours.get(i);
            if (particle.getId() != p.getId() && particle.isOverlapped(p)) {
                double dx = (particle.getX() - p.getX());
                double distance = particle.distance(p);
                double dy = (particle.getY() - p.getY());
                double radiusSum = particle.getRadius() + p.getRadius();

                double fn = -KN * (radiusSum - distance) - GAMMA * getOverlapDerivative(particle, p);
                /*Pair relativeVelocity = particle.getRelativeVelocity(p);
                double ft = -KT * (radiusSum - distance) *
                        (relativeVelocity.getX() * -dy / distance + relativeVelocity.getX() * dx / distance);*/
                double ft = 0;
                Pair force = new Pair(fn * dx / distance + ft * -dy / distance,
                        fn * dy / distance + ft * dx / distance);

                forces.substract(force);
            }
        }

        List<Particle> walls = area.getWallPositions(particle);

        for (Particle wall: walls) {
            if (particle.isOverlapped(wall)) {
                double dx = (particle.getX() - wall.getX());
                double distance = particle.distance(wall);
                double dy = (particle.getY() - wall.getY());
                double radiusSum = particle.getRadius() + wall.getRadius();

                double fn = -KN * (radiusSum - distance) - GAMMA * getOverlapDerivative(particle, wall);
                /*Pair relativeVelocity = particle.getRelativeVelocity(wall);
                double ft = -KT * (radiusSum - distance) *
                        (relativeVelocity.getX() * -dy / distance + relativeVelocity.getX() * dx / distance);*/
                double ft = 0;
                Pair force = new Pair(fn * dx / distance + ft * -dy / distance,
                        fn * dy / distance + ft * dx / distance);

                forces.substract(force);
            }
        }

        return forces;
    }

    public Pair recalculateForce1(Particle particle, List<Particle> particles, Area area) {

        Pair forces = new Pair(0,0);
        for (Particle p : particles) {
            if (particle.getId() != p.getId() && particle.isOverlapped(p) && particle.getY() > 0 && p.getY() > 0) {
                double dx = (particle.getX() - p.getX());
                double distance = particle.distance(p);
                double dy = (particle.getY() - p.getY());
                double radiusSum = particle.getRadius() + p.getRadius();

                double fn = -KN * (radiusSum - distance)/* - GAMMA * getOverlapDerivative(p1, neighbour)*/;
                Pair relativeVelocity = particle.getRelativeVelocity(p);
                double ft = -KT * (radiusSum - distance) *
                        (relativeVelocity.getX() * -dy / distance + relativeVelocity.getX() * dx / distance);
                Pair force = new Pair(fn * dx / distance + ft * -dy / distance,
                        fn * dy / distance + ft * dx / distance);

                forces.substract(force);
            }

        }

        List<Particle> walls = area.getWallPositions(particle);

        for (Particle wall: walls) {
            if (particle.isOverlapped(wall) && particle.getY() > 0) {
                double dx = (particle.getX() - wall.getX());
                double distance = particle.distance(wall);
                double dy = (particle.getY() - wall.getY());
                double radiusSum = particle.getRadius() + wall.getRadius();

                double fn = -KN * (radiusSum - distance)/* - GAMMA * getOverlapDerivative(p1, neighbour)*/;
                Pair relativeVelocity = particle.getRelativeVelocity(wall);
                double ft = -KT * (radiusSum - distance) *
                        (relativeVelocity.getX() * -dy / distance + relativeVelocity.getX() * dx / distance);
                Pair force = new Pair(fn * dx / distance + ft * -dy / distance,
                        fn * dy / distance + ft * dx / distance);

                forces.substract(force);
            }
        }

        return forces;
    }

    private double getOverlapDerivative(Particle p1, Particle p2) {
        Pair relativeVelocity = p2.getRelativeVelocity(p1);
        double relativeVelocityModule = p1.relativeVelocityModule(p2);
        if(relativeVelocityModule == 0)
            return 0;
        Pair versor = new Pair(relativeVelocity).multiplyByScalar(1/relativeVelocityModule);
        return versor.getX() * relativeVelocity.getX() + versor.getY() * relativeVelocity.getY();
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
        double pressure;
        Pair force;
        Pair d1;
        Pair d2;
        Pair d3;

        BundleOfJoy() {
            this.pressure = 0;
            this.force = new Pair(0, 0);
            this.d1 = new Pair(0, 0);
            this.d2 = new Pair(0, 0);
            this.d3 = new Pair(0, 0);
        }
    }

}
