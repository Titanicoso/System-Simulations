import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
	
	private final static int N = 60;
	private final static double L = 20.0;
	private final static double RC = 1.0;
	private final static int M = 5;
	private final static boolean PERIODIC = false;

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

		logPoints(particles);
		logNeighbours(cellNeighbours);
		
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

	private static void logPoints(final Particle[] particles) {
		File file = new File("points.txt");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			return;
		}
		PrintStream ps = new PrintStream(fos);

		ps.println(L + " " + RC + particles[0].getRatio());

		Arrays.stream(particles).forEach(particle -> {
			ps.println(
					particle.getX() + " " + particle.getY()
			);
		});
	}

	private static void logNeighbours(final Map<Integer, List<Particle>> neighbours) {
		File file = new File("out.txt");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			return;
		}
		PrintStream ps = new PrintStream(fos);

		neighbours.forEach((particle, adjacent) -> {
			ps.println(
					(particle + 1) + " " + list(adjacent)
			);
		});
	}

	private static String list(final List<Particle> neighbours) {
		StringBuilder list = new StringBuilder();
		neighbours.forEach(particle -> list.append(particle.getId() + 1).append(" "));
		if(list.length() > 0)
			return list.substring(0, list.length() - 1);
		return list.toString();
	}

}
