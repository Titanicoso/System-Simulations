import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Options {
	
	@Option(name = "-N", usage = "Initial particles.")
    private Integer n = 400;
	
	@Option(name = "-L", usage = "Box length.")
    private Double length = 0.3;

	private Double extraSpace = 1.0/10;

	@Option(name = "-H", usage = "Box height.")
	private Double height = 1.0 + extraSpace;

	@Option(name = "-V", usage = "Velocity module.")
	private Double velocity = 0.0;

	@Option(name = "-HO", usage = "Hole size.")
	private Double hole = 0.0;
	
	@Option(name = "-M", usage = "Particle mass.")
	private Double mass = 0.01;
	
	@Option(name = "-DT", usage = "Integration Step.")
    private Double dt = 1.0;

	@Option(name = "-MiR", usage = "Min radius.")
	private Double minRadius = 0.01;

	@Option(name = "-MaR", usage = "Max radius.")
	private Double maxRadius = 0.015;
	
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

	public Double getHeight() {
		return height;
	}

	public Double getMass() {
		return mass;
	}

	public Double getDt() {
		return dt;
	}

	public Double getVelocity() {
		return velocity;
	}

	public Double getHole() {
		return hole;
	}

	public Double getMinRadius() {
		return minRadius;
	}

	public Double getMaxRadius() {
		return maxRadius;
	}

	public Double getExtraSpace() {
		return extraSpace;
	}
}
