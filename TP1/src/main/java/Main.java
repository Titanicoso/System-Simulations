import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

	public static void main(String[] args) {
		Options options = new Options(args);
		
		final Particle[] particles = new Particle[options.getN()];
		final Area area = new Area(options.getL(), options.getRc(), particles, options.isPeriodic());
		
		if (options.getInput() != null) {
			readInput(particles, options);
		} else {
			if (options.getMR() == null) {
				for (int i = 0; i < options.getN(); i++) {
					particles[i] = new Particle(i, rand(0, options.getL()), rand(0, options.getL()), options.getR());
				}
			} else {
				for (int i = 0; i < options.getN(); i++) {
					particles[i] = new Particle(i, rand(0, options.getL()), rand(0, options.getL()), rand(0, options.getMR()));
				}
			}
		}

		final long bruteStart = System.nanoTime();
		final Map<Integer, List<Particle>> bruteNeighbours = BruteForceMethod.findNeighbours(area);
		final long bruteEnd = System.nanoTime();

		printNeighbours(particles, bruteNeighbours);
		
		if (options.getM() == null)
			options.setM(CellIndexMethod.findMaximumM(area));

		long cellStart = System.nanoTime();
		final Map<Integer, List<Particle>> cellNeighbours = CellIndexMethod.findNeighbours(area, options.getM());
		final long cellEnd = System.nanoTime();

		printNeighbours(particles, cellNeighbours);

		logPoints(particles, options);
		logNeighbours(cellNeighbours);
		
		System.out.println("Brute: " + (bruteEnd - bruteStart) + " ns = " + (bruteEnd - bruteStart) * 0.000000001 + " s");
		System.out.println("Cell: " + (cellEnd - cellStart) + " ns = " + (cellEnd - cellStart) * 0.000000001 + " s");
		System.out.println("Maximum M: " + CellIndexMethod.findMaximumM(area));
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

	private static void logPoints(final Particle[] particles, final Options options) {
		File file = new File("utils/points.txt");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			return;
		}
		PrintStream ps = new PrintStream(fos);

		ps.println(options.getL() + " " + options.getRc() + " " + options.getM());

		Arrays.stream(particles).forEach(particle -> {
			ps.println(
					particle.getX() + " " + particle.getY() + " " + particle.getRatio()
			);
		});
		
		ps.close();
	}

	private static void logNeighbours(final Map<Integer, List<Particle>> neighbours) {
		File file = new File("utils/out.txt");
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
		
		ps.close();
	}

	private static String list(final List<Particle> neighbours) {
		StringBuilder list = new StringBuilder();
		neighbours.forEach(particle -> list.append(particle.getId() + 1).append(" "));
		if(list.length() > 0)
			return list.substring(0, list.length() - 1);
		return list.toString();
	}
	
	private static void readInput(final Particle[] particles, final Options options) {
		File input = options.getInput();
		try {
			BufferedReader br = new BufferedReader(new FileReader(input));
			String st;
			int i = 0;
			while (i < options.getN() && (st = br.readLine()) != null) {
				Scanner scanner = new Scanner(st);
				scanner.useLocale(Locale.US);
				particles[i] = new Particle(i, scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
				scanner.close();
				i++;
			}
			br.close();
		} catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
		} catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
		}
	}
	
	private static double rand(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

}
