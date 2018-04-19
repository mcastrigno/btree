import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author James Brooks
 *
 */
public class GeneSequenceEncoder {
	
	public GeneSequenceEncoder() {
		
	}
	
	public long encode(String sequence) {
		char[] charArray = sequence.toCharArray();
		String encodedString = "";
		for(int i = 0; i < charArray.length; i++) {
			if(charArray[i] == 'a') {
				encodedString = encodedString + "00";
			}
			if(charArray[i] == 'c') {
				encodedString = encodedString + "01";
			}
			if(charArray[i] == 'g') {
				encodedString = encodedString + "10";
			}
			if(charArray[i] == 't') {
				encodedString = encodedString + "11";
			}
		}
		encodedString = "1" + encodedString; //Add extra 1 to front to retain leading zeros when converting from binary string to long
		return Long.parseLong(encodedString, 2);
	}
	
	public String decode(long sequence) {
		String decodedString = "";
		char decodedChar = ' ';
		String encodedString = Long.toBinaryString(sequence);
		encodedString = encodedString.substring(1, encodedString.length()); //remove the extra 1
		for(int i = 0; i <= encodedString.length() - 2; i += 2) {
			if(encodedString.substring(i, (i + 2)).equals("00")) {
				decodedChar = 'a';
			}
			else if(encodedString.substring(i, (i + 2)).equals("01")) {
				decodedChar = 'c';
			}
			else if(encodedString.substring(i, (i + 2)).equals("10")) {
				decodedChar = 'g';
			}
			else if(encodedString.substring(i, (i + 2)).equals("11")) {
				decodedChar = 't';
			}
			decodedString = decodedString + decodedChar;
		}
		return decodedString;
	}
}
