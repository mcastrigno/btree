
public class GeneBankSearch {


	public static void main(String[] args) {

		checkUsage(args);
		boolean useCache = (Integer.parseInt(args[0]) == 1);
		int cacheSize = 100;
		String bTreeFileName = args[1];
		String queryFileName = args[2];
		boolean useDebug;
		int debugLevel = -1;
		
		//check debug level
		if (useCache) {
			cacheSize = Integer.parseInt(args[3]);
			if (args.length == 5) {
				useDebug = true;
				debugLevel = Integer.parseInt(args[4]);
			}
		} else if (args.length == 4) {
			useDebug = true;
			debugLevel = Integer.parseInt(args[3]);
		}
		
		
	}

	private static void checkUsage(String[] args) {
		// checks for invalid numbers of arguments
		if (args.length < 3 || args.length > 5) {
			printUsage();
		}

		try {
			// checks for invalid cache option (not 0 or 1)
			if (Integer.parseInt(args[0]) < 0 || Integer.parseInt(args[0]) > 1) {
				printUsage();
			}
			// checks if cache option is 1
			if (Integer.parseInt(args[0]) == 1) {
				// if cache option is 1, checks for valid number of arguments
				if (args.length < 4) {
					printUsage();
				}
				// checks if cache size is less than 1
				if (Integer.parseInt(args[3]) < 1) {
					printUsage();
				}
				// checks if debug level was inputed
				if (args.length == 5) {
					// checks for invalid debug level (not 0)
					if (Integer.parseInt(args[4]) != 0) {
						printUsage();
					}
				}
				// else if cache option is 0, check for debug input
			} else if (args.length == 5) {
				// checks for invalid debug level (not 0)
				if (Integer.parseInt(args[3]) != 0) {
					printUsage();
				}
			}
		} catch (NumberFormatException e) {
			printUsage();
		}
	}

	public static void printUsage() {
		System.err.println("Usage Error! Please use:");
		System.err.println(
				"java GeneBankCreateBTree <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]");
		System.exit(0);
	}

}
