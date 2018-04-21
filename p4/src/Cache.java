/**
 * Generic Cache with methods to support a multilevel inclusion property usage
 * 
 *  
 * @author Matthew Castrigno
 * @version 1.0 cs321 p1
 * @param <T>
 */
public class Cache<T> {
	private IUDoubleLinkedList<T> cacheDLL;
	private int cacheSize;

	
	public Cache(int cacheSize) {
		this.cacheSize = cacheSize;
		this.cacheDLL = new IUDoubleLinkedList<T>(); 
		
	}
	/**
	 * 
	 * @param element object to be retrieved 
	 * @return returns object if found, if not found returns null
	 */
	public T getObject(T element) {
		if(cacheDLL.contains(element)) {
			T returned = element;
			return returned;
		}
		return null;
	}
		
	public Boolean addObject(T element) {
		if (cacheSize > cacheDLL.size()) {
			cacheDLL.addToFront(element);
			return true;
		}
		return false;
	}
	
	public void removeLast() {
		cacheDLL.removeLast();
	}
	
	public T removeObject(T element) {
		T returned = cacheDLL.remove(element);
		return returned;
	}
//	public Boolean removeObject(T element) {
//		if(getObject(element) !=null){
//			cacheDLL.remove(element);
//			return true;
//		}
//		return false;
//	}
	public void clearCache() {
		int size = cacheDLL.size();
		for (int i = 0; i < size; i++) {
			cacheDLL.remove(i);
		}
	}
	
	public String toString() {
		return cacheDLL.toString();
			
		}
	
}
