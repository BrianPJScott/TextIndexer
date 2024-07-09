package ie.atu.sw;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/** Dictionary
 * @author Brian Scott
 * @version 1.0
 * @since 19.0.1
 * 
 *          Each instance of the class <b>Dictionary</b> can contain one or more
 *          words. Each word is mapped to a definition of that word. Each word
 *          can have one and only one definition. Each word must be unique.
 * 
 */
public class Dictionary {

	private Map<String, String> mapDictionary = new ConcurrentSkipListMap<String, String>();

	/**
	 * Adds a word and it's matching definition to the Dictionary object.
	 * 
	 * @param newWord New Word to be added to the dictionary object
	 * @param newDef  New definition for the word to be added to the dictionary
	 * @return
	 */
	public int add(String newWord, String newDef) { // Big(O) = O(log n) - Map object

		// if the dictionary doesn't contain the word, then add it. Otherwise, don't.
		if (!mapDictionary.containsKey(newWord)) {
			mapDictionary.put(newWord, newDef);
			return 1;
		} else {
			return 0;
		}

	}

	/**
	 * Returns the size of the dictionary object (the amount of words help by the
	 * object).
	 * 
	 * @return An interegr representatio of the amount of words in the dictionary
	 */
	public int size() { // Big(O) = O(log n) - Map object
		return mapDictionary.size();
	}

	/**
	 * Overrides Super toString() method to return a String representation of the
	 * Dictionary contents
	 * 
	 * @return String representation of all contents of the Dictionary object
	 */
	@Override
	public String toString() { // Big(O) = O(log n) - Map object

		// Helper method for debugging. Used to display the content of the dictionary
		// object.
		StringBuilder sb = new StringBuilder("<<< Contents of Dictionary >>>\\r\\n");

		// Gathers all elements contained in the dictionary map, builds a string
		// representation and returns that String
		for (Map.Entry<String, String> s : mapDictionary.entrySet()) {
			sb.append(s.getKey() + " : " + s.getValue() + "\n");
		}

		return sb.toString();
	}

	/**
	 * Used to identify if a word has already been entered into this dictionary (has
	 * been defined in the dictionary.
	 * 
	 * @param s The word that is to be checked for
	 * @return True if the word is already in the dictionary. False if the word does
	 *         not exist in the dictionary.
	 */
	public boolean isDefined(String s) { // Big(O) = O(log n) - Map object

		// If the map contains the argument s then return true, otherwise return false.
		if (mapDictionary.containsKey(s)) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the definition of a word stored in this dictionary.
	 * 
	 * @param s The word that we wish to receive the definition for
	 * @return A String representation of the definition for the word
	 */
	public String getDefinition(String s) { // Big(O) = O(log n) - Map object

		// If the word is defined, return the definition, otherwise return null.
		if (isDefined(s)) {
			return mapDictionary.get(s);
		} else {
			return null;
		}

	}
}
