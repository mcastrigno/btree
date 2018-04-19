
/**
 * I added this class just to do
 * random testing. I didn't want to add to 
 * TestBTree class in case that was for more 
 * BTree specific tests.
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
		String seq1 = "aaaaaaaattccggttggggaaaattgtgtt";
		String seq2 = "tccggtgaaaaagggggggcttttgaaaaaa";
		String seq3 = "cccggtgaaaaagggggggcttttgaaaaaa";
		String seq4 = "gccggtgaaaaagggggggcttttgaaaaaa";
		
		long encodedSeq1 = encoder.encode(seq1);
		long encodedSeq2 = encoder.encode(seq2);
		long encodedSeq3 = encoder.encode(seq3);
		long encodedSeq4 = encoder.encode(seq4);
		
		System.out.print("encoder test 1: ");
		if(seq1.equals(encoder.decode(encodedSeq1))) {
			System.out.println("success");
		}
		else {
			System.out.println("failure");
		}

		System.out.print("encoder test 2: ");
		if(seq2.equals(encoder.decode(encodedSeq2))) {
			System.out.println("success");
		}
		else {
			System.out.println("failure");
		}
		
		System.out.print("encoder test 3: ");
		if(seq3.equals(encoder.decode(encodedSeq3))) {
			System.out.println("success");
		}
		else {
			System.out.println("failure");
		}
		
		System.out.print("encoder test 4: ");
		if(seq4.equals(encoder.decode(encodedSeq4))) {
			System.out.println("success");
		}
		else {
			System.out.println("failure");
		}
	}
}
