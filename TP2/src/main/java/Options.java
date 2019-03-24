import java.io.File;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Options {
	
	@Option(name = "-N", usage = "Initial cells.", forbids={"-I"})
    private Integer n = 1000;
	
	@Option(name = "-S", usage = "Number of states/iterations.")
    private Integer states = 50;
	
	@Option(name = "-L", usage = "Dimension length.")
    private Integer dim = 100;
	
	@Option(name = "-3D", usage = "Enable 3D.")
    private boolean is3D = false;
	
	@Option(name = "-I", usage = "Input file.", forbids={"-N"})
    private File input;
	
	public Options(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            if (input != null && !input.isFile()) {
                throw new IllegalArgumentException("-I is no valid input file.");
            }
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

	public Integer getDim() {
		return dim;
	}

	public boolean is3D() {
		return is3D;
	}
	
	public File getInput() {
		return input;
	}

}
