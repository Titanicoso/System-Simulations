package forces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.Force;
import model.Area;
import model.Pair;
import model.Particle;

public class LennardJonesGas implements Force {

    private static final double RM = 1.0;
    private static final double E = 2.0;
    private static final double R = 5.0;
    private Map<Integer, BundleOfJoy> bundlesOfJoy = new HashMap<>();
    
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
    			if (p1.distance(p2) <= R) {
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

		if (!bundlesOfJoy.containsKey(particle.getId())) {
			BundleOfJoy boj = new BundleOfJoy();
			bundlesOfJoy.put(particle.getId(), boj);
		}
		walls = area.getWallPositions(particle);

		for (Pair wall: walls) {
			if (wall.distance(particle.getPosition()) <= R) {
				double dx = (particle.getX() - wall.getX()) * 2;
				double dy = (particle.getY() - wall.getY()) * 2;
				double distance = wall.distance(particle.getPosition()) * 2;

				double f = 12 * E / RM * (Math.pow(RM / distance, 13) - Math.pow(RM / distance, 7));
				Pair force = new Pair(f * dx/distance, f * dy/distance);

				double f1 = 12 * E * Math.pow(RM, 6) * (7 * Math.pow(distance, 6) - 13 * Math.pow(RM, 6)) / Math.pow(distance, 14);
				Pair d1 = new Pair(f1 * dx/distance, f1 * dy/distance);

				double f2 = 168 * E * Math.pow(RM, 6) * (-4 * Math.pow(dx, 6) + 13 * Math.pow(RM, 6)) / Math.pow(dx, 15);
				Pair d2 = new Pair(f2 * dx/distance, f2 * dy/distance);

				double f3 = -504 * E * Math.pow(RM, 6) * (-12 * Math.pow(dx, 6) + 65 * Math.pow(RM, 6)) / Math.pow(dx, 16);
				Pair d3 = new Pair(f3 * dx/distance, f3 * dy/distance);

				BundleOfJoy boj = bundlesOfJoy.get(particle.getId());
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
		for (int i = 0; i < particles.size(); i++) {
			if (i != particle.getId()) {
				Particle p = particles.get(i);

				if (particle.distance(p) <= R) {
					double dx = (particle.getX() - p.getX());
					double dy = (particle.getY() - p.getY());
					double distance = particle.distance(p);

					double f = 12 * E / RM * (Math.pow(RM / distance, 13) - Math.pow(RM / distance, 7));
					Pair force = new Pair(f * dx/distance, f * dy/distance);

					forces.sum(force);
				}
			}
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
	
	private class BundleOfJoy {
		Pair force;
		Pair d1;
		Pair d2;
		Pair d3;
		
		BundleOfJoy() {
			this.force = new Pair(0, 0);
			this.d1 = new Pair(0, 0);
			this.d2 = new Pair(0, 0);
			this.d3 = new Pair(0, 0);
		}
	}
	
}
