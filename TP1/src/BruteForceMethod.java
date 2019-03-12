
public class BruteForceMethod {
	
	public static void findNeighbours(final Area area, final boolean cont) {
		for (final Particle particle1 : area.getParticles()) {
			System.out.print(particle1.getId() + ": ");
			for (final Particle particle2 : area.getParticles()) {
				if (particle1.getId() != particle2.getId()) {
					if (particle1.distance(particle2, area, cont) < area.getInteractionRatio()) {
						System.out.print(particle2.getId() + " ");
					}
				}
			}
			System.out.println();
		}
	}
}
