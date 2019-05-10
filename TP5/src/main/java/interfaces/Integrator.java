package interfaces;

import model.Area;
import model.Particle;

import java.util.List;
import java.util.Set;

public interface Integrator {
    Particle evolve(Particle particle, double dt,
                    List<Particle> particles, Set<Force> forces, Area area);
}
