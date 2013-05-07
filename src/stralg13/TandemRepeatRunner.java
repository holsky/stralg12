package stralg13;

public class TandemRepeatRunner {

	public static void main(String[] args) {
		if (args.length != 1) {
			printUsage();
		} else {
			try {
				TandemRepeatFinder.findTandemRepeatsReportOnlyCount(args[0], System.out);
			} catch (Exception e) {
				System.err.println("Could not open file " + args[0] + ", aborting.");
			}
		}

	}

	private static void printUsage() {
		System.out.println("Usage: ./tandem <file.txt>");
	}

}
