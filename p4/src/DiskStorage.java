import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;

/**
 * 
 * @author Matthew Castrigno
 * 
 *
 *         File Format For BTree Disk Storage
 *        ------- Tree Metadata
 *         ------- Values are byte
 *         offsets from the start of the file 
 *         0: Integer Pointer of Root Node 
 *         4: Integer Degree of Btree 
 *         8: Integer Sequence Length 
 *         12: Reserved Integer location 
 *         -----------------------------------------------
 *         Start of Nodes. The file offsets are constructed to accommodate full
 *         nodes The very first node stored will be location 16 after the
 *         Metadata of the Tree Locations are from the start of the node. 1 Node
 *         starts Byte 16. The space allocated for a node is as follows: 8 Bytes
 *         Metadata (1 Integer node pointer (it is not a direct pointer but
 *         rather a number that can be used to derive the actual offset) + 1
 *         Integer that holds the number of objects  currently in the Node.
 *         +1 Integer for leaf status, + 1 integer reserved. 
 *         (2*degree -1)*12 Bytes for each Object (long data + integer
 *         frequency) (2*degree)*4 Bytes for child Pointers
 *
 *         ----------------------------------------------- 
 *        Node Metadata
 *        ----------------------------------------------- 
 *        0: Integer Node Pointer 
 *        4: Integer Number of Objects 
 *        8: Integer Leaf 0 = false, 1 = true 
 *        12: Integer Reserved
 *        ----------------------------------------------- 
 *        Node Data
 *        ----------------------------------------------- 
 *        8 -((2*degree -1)*12): Node Objects (8 bytes data, 4 bytes frequency from the end
 *        of the nodes - (2*degree) *4): Integer child pointers
 *
 *        nodeSize = (16 + (2*degree -1)*12 + (2*degree)*4) 
 *        Start of Objects: 16 
 *        Start of Child Pointers:16 +((2*degree -1)*12)
 *
 *        nodeStart = 16 + nodeSize*(nodePointer - 1) // File starts at 0,
 *        nodePointer scheme starts at 1
 *
 */
public class DiskStorage {

	private RandomAccessFile raFile;
	private final String rwMode = "rw";
	private final String rMode = "r";
	private String fileName;
	private int degree;
	private int sequenceLength;
	private int nodeSize;
	private boolean cache = false;
	private int cacheSize = 100;
	private Cache<BTreeNode> NodeCache;

	public DiskStorage(String BaseFileName, int degree, int sequenceLength, boolean cache, int cacheSize) {
		this.cache = cache;
		this.cacheSize = cacheSize;
		if (cache) {
			NodeCache = new Cache<BTreeNode>(cacheSize);
		}
		this.degree = degree;
		this.sequenceLength = sequenceLength;

		this.nodeSize = (16 + (2 * degree - 1) * 12 + (2 * degree) * 4);
		this.fileName = BaseFileName + ".btree.data." + sequenceLength + "." + degree;
		try {
			raFile = new RandomAccessFile(fileName, rwMode);
			// Store Metadata, degree, sequenceLength
			raFile.seek(4);
			raFile.writeInt(degree);
			raFile.writeInt(sequenceLength);
			// raFile.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error 0: File not found!");
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Error 1: File not found!");
			System.exit(0);
		}
	}

	/**
	 * This constructor is used to open the file as opposed to creating a new file
	 * for new BTree
	 * 
	 * @param fullFilename
	 * @throws IOException
	 */
	public DiskStorage(String fullFilename, boolean cache, int cacheSize) {
		this.cache = cache;
		this.cacheSize = cacheSize;
		if (cache) {
			NodeCache = new Cache<BTreeNode>(cacheSize);
		}
		try {
			this.fileName = fullFilename;
			raFile = new RandomAccessFile(fullFilename, rMode); // Store Metadata, degree, sequenceLength
			raFile.seek(4);
			this.degree = raFile.readInt();
			this.sequenceLength = raFile.read();
			this.nodeSize = (16 + (2 * degree - 1) * 12 + (2 * degree) * 4);
			// raFile.close();
		} catch (IOException e) {
			System.err.println("Error 3: File not found!");
			System.exit(0);
		}
	}

	public void closeFile() {
		// if cache is in use, write back to cache before closing file
		try {
			raFile.close();
		} catch (IOException e) {
			System.err.println("Error 4: File not found!");
			System.exit(0);
		}
	}

	public int getDegree() {
		return degree;
	}

	public int getSeqenceLength() {
		return sequenceLength;
	}

	// Helper methods to translate node pointers locations in file
	public int nodeStart(int nodePointer) {
		int nodeStart;
		return nodeStart = 16 + nodeSize * (nodePointer - 1); // File starts at 0, nodePointer scheme starts at 1
	}

	public int childPointerStart(int nodePointer) {
		int childStart;
		return childStart = nodeStart(nodePointer) + ((2 * degree) - 1) * 12 + 16;
	}
	public int childOffsetIntNode(int nodePointer) {
		int childStart;
		return childStart =  ((2 * degree) - 1) * 12 + 16;
	}

