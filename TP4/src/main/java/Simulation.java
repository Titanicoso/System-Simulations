import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import forces.DampedOscillator;
import forces.LennardJonesGas;
import integrators.Beeman;
import integrators.GearPredictorCorrector;
import integrators.VelocityVerlet;
import interfaces.Force;
import model.Area;
import model.Pair;
import model.Particle;

public class Simulation {

	static boolean append = false;
	static boolean appendData = false;

	public static void simulate(Options options) {
		VelocityVerlet gpc = new VelocityVerlet();
		double dt = 1e-4;
		double t = 0;
		int times = 0;
		double fraction = 1;
		Area area = generateParticles(options);
		logParticles(area.getParticles());
		Force f = new LennardJonesGas(area);
		List<Particle> previous = area.getParticles();
		f.calculate(previous, area);
		double initialEnergy = calculateEnergy(previous, f);
		System.out.println(initialEnergy);
		while(fraction > 0.5) {
			List<Particle> predicted = new ArrayList<> ();
			int leftParticles = 0;
			for (Particle p: previous) {
				predicted.add(gpc.evolve(p, dt, previous, f, area));
				if(area.leftBox(p)) {
					leftParticles++;
				}
			}
			previous = predicted;
			area.setParticles(predicted);
			fraction = (float)leftParticles/predicted.size();
			times++;
			t += dt;
			f.calculate(previous, area);
			if(times == 1000) {
				times = 0;
				double energy = calculateEnergy(previous, f);
				logData(previous, energy, t, leftParticles);
				logParticles(previous);
				System.out.println(leftParticles);
				System.out.println(energy);
			}
		}
		System.out.println(t);
	}
	
	public static void simulate1(Options options) {
		Particle particle = new Particle(0, 1.0, 0.0, -10.0/14, 0, 70);
		Particle particle4 = new Particle(0, 1.0, 0.0, -10.0/14, 0, 70);
		Particle particle1 = new Particle(0, 1.0, 0.0, -10.0/14, 0, 70);
		Particle particle2 = new Particle(0, 1.0, 0.0, -10.0/14, 0, 70);
		Particle particle3 = new Particle(0, 1.0, 0.0, -10.0/14, 0, 70);
		Beeman beeman = new Beeman();
		GearPredictorCorrector gpc = new GearPredictorCorrector();
		VelocityVerlet vv = new VelocityVerlet();
		Force f = new DampedOscillator();
		double dt = 0.0001;
		double e1 = 0;
		double e2 = 0;
		double e3 = 0;
		double t = 0;
		Pair[] previous = null;
		int times = 0;
		Area area = new Area(options.getLength(), options.getHeight(), options.getHole(), new ArrayList<>());
		List<Particle> particles = new ArrayList<>();
		particles.add(particle4);
		particles.add(particle1);
		particles.add(particle2);
		particles.add(particle3);
		//logParticles(particles);
		while(t < 5) {
			List<Particle> predicted = new ArrayList<>();
			t += dt;
			Pair p1 = f.getAnalyticalSolution(particle, t);
			particle4.setPosition(p1);
			particle1 = beeman.evolve(particle1, dt, null, f, previous, area);
			particle2 = gpc.evolve(particle2, dt, null, f, area);
			particle3 = vv.evolve(particle3, dt, null, f, area);
			predicted.add(particle1);
			predicted.add(particle2);
			predicted.add(particle3);
			predicted.add(particle4);
			e1 += Math.pow(p1.getX() - particle1.getX(), 2);
			e2 += Math.pow(p1.getX() - particle2.getX(), 2);
			e3 += Math.pow(p1.getX() - particle3.getX(), 2);
			previous = new Pair[] { particle1.getPosition(), particle1.getVelocity() };
			times++;
			if(times == 0.1/dt) {
				logParticles(predicted);
				times = 0;
				System.out.println(t);
			}
		}
		System.out.println(t);
		System.out.println(e1/(t/dt));
		System.out.println(e2/(t/dt));
		System.out.println(e3/(t/dt));
	}

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

	private static void logParticles(List<Particle> particles) {
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
		ps.println("Lattice=\"400.0 0.0 0.0 0.0 200.0 0.0 0.0 0.0 1.0\"");
		for (Particle p : particles) {
			ps.println(p.getX() + " " + p.getY() + " " + p.getVx() + " " + p.getVy());
		}

		ps.close();
	}

	private static double calculateEnergy(final List<Particle> particles, final Force force) {

		return particles.stream()
				.mapToDouble(particle -> 0.5 * (particle.getMass()) * (Math.pow(particle.getVx(), 2)
						+ Math.pow(particle.getVy(), 2)) + force.getPotentialEnergy(particle))
				.average().orElse(0);
	}

	public static Area generateParticles(Options options) {

		int i = 0;
		List<Particle> particles = new ArrayList<>();
		boolean overlapped = false;

		while(i != options.getN()) {
			double ang = rand(0, 2 * Math.PI);
			double mod = options.getVelocity();
			double vx = mod * Math.cos(ang);
			double vy = mod * Math.sin(ang);
			double x = rand(5, options.getLength() - 5);
			double y = rand(5, options.getHeight() - 5);

			Particle particle = new Particle(i, x, y, vx, vy, options.getMass());
			overlapped = false;
			for (Particle p : particles) {
				if (particle.isOverlapped(p) || particle.distance(p) < 5) {
					overlapped = true;
					break;
				}
			}
			if (!overlapped) {
				particles.add(particle);
				i++;
			}
		}

		return new Area(options.getLength(), options.getHeight(), options.getHole(), particles);
	}

	private static double rand(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

}
