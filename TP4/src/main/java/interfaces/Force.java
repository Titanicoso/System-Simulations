package interfaces;

import model.Pair;
import model.Particle;

import java.util.List;

public interface Force {
    Pair getForce(Particle particle, List<Particle> particles);

    Pair getD1(Particle particle, List<Particle> particles);

    Pair getD2(Particle particle, List<Particle> particles);

    Pair getD3(Particle particle, List<Particle> particles);

    Pair getAnalyticalSolution(Particle particle, double time);

    boolean isVelocityDependant();
}
