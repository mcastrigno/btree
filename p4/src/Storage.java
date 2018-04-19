import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * @author James Brooks
 *
 */
public class Storage {
	private RandomAccessFile raf;
	private ByteBuffer buffer;
	private final String MODE = "rw"; //access mode used for RandomAccessFile. See RandomAccessFile docs for more info
	
	public Storage(String fileName) {
		try {
			raf = new RandomAccessFile(fileName, MODE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void nodeWrite(BTreeNode root) {
		// TODO Auto-generated method stub
		
	} 
	
	public BTreeNode nodeRead(int location){
		//TODO 
		return null;
	}
}
