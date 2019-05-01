package forces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.Force;
import model.Area;
import model.CellIndexMethod;
import model.Pair;
import model.Particle;

public class LennardJonesGas implements Force {

    private static final double RM = 1.0;
    private static final double E = 2.0;
    private static final double R = 5.0;
    private Map<Integer, BundleOfJoy> bundlesOfJoy;
    private CellIndexMethod cim;

	public LennardJonesGas(Area area) {
		this.bundlesOfJoy = new HashMap<>();
		cim = new CellIndexMethod(R, area);
	}

	@Override
	public void calculate(List<Particle> particles, Area area) {
		bundlesOfJoy.clear();
		final Map<Integer, List<Particle>> neighbours = cim.findNeighbours(area);

		for (int i = 0; i < particles.size(); i++) {

			Particle p1 = particles.get(i);
			if (!bundlesOfJoy.containsKey(p1.getId())) {
				BundleOfJoy boj = new BundleOfJoy();
				bundlesOfJoy.put(p1.getId(), boj);
			}
			if(neighbours.containsKey(i)) {
				List<Particle> particleNeighbours = neighbours.get(p1.getId());

				for (Particle neighbour : particleNeighbours) {
					if (!bundlesOfJoy.containsKey(neighbour.getId())) {
						BundleOfJoy boj = new BundleOfJoy();
						bundlesOfJoy.put(neighbour.getId(), boj);
					}
					double dx = (p1.getX() - neighbour.getX());
					double distance = p1.distance(neighbour);
					double dy = (p1.getY() - neighbour.getY());

					double potential = E * (Math.pow(RM / distance, 12) - 2 * Math.pow(RM / distance, 6));

					double f = 12 * E / RM * (Math.pow(RM / distance, 13) - Math.pow(RM / distance, 7));
					Pair force = new Pair(f * dx / distance, f * dy / distance);

					double f1 = 12 * E * Math.pow(RM, 6) * (7 * Math.pow(distance, 6) - 13 * Math.pow(RM, 6)) / Math.pow(distance, 14);
					Pair d1 = new Pair(f1 * dx / distance, f1 * dy / distance);

					double f2 = 168 * E * Math.pow(RM, 6) * (-4 * Math.pow(dx, 6) + 13 * Math.pow(RM, 6)) / Math.pow(dx, 15);
					Pair d2 = new Pair(f2 * dx / distance, f2 * dy / distance);

					double f3 = -504 * E * Math.pow(RM, 6) * (-12 * Math.pow(dx, 6) + 65 * Math.pow(RM, 6)) / Math.pow(dx, 16);
					Pair d3 = new Pair(f3 * dx / distance, f3 * dy / distance);

					BundleOfJoy boj1 = bundlesOfJoy.get(p1.getId());
					BundleOfJoy boj2 = bundlesOfJoy.get(neighbour.getId());
					boj1.potential += potential;
					boj2.potential += potential;
					boj1.force.sum(force);
					boj2.force.substract(force);
					boj1.d1.sum(d1);
					boj2.d1.substract(d1);
					boj1.d2.sum(d2);
					boj2.d2.substract(d2);
					boj1.d3.sum(d3);
					boj2.d3.substract(d3);
				}
			}
			calculateWallInteractions(p1, area);
		}
	}

