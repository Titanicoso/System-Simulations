import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import model.*;

public class Simulation {

	static boolean append = false;
	static boolean appendData = false;
	static boolean appendFlow = false;

	public static void simulate(Options options) {
		double dt = 1e-4;
		final Area area = generateParticles(options);
		System.out.println("generated");
		CellIndexMethod cim = new CellIndexMethod(area, options.getMaxRadius());
		final ContractileParticleModel cpm = new ContractileParticleModel(
		        options.getVelocity(), options.getMaxRadius(), area);

		double t = 0;
		int times = 0;

		Map<Integer, Double> outOfHoleParticles = new HashMap<>();
        List<Particle> previous = area.getParticles();
        List<Particle> predicted;

		while(!previous.isEmpty()) {
			t += dt;
			times++;

			Map<Integer, List<Particle>> neighbours = cim.findNeighbours(area);
            predicted = new ArrayList<>();

			for (Particle particle : previous) {
                Particle predictedParticle = cpm.evolve(particle,
                        neighbours.get(particle.getId()), dt);

                if(predictedParticle.getY() >= 1.0) {
                    predicted.add(predictedParticle);
                } else {
                    outOfHoleParticles.put(predictedParticle.getId(), t);
                }
			}

			previous = predicted;
			area.setParticles(predicted);

			if (times == Math.round(0.1 / dt)) {
				times = 0;
				System.out.println(t);
				logParticles(previous, area);
			}
		}
	}

	private static void checkIfParticleNeedsRegen(List<Integer> upperParticles, List<Integer> outParticles, Particle predictedParticle, Area area) {
		if(predictedParticle.getY() < 0) {
			outParticles.add(predictedParticle.getId());
		} else {
			if (predictedParticle.getY() + predictedParticle.getRadius() < area.getHeight() - 3.0 / 10) {
				upperParticles.remove(Integer.valueOf(predictedParticle.getId()));
			} else {
				if(!upperParticles.contains(predictedParticle.getId()))
					upperParticles.add(predictedParticle.getId());
			}
		}
	}

	private static void checkIfParticleIsOut(Particle predictedParticle, Area area, Map<Integer, Double> outOfHole, double time) {
		if(predictedParticle.getY() < area.getExtraSpace() && !outOfHole.containsKey(predictedParticle.getId()))
			outOfHole.put(predictedParticle.getId(), time);
	}

	private static void logData(double totalEnergy, double time) {
		File file = new File("data.data");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, appendData);
			appendData = true;
		} catch (FileNotFoundException e) {
			return;
		}
		PrintStream ps = new PrintStream(fos);
		ps.println(totalEnergy + " " + time);

		ps.close();
	}

	private static void logFlow(Map<Integer, Double> outParticles, List<Integer> regenParticles) {
		File file = new File("flow.data");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, appendFlow);
			appendFlow = true;
		} catch (FileNotFoundException e) {
			return;
		}
		PrintStream ps = new PrintStream(fos);
		outParticles.entrySet().stream()
				.filter(entry -> regenParticles.contains(entry.getKey()))
				.forEach(entry -> ps.println(entry.getValue()));

		ps.close();
	}

	private static void logParticles(List<Particle> particles, Area area) {
		File file = new File("output.xyz");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, append);
			append = true;
		} catch (FileNotFoundException e) {
			return;
		}
		PrintStream ps = new PrintStream(fos);

		ps.println(particles.size());
		ps.println("Lattice=\"20.0 0.0 0.0 0.0 21.0 0.0 0.0 0.0 1.0\"");
		for (Particle p : particles) {
			ps.println(p.getId() + " " + p.getX() + " " + p.getY() + " " + p.getVx() + " " + p.getVy()
					+ " " + p.getInteractionRadius());
		}

		ps.close();
	}

	public static Area generateParticles(Options options) {

		int i = 0;
		List<Particle> particles = new ArrayList<>();
		boolean overlapped;

		while(particles.size() < options.getN()) {
			double x = rand(options.getMaxRadius(), options.getLength() - options.getMaxRadius());
			double y = rand(options.getMaxRadius() + 1.0, options.getHeight() - options.getMaxRadius());

			Particle particle = new Particle(i, x, y, 0.0, 0.0, options.getMinRadius(), options.getMaxRadius());
			overlapped = false;
			for (Particle p : particles) {
				if (particle.isOverlapped(p)) {
					overlapped = true;
					break;
				}
			}
			if (!overlapped) {
				particles.add(particle);
				i++;
			}
		}

		return new Area(options.getLength(), options.getHeight(), options.getHole(), options.getExtraSpace(), particles);
	}

	private static double rand(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

}
