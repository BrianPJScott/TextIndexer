package ie.atu.sw;

import java.util.Map;
import java.util.concurrent.*;

import java.util.ArrayList;

/**
 * @author Brian Scott
 * @version 1.0
 * @since 19.0.1
 * 
 *          The WordPages object allows for the storage of a word, and all
 *          associated pages for that word. It also contains methods which allow
 *          for: > the amount of words in the object to be queried > new words,
 *          and their associated pages to be added > the return of all words
 *          contained in the object > the return of all pages associated with a
 *          word
 * 
 */
public class WordPages {

	private Map<String, ConcurrentSkipListSet<String>> mapWordPages = new ConcurrentSkipListMap<String, ConcurrentSkipListSet<String>>();

	/**
	 * Adds a word and it's current page to the object. If the word already exists,
	 * it will instead add the new page to the existing list of pages in the entry.
	 * As the list of pages is a set, only unique entries will be stored, removing
	 * duplication.
	 * 
	 * @param word the word to be added to the object
	 * @param page the page associated with that word
	 */
	public void add(String word, String page) { // Big(O) = O(log n) - Contains on a map object

		ConcurrentSkipListSet<String> newPages = new ConcurrentSkipListSet<String>();

		// if the word already exists, add this page to the set of pages if it's not
		// already there.
		if (mapWordPages.containsKey(word)) {
			newPages = mapWordPages.get(word);
		}

		newPages.add(page);
		mapWordPages.put(word, newPages);
	}

	/**
	 * Method to return the size of the objects contents (amount of words stored in
	 * the object)
	 * 
	 * @return a integer representation of the number of words stored in the object
	 */
	public int size() { // Big(O) = O(1) - no loops
		return mapWordPages.size();
	}

	/**
	 * Overrides Super toString() method to return a String representation of the
	 * wordpages object contents
	 * 
	 * @return String representation of all contents of the object
	 */
	public String toString() { // Big(O) = O(log n) - entrySet on a map object

		StringBuilder result = new StringBuilder();

		// Build a string containing a list of all words and their pages in this object
		for (Map.Entry<String, ConcurrentSkipListSet<String>> i : mapWordPages.entrySet()) {
			result.append(i.getKey() + " : " + i.getValue() + "\n");
		}

		return result.toString();
	}

	/**
	 * Returns and ArrayList object containing all words contained in the object.
	 * 
	 * @return a ArrayList containing all words in the object
	 */
	public ArrayList<String> returnWords() { // Big(O) = O(log n) - entrySet on a map object

		ArrayList<String> ar = new ArrayList<String>();

		// build an ArrayList containing a list of all words in this object
		for (Map.Entry<String, ConcurrentSkipListSet<String>> i : mapWordPages.entrySet()) {
			ar.add(i.getKey());
		}

		return ar;

	}

	/**
	 * Returns the pages associated with a word
	 * 
	 * @param s the word to be searched for
	 * @return the set of pages the word appears on
	 */
	public ConcurrentSkipListSet<String> getPages(String s) { // Big(O) = O(1) - no loops

		return mapWordPages.get(s);

	}

}
