import model.enums.Rules;

public class Main {

    public static void main(String[] args) {
    	Options options = new Options(args);
        Simulation.simulate(Rules.STANDARD, options);
    }

}
