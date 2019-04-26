package forces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.Force;
import model.Pair;
import model.Particle;

public class LennardJonesGas implements Force {

    private static final double RM = 1.0;
    private static final double E = 2.0;
    private static final double R = 5.0;
    private Map<Integer, BundleOfJoy> bundlesOfJoy = new HashMap<>();
    
    @Override
    public void calculate(List<Particle> particles) {
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
    				double dy = (p1.getY() - p2.getY());
    				Pair force = new Pair(
    						12 * E / RM * (Math.pow(RM / dx, 13) - Math.pow(RM / dx, 7)),
    						12 * E / RM * (Math.pow(RM / dy, 13) - Math.pow(RM / dy, 7))
    				);
    				Pair d1 = new Pair(
    						12 * E * Math.pow(RM, 6) * (7 * Math.pow(dx, 6) - 13 * Math.pow(RM, 6) / Math.pow(dx, 14)),
    						12 * E * Math.pow(RM, 6) * (7 * Math.pow(dy, 6) - 13 * Math.pow(RM, 6) / Math.pow(dy, 14))
    				);
    				Pair d2 = new Pair(
    						168 * E * Math.pow(RM, 6) * (-4 * Math.pow(dx, 6) + 13 * Math.pow(RM, 6) / Math.pow(dx, 15)),
    						168 * E * Math.pow(RM, 6) * (-4 * Math.pow(dy, 6) + 13 * Math.pow(RM, 6) / Math.pow(dy, 15))
    				);
    				Pair d3 = new Pair(
    						-504 * E * Math.pow(RM, 6) * (-12 * Math.pow(dx, 6) + 65 * Math.pow(RM, 6) / Math.pow(dx, 16)),
    						-504 * E * Math.pow(RM, 6) * (-12 * Math.pow(dy, 6) + 65 * Math.pow(RM, 6) / Math.pow(dy, 16))
    				);
    				BundleOfJoy boj1 = bundlesOfJoy.get(i);
    				BundleOfJoy boj2 = bundlesOfJoy.get(j);
    				boj1.force.sum(force);
    				boj2.force.substract(force);
    				boj1.d1.sum(d1);
    				boj2.d1.sum(d1);
    				boj1.d2.sum(d2);
    				boj2.d2.substract(d2);
    				boj1.d3.sum(d3);
    				boj2.d3.sum(d3);
    			}
    		}
    	}
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
		
		public BundleOfJoy() {
			this.force = new Pair(0, 0);
			this.d1 = new Pair(0, 0);
			this.d2 = new Pair(0, 0);
			this.d3 = new Pair(0, 0);
		}
	}
	
}
