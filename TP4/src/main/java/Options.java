import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Options {
	
	@Option(name = "-N", usage = "Initial particles.")
    private Integer n = 100;
	
	@Option(name = "-L", usage = "Dimension length.")
    private Double length = 400.0;

	@Option(name = "-H", usage = "Dimension height.")
	private Double height = 200.0;
	
	@Option(name = "-DT", usage = "Integration Step.")
    private Double dt = 1.0;
	
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

	public Double getDt() {
		return dt;
	}
}
