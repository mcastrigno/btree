import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double-linked node based implementation of IndexUnsortedList
 * A List Iterator with optional working add(), remove(), and set methods are implemented.
 *  
 * @author Matthew Castrigno
 * @version 1.0 CS221 Summer 2017
 * @param <T>
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	private LinearNode<T>  head;
	private LinearNode<T>  tail;
	private int size;
	private int modCount;

	public IUDoubleLinkedList(){
		head = tail = null;
		size = 0;
		modCount = 0;
	}
	@Override
	public void addToFront(T element) {	
		ListIterator<T> lit = listIterator();
		lit.add(element);
	}

	@Override
	public void addToRear(T element) {
		ListIterator<T> lit = listIterator(size);
		lit.add(element);	
	}

	@Override 
	public void add(T element) {	
		ListIterator<T> lit = listIterator(size);
		lit.add(element);
	}

	@Override  
	public void addAfter(T element, T target) {
		ListIterator<T> lit = listIterator();
		boolean foundIt = false;
		T test  = null;
		while (test != target && !foundIt){
			test = lit.next();
			if (test == target){
				foundIt = true;
			}	
		}
		if(!foundIt){
			throw new NoSuchElementException();
		}	
		lit.add(element);
	}

	@Override
	public void add(int index, T element) {
		if(index < 0 || index > size){
			throw new IndexOutOfBoundsException();
		}	
		ListIterator<T> lit = listIterator(index);
		lit.add(element);
	}

	@Override
	public T removeFirst() {
		if(isEmpty()){
			throw new NoSuchElementException();
		}
		ListIterator<T> lit = listIterator();
		T retVal = lit.next();
		lit.remove();
		return retVal;
	}

	@Override
	public T removeLast() {
		if(isEmpty()){
			throw new NoSuchElementException();
		}	
		ListIterator<T> lit = listIterator(size-1);
		T retVal = lit.next();
		lit.remove();
		return retVal;
	}

	@Override
	public T remove(T element) {
		ListIterator<T> lit = listIterator();
		T returned = null;
		boolean foundIt = false;
		while (lit.hasNext() && !foundIt){
			returned = lit.next();
			if(returned.equals(element)){
				foundIt= true;
			}
		}
		if (!foundIt){
			throw new NoSuchElementException();		
		}
		lit.remove();
		return returned;
	}

	@Override
	public T remove(int index) {
		if(index < 0 || index >= size){
			throw new IndexOutOfBoundsException();
		}
		ListIterator<T> lit = listIterator(index);
		T returned = lit.next();
		lit.remove();
		return returned;
	}

	@Override
	public void set(int index, T element) {
		if(index < 0 || index >= size){
			throw new IndexOutOfBoundsException();
		}	
		ListIterator<T> lit = listIterator(index);
		lit.next();
		lit.set(element);  
	}

	@Override
	public T get(int index) {
		if(index < 0 || index >= size){
			throw new IndexOutOfBoundsException();
		}
		ListIterator<T> lit = listIterator();
		T returned = lit.next();
		while(lit.previousIndex() != index ){
			returned = lit.next();
		}
		return returned;
	}

	@Override 
	public int indexOf(T element) { 
		int index=0;
		ListIterator<T> lit = listIterator();
		while (lit.hasNext() && lit.next() != element && index < size){
			index++;
		}
		if (index >= size){
			index = -1;
		}
		return index;
	}

	@Override
	public T first() {
		if(isEmpty()){
			throw new NoSuchElementException();
		}	
		ListIterator<T> lit = listIterator();
		T returned = lit.next();
		return returned;
	}

	@Override
	public T last() {
		if(isEmpty()){
			throw new NoSuchElementException();
		}	
		ListIterator<T> lit = listIterator(size-1);
		T returned = lit.next();
		return returned;
	}

	@Override 
	public boolean contains(T target) {
		boolean foundIt = false;
		ListIterator<T> lit = listIterator();
		while (lit.hasNext() ){
//			if (lit.next() == target){
			if (lit.next().equals(target)){
				foundIt = true;
			}
		}
		return foundIt;	
	}

	@Override
	public boolean isEmpty() {
		return (size() == 0);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append("[");
		for(int i=0; i < size; i++){
			str.append(get(i).toString());
			str.append(",");
		}
		str.append("]");
		if (!isEmpty()){
			str.deleteCharAt(str.length()-2);
		}
		return str.toString();
	}

	@Override
	public Iterator<T> iterator() {
		return listIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		return new DLIterator();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new DLIterator(startingIndex);
	}
	private class DLIterator implements ListIterator<T>{
		private LinearNode<T> nextNode;
		private int iterModCount;
		private int nextIndex;
		private LinearNode<T> lastReturned;

		public DLIterator(){
			nextNode = head;
			iterModCount = modCount;
			nextIndex = 0;
			lastReturned = null;
		}

		public DLIterator(int startingIndex){
			
			if(startingIndex < 0 || startingIndex > size){
				throw new IndexOutOfBoundsException();
			}
			nextNode = head;    //added
			for (int i= 0; i < startingIndex; i++){
				nextNode = nextNode.getNext();
			}
			nextIndex = startingIndex;
			lastReturned = null;
			iterModCount = modCount;
		}

		@Override
		// Inserts the specified element into the list (optional operation). The element is inserted immediately 
		// before the element that would be returned by next(), if any, and after the element that would be returned 
		// by previous(), if any. (If the list contains no elements, the new element becomes the sole element on the list.)
		// The new element is inserted before the implicit cursor: a subsequent call to next would be unaffected, and a 
		// subsequent call to previous would return the new element. (This call increases by one the value that would be 
		// returned by a call to nextIndex or previousIndex.)		
		public void add(T e) {
			if ( iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
			LinearNode<T> newNode = new LinearNode<T>(e);
			//Case of empty list
			if (isEmpty()){
				nextNode= newNode;
				nextNode.setNext(null);
				nextNode.setPrev(null);
				head = tail = nextNode;

				//Case of iterator before head
			}else if(nextNode == head){
				newNode.setNext(nextNode);
				nextNode.setPrev(newNode);
				nextNode = newNode;
				head = nextNode;

				//Case of iterator between two elements
			}else if(nextNode!= head && nextNode != null){
				newNode.setNext(nextNode);
				newNode.setPrev(nextNode.getPrev());
				nextNode.getPrev().setNext(newNode);
				nextNode.setPrev(newNode);
				nextNode = newNode; 

				//Case of iterator after the last element, last returned was the tail.
			}else{ 
				newNode.setNext(null);
				newNode.setPrev(tail);
				tail.setNext(newNode);
				nextNode = newNode; 
				tail = nextNode;
			}
			nextIndex++;
			size++;
			modCount++;
			iterModCount++; 
			lastReturned= null; 
		}

		@Override
		public boolean hasNext() {
			if (iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
			return (nextNode != null);
		}

		@Override
		public boolean hasPrevious() {
			if (iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
			return (nextNode != head);
		}

		@Override
		public T next() {
			if ( iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
			if (!hasNext()){
				throw new NoSuchElementException();
			}
			T retVal = nextNode.getElement();
			lastReturned = nextNode; 			
			nextNode = nextNode.getNext();
			nextIndex++;   						
			return retVal;
		}

		@Override
		public int nextIndex() {
			if ( iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
			return nextIndex;
			
		}

		@Override
		public T previous() {
			if ( iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
			if (!hasPrevious()){
				throw new NoSuchElementException();
			}
			if (nextNode == null){
				nextNode = tail;
			}else{
				nextNode = nextNode.getPrev();
			}
			lastReturned = nextNode; 			
			nextIndex--;
			return nextNode.getElement();
		}


		@Override
		public int previousIndex() {
			if ( iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
			return nextIndex-1;
		}

		@Override
		public void remove() {
			if ( iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
			if (lastReturned == null){
				throw new IllegalStateException();
			}
			//This if is for when the last returned was from previous() operation
			if (lastReturned != nextNode){
				nextIndex--;
			}else{
				nextNode = nextNode.getNext();
				nextIndex++;												
			}
			//This if addresses setting the new next of the element previous to the one we are removing.
			if (lastReturned == head){ 
				head = head.getNext();										//Sets a new head if it was the head last returned.
			}else{
				lastReturned.getPrev().setNext(lastReturned.getNext());		//This sets the next of element before the last returned
			}																//to the next of the last returned
			//This if addresses setting the new previous of the element next to the one we are removing.
			if (lastReturned == tail){
				tail = tail.getPrev();										//Sets a new tail if the tail was the last returned 
			}else{
				lastReturned.getNext().setPrev(lastReturned.getPrev());		//This sets the previous of the element after the last returned
			}																//to the previous of the last returned.	
			size--;
			modCount++;
			iterModCount++; 
			lastReturned= null; 
		}

		@Override
		// Replaces the last element returned by next() or previous() with the specified element (optional operation). 
		// This call can be made only if neither remove() nor add(E) have been called after the last call to next or previous.		
		// throws IllegalStateException - if neither next nor previous have been called, or remove or add have been called 
		// after the last call to next or previous		
		public void set(T e) {
			if ( iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
			if(lastReturned == null){
				throw new IllegalStateException();
			}
			//This method sets what is the lastReturned node to the element provided.
			//Four cases, the lastReturned was the head and tail, just head, just tail, or middle element.
			//Head and Tail
			LinearNode<T> newNode = new LinearNode<T>(e);
			if((lastReturned == head) && (lastReturned ==tail)){
				head = tail = newNode;
				//Head where size greater than 1
			}else if((lastReturned == head)){
				newNode.setNext(lastReturned.getNext());
				lastReturned.getNext().setPrev(newNode);
				head = newNode; 
				//Middle
			}else if((lastReturned != head) && (lastReturned !=tail)){
				newNode.setNext(lastReturned.getNext());
				newNode.setPrev(lastReturned.getPrev());
				lastReturned.getPrev().setNext(newNode);
				lastReturned.getNext().setPrev(newNode);
				//Tail where size greater than 1
			}else{
				newNode.setPrev(lastReturned.getPrev());
				lastReturned.getPrev().setNext(newNode);
				tail = newNode;
			}
			modCount++;
			iterModCount++; 
			lastReturned = null;
		}

	}
}


