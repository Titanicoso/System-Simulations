import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
	
	private final static int N = 10;
	private final static double L = 20.0;
	private final static double RC = 1.0;
	private final static int M = 5;
	private final static boolean PERIODIC = true;

	public static void main(String[] args) {
		final Particle[] particles = new Particle[N];
		final Area area = new Area(L, RC, particles, PERIODIC);
		
		for (int i = 0; i < N; i++) {
			particles[i] = new Particle(i, rand(0, L), rand(0, L), 1.0);
		}

		final long bruteStart = System.nanoTime();
		final Map<Integer, List<Particle>> bruteNeighbours = BruteForceMethod.findNeighbours(area);
		final long bruteEnd = System.nanoTime();

		printNeighbours(particles, bruteNeighbours);

		long cellStart = System.nanoTime();
		final Map<Integer, List<Particle>> cellNeighbours = CellIndexMethod.findNeighbours(area, M);
		final long cellEnd = System.nanoTime();

		printNeighbours(particles, cellNeighbours);
		
		System.out.println("Brute: " + (bruteEnd - bruteStart));
		System.out.println("Cell: " + (cellEnd - cellStart));
	}
	
	private static void printNeighbours(Particle[] particles, Map<Integer, List<Particle>> neighbours) {
		for (Particle particle: particles) {
			System.out.print(particle.getId() + ": ");
			neighbours.get(particle.getId()).stream()
					.map(neighbour -> neighbour.getId() + " ").forEach(System.out::print);
			System.out.println();
		}
		System.out.println();
	}
	
	private static double rand(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

}
