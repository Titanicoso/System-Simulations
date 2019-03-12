
public class BruteForceMethod {
	
	public static void findNeighbours(final Area area) {
		for (final Particle particle1 : area.getParticles()) {
			System.out.print(particle1.getId() + ": ");
			for (final Particle particle2 : area.getParticles()) {
				if (particle1.getId() != particle2.getId()) {
					if (distance(particle1, particle2) < area.getInteractionRatio()) {
						System.out.print(particle2.getId() + " ");
					}
				}
			}
			System.out.println();
		}
	}
	
	private static double distance(final Particle particle1, final Particle particle2) {
		return Math.sqrt(
			(particle2.getY() - particle1.getY()) * (particle2.getY() - particle1.getY()) +
			(particle2.getX() - particle1.getX()) * (particle2.getX() - particle1.getX())
		);
	}
}
