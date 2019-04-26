import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;

import forces.DampedOscillator;
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
		Particle particle = new Particle(0, 1.0, 0.0, -100.0/170, 0, 70);
		Particle particle1 = new Particle(0, 1.0, 0.0, -100.0/170, 0, 70);
		Particle particle2 = new Particle(0, 1.0, 0.0, -100.0/170, 0, 70);
		Particle particle3 = new Particle(0, 1.0, 0.0, -100.0/170, 0, 70);
		Beeman beeman = new Beeman();
		GearPredictorCorrector gpc = new GearPredictorCorrector();
		VelocityVerlet vv = new VelocityVerlet();
		Force f = new DampedOscillator();
		double dt = 0.000001;
		double e1 = 0;
		double e2 = 0;
		double e3 = 0;
		double t = 0;
		while(t < 5) {
			t += dt;
			Pair p1 = f.getAnalyticalSolution(particle, t);
			Pair p2 = beeman.evolve(particle1, dt, null, f, null).getPosition();
			Pair p3 = gpc.evolve(particle2, dt, null, f).getPosition();
			Pair p4 = vv.evolve(particle3, dt, null, f).getPosition();
			e1 += Math.pow(p1.getX() - p2.getX(), 2);
			e2 += Math.pow(p1.getX() - p3.getX(), 2);
			e3 += Math.pow(p1.getX() - p4.getX(), 2);
		}
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
		for (Particle p : particles) {
			ps.println(p.getX() + " " + p.getY() + " " + p.getVx() + " " + p.getVy());
		}

		ps.close();
	}
	
	private static double rand(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

}
