import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import forces.GravitationalForce;
import forces.NonElasticCollision;
import integrators.VelocityVerlet;
import interfaces.Force;
import model.Area;
import model.Pair;
import model.Particle;

public class Simulation {

	static boolean append = false;
	static boolean appendData = false;

	public static void simulate(Options options) {
		VelocityVerlet vv = new VelocityVerlet();
		double dt = 1e-5;

		final Area area = generateParticles(options);
		System.out.println("generated");

		Set<Force> forces = new HashSet<>();
		forces.add(new NonElasticCollision(area, options.getMaxRadius()));
		forces.add(new GravitationalForce());

		List<Particle> previous = area.getParticles();
		for (Force force: forces) {
			force.calculate(previous, area);
		}

		double t = 0;
		int times = 0;
		logParticles(previous, area);

		List<Integer> upperParticles = new ArrayList<>();

		while(t < 10) {
			t += dt;
			times++;
			List<Particle> predicted = new ArrayList<>();
			List<Integer> outParticles = new ArrayList<>();
			for (Particle p: previous) {
				Particle predictedParticle = vv.evolve(p, dt, previous, forces, area);
				checkIfParticleIsOut(upperParticles, outParticles, predictedParticle, area);
				predicted.add(predictedParticle);
			}
			regenerateParticles(upperParticles, outParticles, predicted, area);
			previous = predicted;
			area.setParticles(predicted);
			for (Force force: forces) {
				force.calculate(previous, area);
			}

			if(times == Math.round(0.01/dt)) {
				times = 0;
				System.out.println(t);
				logParticles(previous, area);
			}
		}
	}

	private static void checkIfParticleIsOut(List<Integer> upperParticles, List<Integer> outParticles, Particle predictedParticle, Area area) {
		if(predictedParticle.getY() < 0) {
			outParticles.add(predictedParticle.getId());
		} else {
			if (predictedParticle.getY() + predictedParticle.getRadius() < area.getHeight() - 2.0 / 10) {
				upperParticles.remove(Integer.valueOf(predictedParticle.getId()));
			} else {
				if(!upperParticles.contains(predictedParticle.getId()))
					upperParticles.add(predictedParticle.getId());
			}
		}
	}

	private static void regenerateParticles(List<Integer> upperParticles, List<Integer> outParticles, List<Particle> predicted, Area area) {
		int i = 0;
		while (i < outParticles.size()) {
			int id1 = outParticles.get(i);
			Particle p1 = predicted.get(id1);
			p1.setVelocity(new Pair(0, 0));
			boolean overlapped = false;
			double x = rand(p1.getRadius(), area.getLength() - p1.getRadius());
			double y = rand(area.getHeight() - 3.0 / 10 + p1.getRadius(), area.getHeight() - p1.getRadius());
			p1.setPosition(new Pair(x, y));
			for (int j = 0; j < upperParticles.size(); j++) {
				int id2 = upperParticles.get(j);
				Particle p2 = predicted.get(id2);
				if (p1.isOverlapped(p2)) {
					overlapped = true;
					break;
				}
			}
			if (!overlapped) {
				upperParticles.add(p1.getId());
				i++;
			}
		}
	}

//	public static void simulate1(Options options) {
//		VelocityVerlet vv = new VelocityVerlet();
//		double dt = 1e-5;
//
//		Particle p1 = new Particle(0, 1, 2, 0, 0, 0.01, 0.1);
//		Particle p2 = new Particle(1, 6, 0, -1, 0, 0.01, 0.1);
//		List<Particle> previous = new ArrayList<> ();
//		previous.add(p1);
//		//previous.add(p2);
//
//		Set<Force> forces = new HashSet<>();
//		Area area = new Area(10,10,0.0,previous);
//		forces.add(new NonElasticCollision(area, 0.1));
//		forces.add(new GravitationalForce());
//
//		for (Force force: forces) {
//			force.calculate(previous, area);
//		}
//		logParticles(previous, area);
//		int times = 0;
//
//		while(true) {
//			List<Particle> predicted = new ArrayList<> ();
//			for (Particle p: previous) {
//				predicted.add(vv.evolve(p, dt, previous, forces, area));
//			}
//			previous = predicted;
//			for (Force force: forces) {
//				force.calculate(previous, area);
//			}
//
//			if(times == 10000) {
//				logParticles(previous, area);
//				times = 0;
//				System.out.println(times);
//			} else
//				times++;
//		}
//	}

	private static void logData(List<Particle> particles, double totalEnergy, double time, int leftParticles) {
		File file = new File("data.data");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, appendData);
			appendData = true;
		} catch (FileNotFoundException e) {
			return;
		}
		PrintStream ps = new PrintStream(fos);

		ps.println(particles.size());
		ps.println(totalEnergy + " " + leftParticles + " " + time);
		for (Particle p : particles) {
			ps.println(p.getVelocityModule());
		}

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
		ps.println("Lattice=\"1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0\"");
		for (Particle p : particles) {
			ps.println(p.getX() + " " + p.getY() + " " + p.getVx() + " " + p.getVy()
					+ " " + p.getRadius() + " " + p.getPressure());
		}

		ps.close();
	}

	private static double calculateKineticEnergy(final List<Particle> particles, final Force force) {

		return particles.stream()
				.mapToDouble(particle -> 0.5 * (particle.getMass()) * (Math.pow(particle.getVx(), 2)
						+ Math.pow(particle.getVy(), 2)))
				.average().orElse(0);
	}

	public static Area generateParticles(Options options) {

		int i = 0;
		List<Particle> particles = new ArrayList<>();
		boolean overlapped;
		int overlappedTimes = 0;

		while(overlappedTimes < 10000 && particles.size() < options.getN()) {
			double ang = rand(0, 2 * Math.PI);
			double mod = options.getVelocity();
			double vx = mod * Math.cos(ang);
			double vy = mod * Math.sin(ang);
			double radius = rand(options.getMinRadius(), options.getMaxRadius());
			double x = rand(radius, options.getLength() - radius);
			double y = rand(radius + 1.0/10, options.getHeight() - radius);

			Particle particle = new Particle(i, x, y, vx, vy, options.getMass(), radius);
			overlapped = false;
			for (Particle p : particles) {
				if (particle.isOverlapped(p)) {
					overlapped = true;
					overlappedTimes++;
					break;
				}
			}
			if (!overlapped) {
				particles.add(particle);
				overlappedTimes = 0;
				i++;
			}
		}

		return new Area(options.getLength(), options.getHeight(), options.getHole(), particles);
	}

	private static double rand(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

}
