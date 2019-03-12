import java.util.concurrent.ThreadLocalRandom;

public class Main {
	
	private final static int N = 2;
	private final static double L = 10.0;
	private final static double RC = 1.0;
	private final static int M = 5;
	private final static boolean CONT = false;

	public static void main(String[] args) {
		final Particle[] particles = new Particle[N];
		final Area area = new Area(L, RC, particles);
		
//		for (int i = 0; i < N; i++) {
//			particles[i] = new Particle(i, rand(0, L), rand(0, L), 1.0);
//		}

		particles[0] = new Particle(0, 1.3, 0, 1.0);
		particles[1] = new Particle(1, 5.2, 2.1, 1.0);
		
		BruteForceMethod.findNeighbours(area, CONT);
	}
	
	private static double rand(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

}
