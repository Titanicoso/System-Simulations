import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import model.Area;
import model.Collision;
import model.CollisionType;
import model.Particle;

public class Simulation {
	
	public static void simulate(Options options) {
		Area area = initSimulation(options);
		List<Particle> particles = area.getParticles();
		double length = area.getLength();
		Map<Collision, Double> collisions = new HashMap<>();
		Particle particle1, particle2;
		Double collisionTime;
		
		collisions.put(new Collision(particles.get(0), particles.get(1), CollisionType.PARTICLE_VS_PARTICLE), 1.0);
		collisions.put(new Collision(particles.get(1), particles.get(0), CollisionType.PARTICLE_VS_PARTICLE), 1.0);
		System.out.println(new Collision(particles.get(0), particles.get(1), CollisionType.PARTICLE_VS_PARTICLE).equals(new Collision(particles.get(1), particles.get(0), CollisionType.PARTICLE_VS_PARTICLE)));
		collisions.remove(new Collision(particles.get(0), particles.get(1), CollisionType.PARTICLE_VS_PARTICLE));
		
		for (int i = 0; i < particles.size(); i++) {
			particle1 = particles.get(i);
			for (int j = i + 1; j < particles.size(); j++) {
				particle2 = particles.get(j);
				collisionTime = particle1.calculateCollisionTime(particle2);
				if (collisionTime != null) {
					collisions.put(new Collision(particle1, particle2, CollisionType.PARTICLE_VS_PARTICLE), collisionTime);
				}
			}
			collisionTime = particle1.calculateCollisionTime(CollisionType.PARTICLE_VS_HWALL, length);
			if (collisionTime != null) {
				collisions.put(new Collision(particle1, null, CollisionType.PARTICLE_VS_HWALL), collisionTime);
			}
			collisionTime = particle1.calculateCollisionTime(CollisionType.PARTICLE_VS_VWALL, length);
			if (collisionTime != null) {
				collisions.put(new Collision(particle1, null, CollisionType.PARTICLE_VS_VWALL), collisionTime);
			}
		}

		boolean daBigTouchDaWall = false;
		while(!daBigTouchDaWall) {
			Entry<Collision, Double> entry = collisions.entrySet().stream().sorted(Map.Entry.comparingByValue()).findFirst().orElse(null);
			Collision collision = entry.getKey();
			double time = entry.getValue();
			particles.parallelStream().forEach(p-> p.evolvePosition(time));
			collisions.entrySet().parallelStream().forEach(c -> collisions.put(c.getKey(), c.getValue() - time));
			collision.collide();
			recalculateCollisions(particles, length, collision.getParticle1(), collisions);
			recalculateCollisions(particles, length, collision.getParticle2(), collisions);
			daBigTouchDaWall = collision.getParticle1().isBig() && collision.getParticle2() == null;
		}
		return ;
	}
	
	private static Area initSimulation(Options options) {
		List<Particle> particles = new ArrayList<>();
		particles.add(new Particle(0, options.getLength()/2, options.getLength()/2, options.getBigRadius(), 0, 0, options.getBigMass(), true));
		int i = 0;
		boolean overlapped;
		while(i < options.getN()) {
			Particle particle = new Particle(i+1, rand(options.getLittleRadius(), options.getLength()-options.getLittleRadius()),
					rand(options.getLittleRadius(), options.getLength()-options.getLittleRadius()), options.getLittleRadius(), 
					rand(-options.getVelocityRange(), options.getVelocityRange()), rand(-options.getVelocityRange(), options.getVelocityRange()),
					options.getLittleMass(), false);
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
		return new Area(options.getLength(), particles);
	}
	
	private static void recalculateCollisions(List<Particle> particles, double length, Particle particle, Map<Collision, Double> collisions) {
		if(particle == null)
			return;
		Double collisionTime;
		for (Particle p: particles) {
			if (!p.equals(particle)) {
			collisionTime = particle.calculateCollisionTime(p);
				if (collisionTime != null) {
					collisions.put(new Collision(particle, p, CollisionType.PARTICLE_VS_PARTICLE), collisionTime);
				} else {
					collisions.remove(new Collision(particle, p, CollisionType.PARTICLE_VS_PARTICLE));
				}
			}
			collisionTime = particle.calculateCollisionTime(CollisionType.PARTICLE_VS_HWALL, length);
			if (collisionTime != null) {
				collisions.put(new Collision(particle, null, CollisionType.PARTICLE_VS_HWALL), collisionTime);
			}
			collisionTime = particle.calculateCollisionTime(CollisionType.PARTICLE_VS_VWALL, length);
			if (collisionTime != null) {
				collisions.put(new Collision(particle, null, CollisionType.PARTICLE_VS_VWALL), collisionTime);
			}
		}
	}
	
	private static double rand(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

}
