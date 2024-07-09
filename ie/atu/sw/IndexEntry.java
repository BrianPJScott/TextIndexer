package ie.atu.sw;

import java.util.concurrent.*;

/**
 * @author Brian Scott
 * @version 1.0
 * @since 19.0.1
 * 
 *          The IndexEntry object contains all items necessary to define an
 *          index item. word: the word being defined. definition: the definition
 *          for the word. setWordPages: a record of all pages the word appears
 *          on, help in a collection. <i>Note: this class was not used in the
 *          final version of the project</i>
 */
public class IndexEntry {

	private String word;
	private String definition;
	private ConcurrentSkipListSet<String> setWordPages;

	/**
	 * Getters and setters foe the private variables word, definition and
	 * setWordPages
	 */
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public ConcurrentSkipListSet<String> getWordPages() {
		return setWordPages;
	}

	public void setWordPages(ConcurrentSkipListSet<String> setWordPages) {
		this.setWordPages = setWordPages;
	}

	/**
	 * Constructor for the IndexEntry object. Requires a word, definition and
	 * collection of newPages
	 * 
	 * @param newWord  the word associated with this object
	 * @param def      the definition defined for the word associated with this
	 *                 object
	 * @param newPages the collection of pages that word appears in the sample text
	 */
	public IndexEntry(String newWord, String def, ConcurrentSkipListSet<String> newPages) { // Big(O) = O(1) - Set
																							// object, being constructed

		// Object contains three variables: a word, it;s definition and the pages it
		// appears on.
		setWord(newWord);
		setDefinition(definition);
		setWordPages(newPages);
	}

}
