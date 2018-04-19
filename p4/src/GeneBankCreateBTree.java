import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GeneBankCreateBTree {

	static int sequenceLength;
	static BTree newBTree;
	static String gbkFilename = "";
	public static void main(String[] args) {

		checkUsage(args);
		boolean useCache = (Integer.parseInt(args[0]) == 1);
		boolean useDebug;
		int degree = Integer.parseInt(args[1]);
		String fileName = args[2];
		sequenceLength = Integer.parseInt(args[3]);
		int cacheSize, debugLevel;

		if (useCache) {
			cacheSize = Integer.parseInt(args[4]);
			if (args.length == 6) {
				useDebug = true;
				debugLevel = Integer.parseInt(args[5]);
			}
		} else if (args.length == 5) {
			useDebug = true;
			debugLevel = Integer.parseInt(args[4]);
		}

		if (degree == 0) {
			degree = findOptimumDegree();
		} else if (degree == 1) {
			System.out.println("Invalid degree of 1, fix code to reject at command line");
			System.exit(1);
		}
	
		newBTree = new BTree(degree, sequenceLength, gbkFilename);

		/////////////////////////////////////////
		//GeneBank File Parsing//////////////////
		/////////////////////////////////////////
		try {
		    String currentToken = "";
			String currentSegment = "";
			String currentSubstring = "";
			GeneSequenceEncoder encoder = new GeneSequenceEncoder();
			TreeObject obj;
			Scanner scan = new Scanner(new File(fileName));
			while(scan.hasNextLine()) {		
				if(scan.nextLine().contains("ORIGIN")) {
					while(scan.hasNext()) {	
						currentToken = scan.next();
						if(!currentToken.equals("//") && !currentToken.matches(".*\\d+.*")) { //regex to check for integer
							currentSegment = currentSegment + currentToken.toLowerCase();
						}
					}
				}
				for(int i = 0; i <= (currentSegment.length() - sequenceLength); i++) {	
					currentSubstring = currentSegment.substring(i, (i + sequenceLength));
					if(!currentSubstring.contains("n")) {
						obj = new TreeObject(encoder.encode(currentSubstring));
						newBTree.insert(obj);
					}
				}
			}
		} catch(FileNotFoundException e) {
			System.out.println("Error: File not found!");
		}
	}

	private static void checkUsage(String[] args) {
		// checks for invalid numbers of arguments
		if (args.length < 4 || args.length > 6) {
			printUsage();
		}

		try {
			// checks for invalid cache option (not 0 or 1)
			if (Integer.parseInt(args[0]) < 0 || Integer.parseInt(args[0]) > 1) {
				printUsage();
			}
			// checks for invalid degree (less than 2, but not 0)
			if (Integer.parseInt(args[1]) < 2) {
				if (Integer.parseInt(args[1]) != 0) {
					printUsage();
				}
			}
			// checks that the sequence length is not less than 1 or greater than 31
			if (Integer.parseInt(args[3]) < 1 || Integer.parseInt(args[3]) > 31) {
				printUsage();
			}
			// checks if cache option is 1
			if (Integer.parseInt(args[0]) == 1) {
				// if cache option is 1, checks for valid number of arguments
				if (args.length < 5) {
					printUsage();
				}
				// checks if cache size is less than 1
				if (Integer.parseInt(args[4]) < 1) {
					printUsage();
				}
				// checks if debug level was inputed
				if (args.length == 6) {
					// checks for invalid debug level (not 0 or 1)
					if (Integer.parseInt(args[5]) < 0 || Integer.parseInt(args[5]) > 1) {
						printUsage();
					}
				}
				// else if cache option is 0, check for debug input
			} else if (args.length == 5) {
				// checks for invalid debug level (not 0 or 1)
				if (Integer.parseInt(args[4]) < 0 || Integer.parseInt(args[4]) > 1) {
					printUsage();
				}
			}
		} catch (NumberFormatException e) {
			printUsage();
		}
	}

	public static void printUsage() {
		System.out.println("Usage Error! Please use:");
		System.out.println(
				"java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
	}

	/**
	 * Estimates the optimum degree for the BTree assuming the disk block size is
	 * 4096 bytes. Taken from class notes
	 * 
	 * @return optimum degree for BTree
	 */
	static int findOptimumDegree() {
		int optimumDegree = 2;
		while ((5 + (((2 * optimumDegree) - 1) * 12) + (((2 * optimumDegree) - 1) * 4)) <= 4096) {
			optimumDegree++;
		}
		return optimumDegree;
	}

}
