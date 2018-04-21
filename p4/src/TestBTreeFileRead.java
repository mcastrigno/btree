import java.io.IOException;

public class TestBTreeFileRead {

	
	static String fullFilename = "testFilename.btree.data.2.2";
	static boolean cache = true;
	static int cacheSize = 100;
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

	BTree readBtree = new BTree(fullFilename, cache,cacheSize);
		
	}

}
