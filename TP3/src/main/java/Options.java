import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Options {
	
	@Option(name = "-N", usage = "Initial particles.")
    private Integer n = 10;
	
	@Option(name = "-L", usage = "Dimension length.")
    private Double length = 0.5;
	
	@Option(name = "-BM", usage = "Big particle mass.")
    private Double bigMass = 100.0;
	
	@Option(name = "-BR", usage = "Big particle radius.")
    private Double bigRadius = 0.05;
	
	@Option(name = "-LM", usage = "Little particle mass.")
    private Double littleMass = 0.1;
	
	@Option(name = "-LR", usage = "Little particle radius.")
    private Double littleRadius = 0.005;
	
	@Option(name = "-V", usage = "Velocity range.")
    private Double velocityRange = 0.1;
	
	public Options(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }
    }

	public Integer getN() {
		return n;
	}

	public Double getLength() {
		return length;
	}

	public Double getBigMass() {
		return bigMass;
	}

	public Double getBigRadius() {
		return bigRadius;
	}

	public Double getLittleMass() {
		return littleMass;
	}

	public Double getLittleRadius() {
		return littleRadius;
	}

	public Double getVelocityRange() {
		return velocityRange;
	}

}