	public void nodeWrite(BTreeNode node) { // Note we do not store the dummy object of the node
		if (cache) {
			if (NodeCache.getObject(node) != null) { // if the object is in cache
				NodeCache.removeObject(node); // we remove it and put to the
				NodeCache.addObject(node); // front of the cache then we proceed to disk
				// System.err.println("Cache Hit on Write");
			} else if (!NodeCache.addObject(node)) { // if the node is not in cache, we add it
				NodeCache.removeLast(); // but if the cache is full we make room
				NodeCache.addObject(node); // for it first
			}
		}

		try {
			// raFile = new RandomAccessFile(fileName, rwMode);
			int writeSeekPointer = node.getNodePointer();
			raFile.seek(nodeStart(node.getNodePointer()));
			ByteBuffer writeBuffer = ByteBuffer.allocate(nodeSize);
			//add stuff to buffer
			
			// System.err.println("filePointer at start of write is :" +
			// raFile.getFilePointer());
			
			//raFile.writeInt(node.getNodePointer());
			writeBuffer.putInt(node.getNodePointer());

			//raFile.writeInt(node.numOfObjects());
			writeBuffer.putInt(node.numOfObjects());

			if (!node.isLeaf()) {
				//raFile.writeInt(0);
				writeBuffer.putInt(0);
			} else {
				//raFile.writeInt(1);
				writeBuffer.putInt(1);
			}
			raFile.writeInt(0); // done just to advance the pointer
			for (int i = 1; i <= node.numOfObjects(); i++) {
				//raFile.writeLong(node.key(i));
				writeBuffer.putLong(node.key(i));

				//raFile.writeInt(node.keyObjectAt(i).getFrequency());
				writeBuffer.putInt(node.keyObjectAt(i).getFrequency());
			}
			if (!node.isLeaf()) {
				//raFile.seek(childPointerStart(node.getNodePointer()));
				writeBuffer.position(childOffsetIntNode(node.getNodePointer()));

				for (int i = 1; i <= node.numOfChildren(); i++) {
					// System.err.println("filePointer at start of child "+i+ " write is :" +
					// raFile.getFilePointer());
					//raFile.writeInt(node.getChildPointer(i));
					writeBuffer.putInt(node.getChildPointer(i));
				}
			}
			byte[] writeArray = new byte[nodeSize];
			writeArray = writeBuffer.array();
			raFile.write(writeArray); 			
			// raFile.close();
		} catch (IOException e) {
			System.err.println("Error 5: File not found!");
			System.exit(0);
			// on Disk so we need to keep track as we read and write
		} catch (NullPointerException e) {
			System.err.println("Error 6: File not found!");
			System.exit(0);
		}

	}

	// public void newNodeWrite(BTreeNode node) {
	// nodeWrite(node);
	//
	// }

	public void rootWrite(BTreeNode root) { // Special method necessary so root location can be stored in
		nodeWrite(root); // File MetaData
		try {
			raFile.seek(0);
			raFile.writeInt(root.getNodePointer());
		} catch (IOException e) {
			System.err.println("Error 7: File not found");
			System.exit(0);
		} // root pointer keep at location 0!
		catch (NullPointerException e) {
			System.err.println("Error 8: File not found");
			System.exit(0);
		}
	}

	public BTreeNode rootRead() { // uses File Metadata to retrieve root
		BTreeNode nodeToReturn = null;
		try {
			raFile.seek(0);
			nodeToReturn = nodeRead(raFile.readInt());
		} catch (IOException e) {
			System.err.println("Error 9: File not found");
			System.exit(0);
		}
		return nodeToReturn;
	}

	public BTreeNode nodeRead(int location) {
		BTreeNode nodeToReturn = new BTreeNode(0); // constructor inserts dummy object and dummy child pointer
		if (cache) {
			nodeToReturn.setNodePointer(location); // yes you need an equals method!
			if (NodeCache.getObject(nodeToReturn) != null) {
				// System.err.println("Cache hit on Read");
				nodeToReturn = NodeCache.removeObject(nodeToReturn);
				NodeCache.addObject(nodeToReturn);
				return nodeToReturn;
			}
		}
		try {
			raFile.seek(nodeStart(location));
			byte[] readArray = new byte[nodeSize];
			raFile.read(readArray);
			ByteBuffer readBufferArray = ByteBuffer.wrap(readArray );
			//writeBuffer.put(readBufferArray );
			// System.err.println("filePointer at start of read is :" +
			// raFile.getFilePointer());

			//nodeToReturn.setNodePointer(raFile.readInt());
			nodeToReturn.setNodePointer(readBufferArray.getInt());
			
			//int localNumOfObjects = raFile.readInt();
			int localNumOfObjects = readBufferArray.getInt();

			//int localIsLeaf = raFile.readInt();
			int localIsLeaf = readBufferArray.getInt();

			//int justToMovePointer = raFile.readInt(); // just moves the file pointer over reserved location
			int justToMovePointer = readBufferArray.getInt(); // just moves the file pointer over reserved location

			if (localIsLeaf == 0) {
				nodeToReturn.setLeaf(false);
			} else {
				nodeToReturn.setLeaf(true);
			}
			for (int i = 1; i <= localNumOfObjects; i++) {
				//long dataForNewTreeObject = raFile.readLong();
				long dataForNewTreeObject = readBufferArray.getLong();

				//int frequencyForNewObject = raFile.readInt();
				int frequencyForNewObject = readBufferArray.getInt(); 

				TreeObject newObject = new TreeObject(dataForNewTreeObject);
				newObject.putFrequency(frequencyForNewObject);
				nodeToReturn.putObject(i, newObject);
			}
			if (!nodeToReturn.isLeaf()) {
				//raFile.seek(childPointerStart(nodeToReturn.getNodePointer()));
				readBufferArray.position(childOffsetIntNode(nodeToReturn.getNodePointer()));
				// for (int i = 1; i<= nodeToReturn.numOfChildren(); i++) {
				for (int i = 1; i <= localNumOfObjects + 1; i++) { // your rely on the number children in the array but
																	// you have not popluated it yet
					//nodeToReturn.setChildPointer(i, raFile.readInt());
					nodeToReturn.setChildPointer(i, readBufferArray.getInt()); 
				}
			}
		} catch (IOException e) {
			System.err.println("Error 10: File not found");
			System.exit(0);
		}
		if (cache) {
			NodeCache.addObject(nodeToReturn);
		}

		return nodeToReturn;

	}
}
