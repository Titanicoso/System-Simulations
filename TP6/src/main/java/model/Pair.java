package model;

public class Pair {

    private double x;
    private double y;

    public Pair(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Pair(Pair other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Pair sum(Pair other) {
        x += other.x;
        y += other.y;
        return this;
    }

    public Pair sum(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Pair substract(Pair other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    public Pair multiplyByScalar(double scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public double distance(Pair other) {
        return Math.hypot(other.getX() - x, other.getY() - y);
    }

	@Override
	public String toString() {
		return "Pair [x=" + x + ", y=" + y + "]";
	}
}
