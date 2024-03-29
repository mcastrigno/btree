import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class GeneBankCreateBTree {

	static int sequenceLength;
	static BTree newBTree;
	static String gbkFilename = "";

	public static void main(String[] args) {

		checkUsage(args);
		boolean useCache = (Integer.parseInt(args[0]) == 1);
		int cacheSize = 100;
		int degree = Integer.parseInt(args[1]);
		String fileName = args[2];
		sequenceLength = Integer.parseInt(args[3]);
		boolean useDebug;
		int debugLevel = 0;

		// for cache time improvement testing
		long startTime, endTime;

		// check debug level
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
			//Diagnostic Message
			if(debugLevel == 0) {
				System.err.println("Optimum degree found: " + degree);
			}
		} else if (degree == 1) {
			System.err.println("Invalid degree of 1, fix code to reject at command line");
			System.exit(1);
		}

		startTime = System.currentTimeMillis();
		newBTree = new BTree(degree, sequenceLength, fileName, useCache, cacheSize);

		// Diagnostic Messages
		if (debugLevel == 0) {
			System.err.println("Creating BTree...");
			System.err.println("Beginning parsing sequences of length " + sequenceLength);
		}

		/////////////////////////////////////////
		// GeneBank File Parsing/////////////////
		/////////////////////////////////////////
		try {
			String currentToken, currentSegment, currentSubstring;
			GeneSequenceEncoder encoder = new GeneSequenceEncoder();
			TreeObject obj;
			Scanner scan = new Scanner(new File(fileName));
			while (scan.hasNextLine()) {
				if (scan.nextLine().contains("ORIGIN")) {
					currentToken = "";
					currentSegment = "";
					while (scan.hasNext() && !currentToken.equals("//")) {
						currentToken = scan.next();
						if (!currentToken.equals("//") && !currentToken.matches(".*\\d+.*")) { // regex to check for
																								// integer
							currentSegment = currentSegment + currentToken.toLowerCase();
						}
					}
					for (int i = 0; i <= (currentSegment.length() - sequenceLength); i++) {
						currentSubstring = currentSegment.substring(i, (i + sequenceLength));
						if (!currentSubstring.contains("n")) {
							obj = new TreeObject(encoder.encode(currentSubstring));
							newBTree.insert(obj);

							// Diagnostic Messages
							if (debugLevel == 0) {
								System.err.println("Inserting " + currentSubstring);
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Error: File not found!");
		}
		newBTree.rootWrite();

		endTime = System.currentTimeMillis();

		// Diagnostic Messages
		if (debugLevel == 0) {
			System.err.println("BTree creation complete.");
			System.err.println("Time to complete: " + (endTime - startTime) + " ms");
		}
		if (debugLevel == 1) {
			PrintWriter writer;
			try {
				writer = new PrintWriter("dump");
				newBTree.rootRead();
				System.out.println(newBTree.dnaDump());
				writer.println(newBTree.dnaDump());
				writer.close();
			} catch (FileNotFoundException e) {
				System.err.println("Error 11: File not found!");
				System.exit(0);
			}
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
		System.err.println("Usage Error! Please use:");
		System.err.println(
				"java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
		System.exit(0);
	}

	/**
	 * Estimates the optimum degree for the BTree assuming the disk block size is
	 * 4096 bytes. Taken from class notes
	 * 
	 * @return optimum degree for BTree
	 */
	static int findOptimumDegree() {
		int optimumDegree = 2;
		while ((16 + (((2 * optimumDegree) - 1) * 12) + ((2 * optimumDegree)  * 4)) <= 4096) {
			optimumDegree++;
		}
		return optimumDegree - 1;
	}

}
