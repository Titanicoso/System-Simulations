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

	public static void simulate(Options options) {
		GearPredictorCorrector gpc = new GearPredictorCorrector();
		Force f = new LennardJonesGas();
		double dt = 0.0001;
		double t = 0;
		int times = 0;
		double fraction = 1;
		Area area = generateParticles(options);
		logParticles(area.getParticles());
		while(fraction != 0.5) {
			f.calculate(area.getParticles(), area);
			int leftParticles = 0;
			for (Particle p: area.getParticles()) {
				gpc.evolve(p, dt, area.getParticles(), f, area);
				if(area.leftBox(p)) {
					leftParticles++;
				}
			}
			fraction = (float)leftParticles/area.getParticles().size();
			times++;
			if(times == 1/dt) {
				logParticles(area.getParticles());
				times = 0;
			}
		}
		System.out.println(t);
	}
	
	public static void simulate1(Options options) {
		Particle particle = new Particle(0, 1.0, 0.0, -10.0/14, 0, 70);
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
		Area area = new Area(options.getLength(), options.getHeight(), options.getHole(), new ArrayList<>());
		while(t < 5) {
			t += dt;
			Pair p1 = f.getAnalyticalSolution(particle, t);
			Pair p2 = beeman.evolve(particle1, dt, null, f, previous, area).getPosition();
			Pair p3 = gpc.evolve(particle2, dt, null, f, area).getPosition();
			Pair p4 = vv.evolve(particle3, dt, null, f, area).getPosition();
			e1 += Math.pow(p1.getX() - p2.getX(), 2);
			e2 += Math.pow(p1.getX() - p3.getX(), 2);
			e3 += Math.pow(p1.getX() - p4.getX(), 2);
			previous = new Pair[] { p2, particle1.getVelocity() };
		}
		System.out.println(t);
		System.out.println(e1/(t/dt));
		System.out.println(e2/(t/dt));
		System.out.println(e3/(t/dt));
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
		ps.println();
		for (Particle p : particles) {
			ps.println(p.getX() + " " + p.getY() + " " + p.getVx() + " " + p.getVy());
		}

		ps.close();
	}

	public static Area generateParticles(Options options) {

		int i = 0;
		List<Particle> particles = new ArrayList<>();
		boolean overlapped = false;

		while(i != options.getN()) {
			double ang = rand(0, 2 * Math.PI);
			double mod = rand(0, options.getVelocity());
			double vx = mod * Math.cos(ang);
			double vy = mod * Math.sin(ang);
			double x = rand(0, options.getLength());
			double y = rand(0, options.getHeight());

			Particle particle = new Particle(i, x, y, vx, vy, options.getMass());
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

		return new Area(options.getLength(), options.getHeight(), options.getHole(), particles);
	}

	private static double rand(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

}
