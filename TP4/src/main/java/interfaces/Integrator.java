package interfaces;

import model.Area;
import model.Particle;

import java.util.List;

public interface Integrator {
    Particle evolve(Particle particle, double dt,
                    List<Particle> particles, Force force, Area area);
}
