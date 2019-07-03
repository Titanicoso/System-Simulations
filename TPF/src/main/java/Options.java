import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Options {
	
	@Option(name = "-N", usage = "Initial cars")
    private Integer n = 4;

	@Option(name = "-S", usage = "Number of states/iterations")
    private Integer states = 50;

	@Option(name = "-L", usage = "Dimension length")
    private Integer length = 100;

    @Option(name = "-H", usage = "Dimension height")
    private Integer height = 1;

    @Option(name = "-P", usage = "Probability")
    private Double probability = 0.5;

    @Option(name = "-V", usage = "Max Velocity")
    private Integer maxVelocity = 5;
	
	public Options(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }
    }
	
	public Integer getN() {
		return n;
	}
	
	public Integer getStates() {
		return states;
	}

    public Integer getLength() {
        return length;
    }

    public Integer getHeight() {
        return height;
    }

    public Double getProbability() {
        return probability;
    }

    public Integer getMaxVelocity() {
        return maxVelocity;
    }
}
