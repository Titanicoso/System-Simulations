import model.Cell;
import model.State;
import model.enums.Rules;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    public static void simulate(int states, int dim, Rules rule, boolean is3D) {

        List<Cell> modified = new ArrayList<>();

        State state = new State(dim, is3D);

        int i = 0;
        while (i < states) {
            List<Cell> lastModified = state.getModified();

            for (Cell cell: lastModified) {
                List<Cell> neighbours = rule.getNeighbours(state, cell);
                for (Cell neighbour : neighbours) {
                    if(rule.apply(state, neighbour)) {
                        modified.add(neighbour);
                    }
                }
            }
            //logState(state);
            state.changeState(modified);
            i++;
        }
    }
}
