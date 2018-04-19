import java.util.ArrayList;
//import java.util.LinkedList;
import java.util.List;

/**
 * @author James Brooks
 * @author Matthew Castrigno added some methods and instance variables, changed some names to be more descriptive
 *                           added toString for debugging, modified constructor
 *
 */
public class BTreeNode {
	private GeneSequenceEncoder encoder = new GeneSequenceEncoder();
	private int location; 					//byte offset of this node in file
	private int nodePointer; 				//Consistent but descriptive name	
	private boolean leaf =  false;  		//default value 
	private ArrayList<TreeObject> objects;  //Array list was suggest by Yeh and the tutor
//	private int parentPointer; 				//pointer to byte offset location of parent in file

	private List<Integer> childPointers = new ArrayList<>();	//Makes an ArrayList of ints instead of Integers

	private TreeObject dummyTreeObject = new TreeObject(0) ;
	
	public BTreeNode(int nodePointer) {		//degree is not used - delete it?
		this.nodePointer = nodePointer;
		this.objects = new ArrayList<TreeObject>();
		objects.add(dummyTreeObject);    				//this is a dummy object so we can index from one
		childPointers.add(0,0);							//add dummy to take up position 0
	}
	
	public int numOfObjects() {
		return objects.size() - 1;
	}
	public int numOfChildren() {
		return childPointers.size() -1;
	}
	
	public int getNodePointer() {
		return nodePointer;
	}
	public void setNodePointer(int nodePointer) {
		this.nodePointer = nodePointer;
	}
	
//	public int getParentPointer() {
//		return parentPointer;
//	}
	
	public int getChildPointer(int i) {
		return childPointers.get(i);
	}
	public int removeChildPointer(int i) {
		return childPointers.remove(i);
	}
	
	public void setChildPointer(int i, int childPointer) {
		if (i == childPointers.size()) {
			childPointers.add(childPointer);
		}else if ((i >= 1) && (i < childPointers.size())) {
			childPointers.set(i, childPointer);
		}
		else {
			System.out.println("Invalid operation, attempted to add pointer at index : " + i + " but number of children is : " + childPointers.size());
		}
	
}
	
	/**
	 * 
	 * @param i location in node to put object.
	 * 			When this operation adds an object we use an add operation.
	 * 			If i is in current range then the set operation is used
	 */
	public void putObject(int i, TreeObject newObject) {
		if (i == objects.size()) {
			objects.add(newObject);
		}else if ((i >= 1) && (i < objects.size())) {
			objects.set(i, newObject);
		}
		else {
			System.out.println("Invalid operation, attempted to add object at index : " + i + " of node at pointer " +  getNodePointer()  +" but number of nodes is : " + objects.size());
		}
		
	}
	
	
	
	public boolean isLeaf() {
		return leaf;
	}
	

	public void setLeaf(boolean isLeaf) {
		leaf = isLeaf;
	}
	
	public long key(int i) {
		return objects.get(i).getData();
	}


	public TreeObject keyObjectAt(int i) {
		return objects.get(i);
	}
	
	public TreeObject removeKeyObjectAt(int i) {
		return objects.remove(i);
	}
	
	public String toString() {
		String returnString = "Node located at pointer "+ nodePointer + " has " + numOfObjects() + " objects and " + numOfChildren() + " children.\n";
		for (int i= 1; i < objects.size(); i++) {  // Deliberately skipping the first one, its a dummy
			returnString = returnString + "Object [" + i + "]" + " has key value of " + key(i) + " and frequency of " + keyObjectAt(i).getFrequency() + "\n" ;
		}
		returnString = returnString +"\n";
		for (int i = 1; i < childPointers.size(); i++) {
			returnString = returnString + "ChildPointer [" + i + "]" + " is " +childPointers.get(i) +"\n";
		}
		returnString += "\n";
		return returnString;
	}
	public String toDnaString() {
		String returnString = "";
		for (int i= 1; i < objects.size(); i++) {
			returnString += (encoder.decode(key(i)) +" " + keyObjectAt(i).getFrequency() +"\n");
		}
		return returnString;
	
	}	
}
