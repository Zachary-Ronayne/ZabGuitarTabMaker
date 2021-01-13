package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

		arr.add(low, e);
		return true;
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
	
	/**
	 * Add the given element to the list only if the given list does not contain the given element
	 * @param <E> The type of the elements in the list
	 * @param list The list
	 * @param e The element to potentially add
	 * @return true if the item was added, false otherwise
	 */
	public static <E> boolean addWithoutDuplicate(List<E> list, E e){
		if(list.contains(e)) return false;
		return list.add(e);
	}
	
	/**
	 * Add the all of the given elements to the given list, but only the items which the given list does not contain
	 * @param <E> The type of the elements in the list
	 * @param list The list to add items to
	 * @param toAdd The items to potentially add
	 * @return true if all items were added, false otherwise
	 */
	public static <E> boolean addManyWithoutDuplicate(List<E> list, Collection<E> toAdd){
		boolean success = true;
		for(E e : toAdd){
			if(!addWithoutDuplicate(list, e)) success = false;
		}
		return success;
	}
	
	/** Cannot instantiate ArrayUtils */
	private ArrayUtils(){}
}
