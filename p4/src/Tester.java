import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * @author James Brooks
 *
 */
public class Tester {

	public static void main(String[] args) {
		encoderTest();
		parserTest();
	}

	public static void encoderTest() {
		GeneSequenceEncoder encoder = new GeneSequenceEncoder();
		int testCount = 0;
		int successCount = 0;
		int failureCount = 0;
		for (int i = 1; i <= 31; i++) {
			for (int j = 0; j < 1000; j++) {
				testCount++;
				String seq = randomSeqGenerator(i);
				long encodedSeq = encoder.encode(seq);
				// System.out.print("encoder test - seq length " + i + ": ");
				if (seq.equals(encoder.decode(encodedSeq))) {
					successCount++;
					// System.out.println("success");
				} else {
					failureCount++;
					// System.out.println("failure");
				}
			}
		}
		System.out.println("------------------------------------------");
		System.out.println("Sequence Encoder Tests:");
		System.out.println("1000 tests per sequence length from 1 to 31 with random sequences");
		System.out.println("Tests: " + testCount);
		System.out.println("Successes: " + successCount);
		System.out.println("Failures: " + failureCount);
		System.out.println("------------------------------------------");
	}

	public static String randomSeqGenerator(int length) {
		Random random = new Random();
		String seq = "";
		for (int i = 0; i < length; i++) {
			int nextInt = random.nextInt(4);
			if (nextInt == 0) {
				seq = seq + "a";
			} else if (nextInt == 1) {
				seq = seq + "c";
			} else if (nextInt == 2) {
				seq = seq + "g";
			} else if (nextInt == 3) {
				seq = seq + "t";
			}
		}
		return seq;
	}

	public static void parserTest() {
		// These sequences are from the test2.gbk file
		// I manually parsed them
		String firstSegment = firstParseSeg();
		String secondSegment = secondParseSeg();
		String testSegment = "";
		String testSubstring = "";
		int segmentCount = 0;

		/////////////////////////////////////////////
		// GeneBank File Parsing - Modified for Test//
		/////////////////////////////////////////////
		// This uses test2.gbk
		String fileName = "C:\\Users\\James\\git\\btree\\p4\\src\\data\\test2.gbk";
		System.out.println("------------------------------------------");
		System.out.println("Parser Tests:");
		System.out.println("1 test per sequence length from 1 to 31");
		System.out.println("Uses test2.gbk file");
		for (int sequenceLength = 1; sequenceLength  <= 31; sequenceLength++) {
			try {
				String currentToken = "";
				String currentSegment = "";
				String currentSubstring = "";
				GeneSequenceEncoder encoder = new GeneSequenceEncoder();
				TreeObject obj1, obj2;
				Scanner scan = new Scanner(new File(fileName));
				while (scan.hasNextLine()) {
					if (scan.nextLine().contains("ORIGIN")) {

						while (scan.hasNext() && !currentToken.equals("//")) {
							currentToken = scan.next();
							if (!currentToken.equals("//") && !currentToken.matches(".*\\d+.*")) { // regex to check for integer
								currentSegment = currentSegment + currentToken.toLowerCase();
							}
						}

						segmentCount++;
						if (segmentCount == 1) {
							testSegment = firstSegment;
						} else {
							testSegment = secondSegment;
						}

						if (testSegment.length() != currentSegment.length()) {
							System.out.println("Failure: Parser segment length does not match control segment length!");
							System.out.println("First Segment Length: " + firstSegment.length());
							System.out.println("Second Segment Length: " + secondSegment.length());
							System.out.println("Test Segment Length: " + testSegment.length());
							System.out.println("Current Segment Length: " + currentSegment.length());
							return;
						}

						for (int i = 0; i <= (currentSegment.length() - sequenceLength); i++) {
							currentSubstring = currentSegment.substring(i, (i + sequenceLength));
							testSubstring = testSegment.substring(i, (i + sequenceLength));
							if (!currentSubstring.contains("n") && !testSubstring.contains("n")) {
								obj1 = new TreeObject(encoder.encode(currentSubstring));
								obj2 = new TreeObject(encoder.encode(testSubstring));
								if(obj1.getData() != obj2.getData()) {
									System.out.println("Failure: Objects do not match! Sequence Length: " + sequenceLength);
								    return;
								}
							}
							if (!currentSubstring.equals(testSubstring)) {
								System.out.println("Failure: Substrings do not match! Sequence Length: " + sequenceLength);
								return;
							}
						}
					}

				}
			} catch (FileNotFoundException e) {
				System.out.println("Error: File not found!");
			}
		}
		System.out.println("Result: Success!");
		System.out.println("------------------------------------------");
	}

