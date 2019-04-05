import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

import model.Area;
import model.Collision;
import model.CollisionType;
import model.Particle;

public class Simulation {
	
	public static void simulate(int n, double length, double bigMass, double bigRadius,
			double littleMass, double littleRadius, double velocityRange) {
		Area area = initSimulation(n, length, bigMass, bigRadius, littleMass, littleRadius, velocityRange);
		List<Particle> particles = area.getParticles();
		Set<Collision> collisions = new TreeSet<>();
		Particle particle1, particle2;
		Double collisionTime;
		double time = 0;
		
		for (int i = 0; i < n; i++) {
			particle1 = particles.get(i);
			for (int j = i + 1; j < n; j++) {
				particle2 = particles.get(j);
				collisionTime = particle1.calculateCollisionTime(particle2);
				if (collisionTime != null) {
					collisions.add(new Collision(particle1, particle2, CollisionType.PARTICLE_VS_PARTICLE, collisionTime));
				}
			}
			collisionTime = particle1.calculateCollisionTime(CollisionType.PARTICLE_VS_HWALL, length);
			collisions.add(new Collision(particle1, null, CollisionType.PARTICLE_VS_HWALL, collisionTime));
			
			collisionTime = particle1.calculateCollisionTime(CollisionType.PARTICLE_VS_VWALL, length);
			collisions.add(new Collision(particle1, null, CollisionType.PARTICLE_VS_VWALL, collisionTime));
		}
		
		
		while(time < 1000) {
			
		}
	}
	
	private static Area initSimulation(int n, double length, double bigMass, double bigRadius,
			double littleMass, double littleRadius, double velocityRange) {
		List<Particle> particles = new ArrayList<>();
		particles.add(new Particle(0, length/2, length/2, bigRadius, 0, 0, bigMass, true));
		int i = 0;
		boolean overlapped;
		while(i < n) {
			Particle particle = new Particle(i+1, rand(littleRadius, length-littleRadius),
					rand(littleRadius, length-littleRadius), littleRadius, 
					rand(-velocityRange, velocityRange), rand(-velocityRange, velocityRange),
					littleMass, false);
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
		return new Area(length, particles);
	}
	
	private static double rand(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

}
