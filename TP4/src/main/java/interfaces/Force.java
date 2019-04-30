package interfaces;

import java.util.List;

import model.Area;
import model.Pair;
import model.Particle;

public interface Force {
    Pair getForce(Particle particle);

    Pair getD1(Particle particle);

    Pair getD2(Particle particle);

    Pair getD3(Particle particle);

    Pair getAnalyticalSolution(Particle particle, double time);

    boolean isVelocityDependant();

	void calculate(List<Particle> particles, Area area);

	Pair recalculateForce(Particle particle, List<Particle> particles, Area area);

    double getPotentialEnergy(Particle particle);
}
