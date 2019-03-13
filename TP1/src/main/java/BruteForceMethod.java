import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BruteForceMethod {

	public static Map<Integer, List<Particle>> findNeighbours(final Area area) {

		final Map<Integer, List<Particle>> neighbours = new HashMap<>();
		for (int i = 0; i < area.getParticles().length; i++) {
			neighbours.put(i, new ArrayList<>());
		}

		for (final Particle particle1 : area.getParticles()) {
			for (final Particle particle2 : area.getParticles()) {
				if (particle1.getId() != particle2.getId()) {
					if (particle1.distance(particle2, area) < area.getInteractionRatio()) {
						neighbours.get(particle1.getId()).add(particle2);
					}
				}
			}
		}
		return neighbours;
	}
}