	public static String firstParseSeg() {
		// I manually parse this from the test2.gbk file
		return "gatcctccatatacaacggtatctccacctcaggtttagatctcaacaacggaaccattgccgacatgagacagttaggtatcgtcgag"
				+ "agttacaagctaaaacgagcagtagtcagctctgcatctgaagccgctgaagttctactaagggtggataacatcatccgtgcaagacc"
				+ "aagaaccgccaatagacaacatatgtaacatatttaggatatacctcgaaaataataaaccgccacactgtcattattataattagaaa"
				+ "cagaacgcaaaaattatccactatataattcaaagacgcgaaaaaaaaagaacaacgcgtcatagaacttttggcaattcgcgtcacaa"
				+ "ataaattttggcaacttatgtttcctcttcgagcagtactcgagccctgtctcaagaatgtaataatacccatcgtaggtatggttaaa"
				+ "gatagcatctccacaacctcaaagctccttgccgagagtcgccctcctttgtcgagtaattttcacttttcatatgagaacttattttc"
				+ "ttattctttactctcacatcctgtagtgattgacactgcaacagccaccatcactagaagaacagaacaattacttaatagaaaaatta"
				+ "tatcttcctcgaaacgatttcctgcttccaacatctacgtatatcaagaagcattcacttaccatgacacagcttcagatttcattatt"
				+ "gctgacagctactatatcactactccatctagtagtggccacgccctatgaggcatatcctatcggaaaacaataccccccagtggcaa"
				+ "gagtcaatgaatcgtttacatttcaaatttccaatgatacctataaatcgtctgtagacaagacagctcaaataacatacaattgcttc"
				+ "gacttaccgagctggctttcgtttgactctagttctagaacgttctcaggtgaaccttcttctgacttactatctgatgcgaacaccac"
				+ "gttgtatttcaatgtaatactcgagggtacggactctgccgacagcacgtctttgaacaatacataccaatttgttgttacaaaccgtc"
				+ "catccatctcgctatcgtcagatttcaatctattggcgttgttaaaaaactatggttatactaacggcaaaaacgctctgaaactagat"
				+ "cctaatgaagtcttcaacgtgacttttgaccgttcaatgttcactaacgaagaatccattgtgtcgtattacggacgttctcagttgta"
				+ "taatgcgccgttacccaattggctgttcttcgattctggcgagttgaagtttactgggacggcaccggtgataaactcggcgattgctc"
				+ "cagaaacaagctacagttttgtcatcatcgctacagacattgaaggattttctgccgttgaggtagaattcgaattagtcatcggggct"
				+ "caccagttaactacctctattcaaaatagtttgataatcaacgttactgacacaggtaacgtttcatatgacttacctctaaactatgt"
				+ "ttatctcgatgacgatcctatttcttctgataaattgggttctataaacttattggatgctccagactgggtggcattagataatgcta"
				+ "ccatttccgggtctgtcccagatgaattactcggtaagaactccaatcctgccaatttttctgtgtccatttatgatacttatggtgat"
				+ "gtgatttatttcaacttcgaagttgtctccacaacggatttgtttgccattagttctcttcccaatattaacgctacaaggggtgaatg"
				+ "gttctcctactattttttgccttctcagtttacagactacgtgaatacaaacgtttcattagagtttactaattcaagccaagaccatg"
				+ "actgggtgaaattccaatcatctaatttaacattagctggagaagtgcccaagaatttcgacaagctttcattaggtttgaaagcgaac"
				+ "caaggttcacaatctcaagagctatattttaacatcattggcatggattcaaagataactcactcaaaccacagtgcgaatgcaacgtc"
				+ "cacaagaagttctcaccactccacctcaacaagttcttacacatcttctacttacactgcaaaaatttcttctacctccgctgctgcta"
				+ "cttcttctgctccagcagcgctgccagcagccaataaaacttcatctcacaataaaaaagcagtagcaattgcgtgcggtgttgctatc"
				+ "ccattaggcgttatcctagtagctctcatttgcttcctaatattctggagacgcagaagggaaaatccagacgatgaaaacttaccgca"
				+ "tgctattagtggacctgatttgaataatcctgcaaataaaccaaatcaagaaaacgctacacctttgaacaacccctttgatgatgatg"
				+ "cttcctcgtacgatgatacttcaatagcaagaagattggctgctttgaacactttgaaattggataaccactctgccactgaatctgat"
				+ "atttccagcgtggatgaaaagagagattctctatcaggtatgaatacatacaatgatcagttccaatcccaaagtaaagaagaattatt"
				+ "agcaaaacccccagtacagcctccagagagcccgttctttgacccacagaataggtcttcttctgtgtatatggatagtgaaccagcag"
				+ "taaataaatcctggcgatatactggcaacctgtcaccagtctctgatattgtcagagacagttacggatcacaaaaaactgttgataca"
				+ "gaaaaacttttcgatttagaagcaccagagaaggaaaaacgtacgtcaagggatgtcactatgtcttcactggacccttggaacagcaa"
				+ "tattagcccttctcccgtaagaaaatcagtaacaccatcaccatataacgtaacgaagcatcgtaaccgccacttacaaaatattcaag"
				+ "actctcaaagcggtaaaaacggaatcactcccacaacaatgtcaacttcatcttctgacgattttgttccggttaaagatggtgaaaat"
				+ "ttttgctgggtccatagcatggaaccagacagaagaccaagtaagaaaaggttagtagatttttcaaataagagtaatgtcaatgttgg"
				+ "tcaagttaaggacattcacggacgcatcccagaaatgctgtgattatacgcaacgatattttgcttaattttattttcctgttttattt"
				+ "tttattagtggtttacagataccctatattttatttagtttttatacttagagacatttaattttaattccattcttcaaatttcattt"
				+ "ttgcacttaaaacaaagatccaaaaatgctctcgccctcttcatattgagaatacactccattcaaaattttgtcgtcaccgctgatta"
				+ "atttttcactaaactgatgaataatcaaaggccccacgtcagaaccgactaaagaagtgagttttattttaggaggttgaaaaccatta"
				+ "ttgtctggtaaattttcatcttcttgacatttaacccagtttgaatccctttcaatttctgctttttcctccaaactatcgaccctcct"
				+ "gtttctgtccaacttatgtcctagttccaattcgatcgcattaataactgcttcaaatgttattgtgtcatcgttgactttaggtaatt"
				+ "tctccaaatgcataatcaaactatttaaggaagatcggaattcgtcgaacacttcagtttccgtaatgatctgatcgtctttatccaca"
				+ "tgttgtaattcactaaaatctaaaacgtatttttcaatgcataaatcgttctttttattaataatgcagatggaaaatctgtaaacgtg"
				+ "cgttaatttagaaagaacatccagtataagttcttctatatagtcaattaaagcaggatgcctattaatgggaacgaactgcggcaagt"
				+ "tgaatgactggtaagtagtgtagtcgaatgactgaggtgggtatacatttctataaaataaaatcaaattaatgtagcattttaagtat"
				+ "accctcagccacttctctacccatctattcataaagctgacgcaacgattactattttttttttcttcttggatctcagtcgtcgcaaa"
				+ "aacgtataccttctttttccgaccttttttttagctttctggaaaagtttatattagttaaacagggtctagtcttagtgtgaaagcta"
				+ "gtggtttcgattgactgatattaagaaagtggaaattaaattagtagtgtagacgtatatgcatatgtatttctcgcctgtttatgttt"
				+ "ctacgtacttttgatttatagcaaggggaaaagaaatacatactattttttggtaaaggtgaaagcataatgtaaaagctagaataaaa"
				+ "tggacgaaataaagagaggcttagttcatcttttttccaaaaagcacccaatgataataactaaaatgaaaaggatttgccatctgtca"
				+ "gcaacatcagttgtgtgagcaataataaaatcatcacctccgttgcctttagcgcgtttgtcgtttgtatcttccgtaattttagtctt"
				+ "atcaatgggaatcataaattttccaatgaattagcaatttcgtccaattctttttgagcttcttcatatttgctttggaattcttcgca"
				+ "cttcttttcccattcatctctttcttcttccaaagcaacgatccttctacccatttgctcagagttcaaatcggcctctttcagtttat"
				+ "ccattgcttccttcagtttggcttcactgtcttctagctgttgttctagatcctggtttttcttggtgtagttctcattattagatctc"
				+ "aagttattggagtcttcagccaattgctttgtatcagacaattgactctctaacttctccacttcactgtcgagttgctcgtttttagc"
				+ "ggacaaagatttaatctcgttttctttttcagtgttagattgctctaattctttgagctgttctctcagctcctcatatttttcttgcc"
				+ "atgactcagattctaattttaagctattcaatttctctttgatc";
	}

