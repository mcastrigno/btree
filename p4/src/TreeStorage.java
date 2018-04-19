import java.util.ArrayList;

/**
 * 
 * @author Matthew Castrigno 
 * 		   This class is intended to emulate the disk storage. It is for testing purposes
 *         One idea is to create a disk storage interface and  change this to implement that interface
 *         and have the "real" disk storage implement the interface also. When the 
 * 
 */
public class TreeStorage {

	private int degree = 2;
	private BTreeNode dummyNode = new BTreeNode(0);
	
	// In this emulation the nodes are kept in an arraylist. 
	// The pointers are just indexes into the arraylist.
	// This storage emulation will store and retrieve nodes
	// based on the pointers within the node (nodePointer).
	// The nodes are never deleted so there are no methods for
	// deleting them or re-cycling their locations in the array.
	// The BTree class has a node count, this acts a pointer.
	// Each time the BTree class creates a node it increments the
	// count and subsequently the nodePointer and assigns it to the new node.
	// This should also work for the "real" storage class will convert the 
	// nodePointer to a file location by a simple multiplication of 
	// Node block size times the nodePointer
	
	private ArrayList<BTreeNode> treeNodes = new ArrayList<BTreeNode>();  	
	
	public TreeStorage(String gbkFilename, int degree, int sequenceLength) {
		this.degree = degree;
		treeNodes.add(dummyNode);   // can comment this needed to start a index 0 instead of one
		//degreeWrite(degree);
	}
	//TBD - what metadata is needed for the BTree?
	//methods to save metaData
	public void degreeWrite(int degree) {
	
	}
	
	public void nodeWrite(BTreeNode node) {				//the pointer is in the BtreeNode so we retrieve it.
		treeNodes.set(node.getNodePointer(), node);		//we use set because we are going to write over the node
	}													//New node will use the add method
	
	public int nodeAdd(BTreeNode node) {
		treeNodes.add(node);
		return numOfNodes();
	
		
	}
	public BTreeNode nodeRead(int nodePointer) {
		return treeNodes.get(nodePointer);
		
	}
	public int numOfNodes() {
		return treeNodes.size() -1;
	}
}
