package model;

import java.util.Objects;

public class Cell {

    private int x;
    private int y;
    private int z;
    private boolean alive;


    public Cell(int x, int y, int z, boolean alive) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.alive = alive;
    }

    public Cell(int x, int y, boolean alive) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.alive = alive;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public boolean isAlive() {
        return alive;
    }

    public void changeStatus() {
        alive = !alive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x &&
                y == cell.y &&
                z == cell.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