	public static String secondParseSeg() {
		// I manually parse this from the test2.gbk file
		return "gatcctccatatacaacggtatctccacctcaggtttagatctcaacaacggaaccattgccgacatgagacagttaggtatcgtcgag"
				+ "agttacaagctaaaacgagcagtagtcagctctgcatctgaagccgctgaagttctactaagggtggataacatcatccgtgcaagacc"
				+ "aagaaccgccaatagacaacatatgtaacatatttaggatatacctcgaaaataataaaccgccacactgtcattattataattagaaa"
				+ "cagaacgcaaaaattatccactatataattcaaagacgcgaaaaaaaaagaacaacgcgtcatagaacttttggcaattcgcgtcacaa"
				+ "ataaattttggcaacttatgtttcctcttcgagcagtactcgagccctgtctcaagaatgtaataatacccatcgtaggtatggttaaa"
				+ "gatagcatctccacaacctcaaagctccttgccgagagtcgccctcctttgtcgagtaattttcacttttcatatgagaacttattttc"
				+ "ttattctttactctcacatcctgtagtgattgacactgcaacagccaccatcactagaagaacagaacaattacttaatagaaaaatta"
				+ "tatcttcctcgaaacgatttcctgcttccaacatctacgtatatcaagaagcattcacttaccatgacacagcttcagatttcattatt"
				+ "gctgacagctactatatcactactccatctagtagtggccacgccctatgaggcatatcctatcggaaaacaataccccccagtggcaa"
				+ "gagtcaatgaatcgtttacatttcaaatttccaatgatacctataaatcgtctgtagacaagacagctcaaataacatacaattgcttc"
				+ "gacttaccgagctggctttcgtttgactctagttctagaacgttctcaggtgaaccttcttctgacttactatctgatgcgaacaccac"
				+ "gttgtatttcaatgtaatactcgagggtacggactctgccgacagcacgtctttgaacaatacataccaatttgttgttacaaaccgtc"
				+ "catccatctcgctatcgtcagatttcaatctattggcgttgttaaaaaactatggttatactaacggcaaaaacgctctgaaactagat"
				+ "cctaatgaagtcttcaacgtgacttttgaccgttcaatgttcactaacgaagaatccattgtgtcgtattacggacgttctcagttgta"
				+ "taatgcgccgttacccaattggctgttcttcgattctggcgagttgaagtttactgggacggcaccggtgataaactcggcgattgctc"
				+ "cagaaacaagctacagttttgtcatcatcgctacagacattgaaggattttctgccgttgaggtagaattcgaattagtcatcggggct"
				+ "caccagttaactacctctattcaaaatagtttgataatcaacgttactgacacaggtaacgtttcatatgacttacctctaaactatgt"
				+ "ttatctcgatgacgatcctatttcttctgataaattgggttctataaacttattggatgctccagactgggtggcattagataatgcta"
				+ "ccatttccgggtctgtcccagatgaattactcggtaagaactccaatcctgccaatttttctgtgtccatttatgatacttatggtgat"
				+ "gtgatttatttcaacttcgaagttgtctccacaacggatttgtttgccattagttctcttcccaatattaacgctacaaggggtgaatg"
				+ "gttctcctactattttttgccttctcagtttacagactacgtgaatacaaacgtttcattagagtttactaattcaagccaagaccatg"
				+ "actgggtgaaattccaatcatctaatttaacattagctggagaagtgcccaagaatttcgacaagctttcattaggtttgaaagcgaac"
				+ "caaggttcacaatctcaagagctatattttaacatcattggcatggattcaaagataactcactcaaaccacagtgcgaatgcaacgtc"
				+ "cacaagaagttctcaccactccacctcaacaagttcttacacatcttctacttacactgcaaaaatttcttctacctccgctgctgcta"
				+ "cttcttctgctccagcagcgctgccagcagccaataaaacttcatctcacaataaaaaagcagtagcaattgcgtgcggtgttgctatc"
				+ "ccattaggcgttatcctagtagctctcatttgcttcctaatattctggagacgcagaagggaaaatccagacgatgaaaacttaccgca"
				+ "tgctattagtggacctgatttgaataatcctgcaaataaaccaaatcaagaaaacgctacacctttgaacaacccctttgatgatgatg"
				+ "cttcctcgtacgatgatacttcaatagcaagaagattggctgctttgaacactttgaaattggataaccactctgccactgaatctgat"
				+ "atttccagcgtggatgaaaagagagattctctatcaggtatgaatacatacaatgatcagttccaatcccaaagtaaagaagaattatt"
				+ "agcaaaacccccagtacagcctccagagagcccgttctttgacccacagaataggtcttcttctgtgtatatggatagtgaaccagcag"
				+ "taaataaatcctggcgatatactggcaacctgtcaccagtctctgatattgtcagagacagttacggatcacaaaaaactgttgataca"
				+ "gaaaaacttttcgatttagaagcaccagagaaggaaaaacgtacgtcaagggatgtcactatgtcttcactggacccttggaacagcaa"
				+ "tattagcccttctcccgtaagaaaatcagtaacaccatcaccatataacgtaacgaagcatcgtaaccgccacttacaaaatattcaag"
				+ "actctcaaagcggtaaaaacggaatcactcccacaacaatgtcaacttcatcttctgacgattttgttccggttaaagatggtgaaaat"
				+ "ttttgctgggtccatagcatggaaccagacagaagaccaagtaagaaaaggttagtagatttttcaaataagagtaatgtcaatgttgg"
				+ "tcaagttaaggacattcacggacgcatcccagaaatgctgtgattatacgcaacgatattttgcttaattttattttcctgttttattt"
				+ "tttattagtggtttacagataccctatattttatttagtttttatacttagagacatttaattttaattccattcttcaaatttcattt"
				+ "ttgcacttaaaacaaagatccaaaaatgctctcgccctcttcatattgagaatacactccattcaaaattttgtcgtcaccgctgatta"
				+ "atttttcactaaactgatgaataatcaaaggccccacgtcagaaccgactaaagaagtgagttttattttaggaggttgaaaaccatta"
				+ "ttgtctggtaaattttcatcttcttgacatttaacccagtttgaatccctttcaatttctgctttttcctccaaactatcgaccctcct"
				+ "gtttctgtccaacttatgtcctagttccaattcgatcgcattaataactgcttcaaatgttattgtgtcatcgttgactttaggtaatt"
				+ "tctccaaatgcataatcaaactatttaaggaagatcggaattcgtcgaacacttcagtttccgtaatgatctgatcgtctttatccaca"
				+ "tgttgtaattcactaaaatctaaaacgtatttttcaatgcataaatcgttctttttattaataatgcagatggaaaatctgtaaacgtg"
				+ "cgttaatttagaaagaacatccagtataagttcttctatatagtcaattaaagcaggatgcctattaatgggaacgaactgcggcaagt"
				+ "tgaatgactggtaagtagtgtagtcgaatgactgaggtgggtatacatttctataaaataaaatcaaattaatgtagcattttaagtat"
				+ "accctcagccacttctctacccatctattcataaagctgacgcaacgattactattttttttttcttcttggatctcagtcgtcgcaaa"
				+ "aacgtataccttctttttccgaccttttttttagctttctggaaaagtttatattagttaaacagggtctagtcttagtgtgaaagcta"
				+ "gtggtttcgattgactgatattaagaaagtggaaattaaattagtagtgtagacgtatatgcatatgtatttctcgcctgtttatgttt"
				+ "ctacgtacttttgatttatagcaaggggaaaagaaatacatactattttttggtaaaggtgaaagcataatgtaaaagctagaataaaa"
				+ "tggacgaaataaagagaggcttagttcatcttttttccaaaaagcacccaatgataataactaaaatgaaaaggatttgccatctgtca"
				+ "gcaacatcagttgtgtgagcaataataaaatcatcacctccgttgcctttagcgcgtttgtcgtttgtatcttccgtaattttagtctt"
				+ "atcaatgggaatcataaattttccaatgaattagcaatttcgtccaattctttttgagcttcttcatatttgctttggaattcttcgca"
				+ "cttcttttcccattcatctctttcttcttccaaagcaacgatccttctacccatttgctcagagttcaaatcggcctctttcagtttat"
				+ "ccattgcttccttcagtttggcttcactgtcttctagctgttgttctagatcctggtttttcttggtgtagttctcattattagatctc"
				+ "aagttattggagtcttcagccaattgctttgtatcagacaattgactctctaacttctccacttcactgtcgagttgctcgtttttagc"
				+ "ggacaaagatttaatctcgttttctttttcagtgttagattgctctaattctttgagctgttctctcagctcctcatatttttcttgcc"
				+ "atgactcagattctaattttaagctattcaatttctctttgatc";
	}

}