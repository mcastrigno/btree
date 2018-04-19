import java.util.Random;

/**
 *  
 * @author James Brooks
 *
 */
public class Tester {

	public static void main(String[] args) {
		encoderTest();
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
				//System.out.print("encoder test - seq length " + i + ": ");
				if (seq.equals(encoder.decode(encodedSeq))) {
					successCount++;
					//System.out.println("success");
				} else {
					failureCount++;
					//System.out.println("failure");
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
		for(int i = 0; i < length; i++) {
			int nextInt = random.nextInt(4);
			if(nextInt == 0) {
				seq = seq + "a";
			}
			else if(nextInt == 1) {
				seq = seq + "c";
			}
			else if(nextInt == 2) {
				seq = seq + "g";
			}
			else if(nextInt == 3) {
				seq = seq + "t";
			}
		}
		return seq;
	}
}