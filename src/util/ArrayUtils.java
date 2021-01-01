package util;

import java.util.ArrayList;

public final class ArrayUtils{
	
	/**
	 * Add the given element to the given {@link ArrayList}, inserting it into a position so that the list remains sorted.
	 * @param <E> The type of the lists, must be {@link Comparable}
	 * @param arr A sorted ArrayList
	 * @param e The element to add
	 * @param allowDuplicates true to allow duplicate values to be added, false to not add them. 
	 * 	If the value to add is a duplicate and this parameter is true, nothing happens and this method returns false
	 * @return true if the insert is successful, false otherwise
	 */
	public static <E extends Comparable<E>> boolean insertSorted(ArrayList<E> arr, E e, boolean allowDuplicates){
		// Get the ends of the ArrayList
		int low = 0;
		int high = arr.size();
		int mid;
		
		// Track the middle element
		E midO;
		
		// Loop until low and high converge on one value
		while(low < high){
			// Get the middle index and middle element
			mid = (low + high) >> 1;
			midO = arr.get(mid);
			
			// Compare the to add element to the middle element
			int comp = e.compareTo(midO);
			
			// If the element is after mid, one before mid becomes the new high
			if(comp < 0){
				high = mid;
			}
			// If the element is before mid, one after mid becomes the new low
			else if(comp > 0){
				low = mid + 1;
			}
			// Otherwise, the element is the same as mid, and should be placed after mid.
			// Or if it is a duplicate and they are not allowed, then return false
			else{
				if(allowDuplicates){
					low = mid + 1;
					high = low;
				}
				else return false;
			}
		}
		
		// Add the value, if it encountered an error while being added, return false
		try{
			arr.add(low, e);
			return true;
		}catch(IndexOutOfBoundsException err){
			err.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Add the given element to the given {@link ArrayList}, inserting it into a position so that the list remains sorted.
	 * @param <E> The type of the lists, must be {@link Comparable}
	 * @param arr A sorted ArrayList
	 * @param e The element to add
	 * @return true if the insert is successful, false otherwise
	 */
	public static <E extends Comparable<E>> boolean insertSorted(ArrayList<E> arr, E e){
		return insertSorted(arr, e, true);
	}
	
	/** Cannot instantiate ArrayUtils */
	private ArrayUtils(){}
}
