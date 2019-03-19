package model;

import java.util.ArrayList;
import java.util.List;

public class State {

    private List<Cell> modified;
    private List<Cell> checked;
    private Cell[][][] cells;
    private int dim;
    private boolean is3D;


    public State(int dim, boolean is3D) {
        this.modified = new ArrayList<>();
        this.checked = new ArrayList<>();
        this.cells = new Cell[dim][dim][dim];
        this.dim = dim;
        this.is3D = is3D;

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if(is3D) {
                    for (int k = 0; k < dim; k++) {
                        this.cells[i][j][k] = new Cell(i, j, k, false);
                    }
                }
                this.cells[i][j][0] = new Cell(i, j, 0, false);
            }
        }
    }

    public List<Cell> getModified() {
        return modified;
    }

    public Cell[][][] getCells() {
        return cells;
    }

    public Cell getCell(int x, int y, int z) {
        return cells[x][y][z];
    }

    public boolean isChecked(Cell cell) {
        return checked.contains(cell);
    }

    public void setChecked(Cell cell) {
        checked.add(cell);
    }

    public List<Cell> getMooreNeighbours(Cell cell, int radius) {

        List<Cell> neighbours = new ArrayList<>();
        Cell neighbour;

        for (int i = -radius; i < radius; i++) {
            for (int j = -radius; j < radius; j++) {
                if(is3D) {
                    for (int k = -radius; k < radius; k++) {
                        neighbour = getCell(i, j, k);
                        neighbours.add(neighbour);
                    }
                } else {
                    neighbour = getCell(i, j, 0);
                    neighbours.add(neighbour);
                }
            }
        }

        return neighbours;
    }

    public List<Cell> getVonNeumannNeighbours(Cell cell, int radius) {

        List<Cell> neighbours = new ArrayList<>();
        Cell neighbour;

        for (int i = -radius; i < radius; i++) {
            for (int j = -radius; j < radius; j++) {
                if(is3D) {
                    for (int k = -radius; k < radius; k++) {
                        if(Math.abs(i)+ Math.abs(j) + Math.abs(k) <= radius) {
                            neighbour = getCell(i, j, k);
                            neighbours.add(neighbour);
                        }
                    }
                } else if (Math.abs(i) + Math.abs(j) <= radius) {
                    neighbour = getCell(i, j, 0);
                    neighbours.add(neighbour);
                }
            }
        }

        return neighbours;
    }

    public void changeState(List<Cell> modified) {
        for (Cell cell: modified) {
            int x = cell.getX();
            int y = cell.getY();
            int z = cell.getZ();

            this.cells[x][y][z].changeStatus();
        }

        this.modified = new ArrayList<>(modified);
        this.checked.clear();
    }
}