    public void calculate1(List<Particle> particles, Area area) {
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
    			if (p1.distance(p2) <= R && area.forceInteraction(p1, p2)) {
    				double dx = (p1.getX() - p2.getX());
    				double distance = p1.distance(p2);
    				double dy = (p1.getY() - p2.getY());

    				double f = 12 * E / RM * (Math.pow(RM / distance, 13) - Math.pow(RM / distance, 7));
    				Pair force = new Pair(f * dx/distance, f * dy/distance);

    				double f1 = 12 * E * Math.pow(RM, 6) * (7 * Math.pow(distance, 6) - 13 * Math.pow(RM, 6)) / Math.pow(distance, 14);
					Pair d1 = new Pair(f1 * dx/distance, f1 * dy/distance);

					double f2 = 168 * E * Math.pow(RM, 6) * (-4 * Math.pow(dx, 6) + 13 * Math.pow(RM, 6)) / Math.pow(dx, 15);
					Pair d2 = new Pair(f2 * dx/distance, f2 * dy/distance);

					double f3 = -504 * E * Math.pow(RM, 6) * (-12 * Math.pow(dx, 6) + 65 * Math.pow(RM, 6)) / Math.pow(dx, 16);
					Pair d3 = new Pair(f3 * dx/distance, f3 * dy/distance);

    				BundleOfJoy boj1 = bundlesOfJoy.get(i);
    				BundleOfJoy boj2 = bundlesOfJoy.get(j);
    				boj1.force.sum(force);
    				boj2.force.substract(force);
    				boj1.d1.sum(d1);
    				boj2.d1.substract(d1);
    				boj1.d2.sum(d2);
    				boj2.d2.substract(d2);
    				boj1.d3.sum(d3);
    				boj2.d3.substract(d3);
    			}
    		}
			calculateWallInteractions(p1, area);
    	}
    }

	private void calculateWallInteractions(Particle particle, Area area) {

		List<Pair> walls;

		walls = area.getWallPositions(particle);

		for (Pair wall: walls) {
			if (wall.distance(particle.getPosition()) * 2 <= R) {
				double dx = (particle.getX() - wall.getX()) * 2;
				double dy = (particle.getY() - wall.getY()) * 2;
				double distance = wall.distance(particle.getPosition()) * 2;

				double potential = E  * (Math.pow(RM / distance, 12) - 2 * Math.pow(RM / distance, 6));

				double f = 12 * E / RM * (Math.pow(RM / distance, 13) - Math.pow(RM / distance, 7));
				Pair force = new Pair(f * dx/distance, f * dy/distance);

				double f1 = 12 * E * Math.pow(RM, 6) * (7 * Math.pow(distance, 6) - 13 * Math.pow(RM, 6)) / Math.pow(distance, 14);
				Pair d1 = new Pair(f1 * dx/distance, f1 * dy/distance);

				double f2 = 168 * E * Math.pow(RM, 6) * (-4 * Math.pow(dx, 6) + 13 * Math.pow(RM, 6)) / Math.pow(dx, 15);
				Pair d2 = new Pair(f2 * dx/distance, f2 * dy/distance);

				double f3 = -504 * E * Math.pow(RM, 6) * (-12 * Math.pow(dx, 6) + 65 * Math.pow(RM, 6)) / Math.pow(dx, 16);
				Pair d3 = new Pair(f3 * dx/distance, f3 * dy/distance);

				BundleOfJoy boj = bundlesOfJoy.get(particle.getId());
				boj.potential += potential;
				boj.force.sum(force);
				boj.d1.sum(d1);
				boj.d2.sum(d2);
				boj.d3.sum(d3);
			}
		}
	}

	@Override
	public Pair recalculateForce(Particle particle, List<Particle> particles, Area area) {
		Pair forces = new Pair(0,0);
		List<Particle> neighbours = cim.predictParticleNeighbours(particle, area);
		for (int i = 0; i < neighbours.size(); i++) {
			Particle p = neighbours.get(i);

			double dx = (particle.getX() - p.getX());
			double dy = (particle.getY() - p.getY());
			double distance = particle.distance(p);

			double f = 12 * E / RM * (Math.pow(RM / distance, 13) - Math.pow(RM / distance, 7));
			Pair force = new Pair(f * dx/distance, f * dy/distance);

			forces.sum(force);
		}

		List<Pair> walls = area.getWallPositions(particle);

		for (Pair wall: walls) {
			if (particle.getPosition().distance(wall) <= R) {
				double dx = (particle.getX() - wall.getX()) * 2;
				double dy = (particle.getY() - wall.getY()) * 2;
				double distance = wall.distance(particle.getPosition()) * 2;

				double f = 12 * E / RM * (Math.pow(RM / distance, 13) - Math.pow(RM / distance, 7));
				Pair force = new Pair(f * dx/distance, f * dy/distance);

				forces.sum(force);
			}
		}

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
	public Pair getAnalyticalSolution(Particle particle, double time) {
		return null;
	}
	
	@Override
	public boolean isVelocityDependant() {
		return false;
	}

	@Override
	public double getPotentialEnergy(Particle particle) {
		return bundlesOfJoy.get(particle.getId()).potential;
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
