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
import model.Particle;

public class Simulation {

	static boolean append = false;
	
	public static void simulate(Options options) {
		Particle particle = new Particle(0, 1.0, 0.0, -100.0/170, 0, 70);
		Beeman beeman = new Beeman();
		GearPredictorCorrector gpc = new GearPredictorCorrector();
		VelocityVerlet vv = new VelocityVerlet();
		Force f = new DampedOscillator();
		double dt = 0.000001;
		System.out.println(f.getAnalyticalSolution(particle, dt));
		System.out.println(beeman.evolve(particle, dt, null, f, null));
		System.out.println(gpc.evolve(particle, dt, null, f));
		System.out.println(vv.evolve(particle, dt, null, f));
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
