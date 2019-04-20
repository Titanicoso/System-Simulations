package interfaces;

import model.Pair;
import model.Particle;

public interface Force {
    Pair getForce(Particle particle);

    Pair getD1(Particle particle);

    Pair getD2(Particle particle);

    Pair getD3(Particle particle);

    Pair getAnalyticalSolution(Particle particle, double time);
}
