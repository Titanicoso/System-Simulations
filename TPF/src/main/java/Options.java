import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;

public class Options {
	
	@Option(name = "-N", usage = "Initial cars")
    private Integer n = 50;

	@Option(name = "-S", usage = "Number of states/iterations")
    private Integer states = 500;

	@Option(name = "-L", usage = "Dimension length")
    private Integer length = 100;

    @Option(name = "-H", usage = "Dimension height")
    private Integer height = 3;

    @Option(name = "-P", usage = "Slow down probability")
    private Double slowDownProbability = 0.15;

    @Option(name = "-LP", usage = "Lane change probability")
    private Double laneChangeProbability = 0.85;

    @Option(name = "-V", usage = "Max Velocity")
    private Integer maxVelocity = 5;

    @Option(name = "-I", usage = "Input file.")
    private File input = new File("input.txt");
	
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

    public Double getSlowDownProbability() {
        return slowDownProbability;
    }

    public Double getLaneChangeProbability() {
        return laneChangeProbability;
    }

    public Integer getMaxVelocity() {
        return maxVelocity;
    }

    public File getInput() {
        return input;
    }
}
