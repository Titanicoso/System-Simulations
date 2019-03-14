import java.io.File;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Options {
	
	@Option(name="-N", usage="Number of particles.")  
	private int N = 20;
	
	@Option(name="-L", usage="Area length.")  
	private double L = 20.0;
	
	@Option(name="-R", usage="Interaction ratio.")
	private double RC = 1.0;
	
	@Option(name="-M", usage="Number of cells per row/column.")
	private int M = 3;
	
	@Option(name="-P", usage="Periodic outline.")
	private boolean PERIODIC = false;
	
	@Option(name = "-I", usage = "Input file.")
    private File input;

	public Options(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            if (input != null && !input.isFile()) {
                throw new IllegalArgumentException("-i is no valid input file.");
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

	public int getN() {
		return N;
	}

	public double getL() {
		return L;
	}

	public double getRc() {
		return RC;
	}

	public int getM() {
		return M;
	}

	public boolean isPeriodic() {
		return PERIODIC;
	}
	
	 public File getInput() {
		return input;
	}
    
}