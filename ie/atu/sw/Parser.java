package ie.atu.sw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.Set;

/**
 * 
 * @author Brian Scott
 * @version 1.0
 * @since 19.0.1
 *
 *          The parser class takes arguments which define the dictionary, list
 *          of common words, output choice and location where needed(file,
 *          screen), and the source file to be processed. It then processes the
 *          file, providing the index for that file as defined by the following
 *          definitions: > Each word is defined in the dictionary > Each word is
 *          not listed in the commonwords/exclusions list > Each word was at
 *          least one instance in the source text
 * 
 *          Each time a word is found, it's page number is tracked and added to
 *          a list of pages numbers on which the word appears.
 * 
 */
public class Parser {

	private boolean endrun; // switch to end the run if an error is found in the process
	private String inputFile;
	private String dictionary;
	private String commonWords;
	private String outputFile;
	private volatile int currentLine; // counter of current line in the source file
	private volatile int currentPage = 1; // current page in the source file

	private Set<String> setExclusions = new ConcurrentSkipListSet<String>(); // set of words to be excluded
	private Dictionary myDictionary = new Dictionary(); // a dictionary object - stores words and their definitions
	private WordPages myWordPages = new WordPages(); // a wordPages object - stored words and their page occurrences

	/**
	 * Constructor for the parser object
	 * 
	 * @param newOutput      The file location for the index. only used if the user
	 *                       wishes to output to a file.
	 * @param newInput       The file location for the source text.
	 * @param newDictionary  The file location for the dictionary file.
	 * @param newCommonWords The file location for the list of common words to be
	 *                       excluded from the index.
	 */
	public Parser(String newOutput, String newInput, String newDictionary, String newCommonWords) {

		// Constructs the parser object and sets the endrun switch to false
		setOutput(newOutput);
		setDictionary(newDictionary);
		setInput(newInput);
		setCommonWords(newCommonWords);
		endrun = false;

	}

	/**
	 * Getters and setters for the Parser object variables.
	 */
	public void setInput(String newInput) { // Big(O) = O(1) - No loops
		inputFile = newInput;
	}

	public void setDictionary(String newDictionary) { // Big(O) = O(1) - No loops
		dictionary = newDictionary;
	}

	public void setCommonWords(String newCommonWords) { // Big(O) = O(1) - No loops
		commonWords = newCommonWords;
	}

	public void setOutput(String newOutput) { // Big(O) = O(1) - No loops
		outputFile = newOutput;
	}

	/**
	 * The run method runs each subroutine in order to generate the index 1) load
	 * the list of common words / exclusions from the commonwords file. 2) load the
	 * dictionary from the dictionary file 3) load the input/source text 4) output
	 * the results/index to the chosen destinaton (file/screen) If any error is
	 * found during this process, the error will be displayed and the run,
	 * terminated.
	 * 
	 * @param screen defines if the user wishes to display the results to the
	 *               screen. True: print to console out. False: write to file.
	 */
	public void run(Boolean screen) { // Big(O) = O(1) - No loops

		// runs through the steps required to generate the index in order
		// if an error is found, the run breaks early.
		loadCommonWords();
		if (!endrun)
			loadDictionary();
		if (!endrun)
			loadInputFile();
		if (!endrun) {
			if (!screen) { // if the user wants to output to a file...
				writeToFile();
			} else if (screen) { // if the user wants to output to the System.out...
				writeToScreen();
			} else {
				System.out.println("<Invalid output destination for results>");
			}
		}
	}

	/**
	 * Method to load the common words file. The method reads each line of the file
	 * and passes the line to a virtual thread for processing.
	 */
	private void loadCommonWords() { // Big(O) = O(n) - defined by number of lines in the file

		System.out.println("Opening exclusions...");

		// Load the exclusions file and pass to the exclusions list through a virtual
		// thread
		try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
			Files.lines(Paths.get(commonWords)).forEach(text -> pool.execute(() -> addToCommon(text))); // pass each
																										// line of the
																										// file to a
																										// threaded
																										// addToCommon
			System.out.println("Exclusions processed! - " + setExclusions.size() + " words ignored.");
		} catch (Exception e) {
			System.out.println("Error: " + e.getLocalizedMessage()); // if there was an error in the file write process,
																		// output the error to the user
			endrun = true;
		}

	}

	/**
	 * Method to add words to be excluded from the index.
	 * 
	 * @param text the word to be added to the exclusion list.
	 */
	private void addToCommon(String text) { // Big(O) = O(1) - No loops

		// add the stripped and converted to lowercase word to the exclusions object
		setExclusions.add(StripAndLower(text));
	}

	/**
	 * Method to load the dictionary file. The method reads each line of the file
	 * and passes the line to a virtual thread for processing.
	 */
	private void loadDictionary() {// Big(O) = O(n) - defined by number of lines in the file

		// Load the dictionary file and pass to the exclusions list through a virtual
		// thread
		System.out.println("Opening dictionary...");
		try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
			Files.lines(Paths.get(dictionary)).forEach(text -> pool.execute(() -> addToDictionary(text))); // pass each
																											// line of
																											// the file
																											// to a
																											// threaded
																											// addToDictionary
			System.out.println("Dictionary processed! - " + myDictionary.size() + " words defined.");
		} catch (Exception e) {
			System.out.println("Error: " + e.getLocalizedMessage()); // if there was an error in the file write process,
																		// output the error to the user
			endrun = true;
		}

	}

	/**
	 * Method to add words and their associated definition to the dictionary object.
	 * The method separated the word from it's definition and then adds them both to
	 * the dictionary object.
	 * 
	 * @param text a String object representing both the word and it's definition
	 *             delimited by ','.
	 */
	private void addToDictionary(String text) { // Big(O) = O(1) - No loops
		// new string array containing both the word and it's definition (delimited by
		// ',')
		String[] wordAndDef = text.split(",");

		wordAndDef[0] = StripAndLower(wordAndDef[0]); // strip and turn to lower case
		if (!setExclusions.contains(wordAndDef[0])) { // if the word was not in the exclusions list...
			myDictionary.add(wordAndDef[0], wordAndDef[1]); // add the word and it's definition to the dictionary
		}
	}

	/**
	 * Method to load the source/input file for processing. The method reads each
	 * line of the file and passes the line to a virtual thread for processing.
	 */
	private void loadInputFile() { // Big(O) = O(n) - defined by number of lines in the file

		currentLine = 0;

		System.out.println("Opening Input File...");
		try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
			Files.lines(Paths.get(inputFile)).forEach(text -> pool.execute(() -> addToWordPage(text, ++currentLine))); // pass
																														// each
																														// line
																														// of
																														// the
																														// source
																														// file
																														// to
																														// a
																														// threaded
																														// addToWordPage
																														// method,
																														// also
																														// pass
																														// the
																														// current
																														// line
																														// number
			System.out.println("Input File processed! - " + myWordPages.size() + " entries accepted.");
		} catch (Exception e) {
			System.out.println("Error: " + e.getLocalizedMessage()); // if there was an error in the file write process,
																		// output the error to the user
			endrun = true;
		}

	}

	/**
	 * Method to add words contained in the source file and the page they appeared
	 * on to the myWordPages object. The counts how many lines are being passed and
	 * increments the page for every 40 lines.
	 * 
	 * @param text    the line of text to be parsed for words and processed
	 * @param lineNum the current line number for the line being processed.
	 */
	private void addToWordPage(String text, int lineNum) { // Big(O) = O(1) - No loops

		// split the sentence into words
		String[] words = text.split("\\s+");

		// we consider 40 lines as a page. For each multiple of 40, up the page number.
		if (lineNum % 40 == 0) {
			currentPage++;
		}

		for (String s : words) {
			s = StripAndLower(s); // strip and convert the string to lowercase
			if (!s.isEmpty() && myDictionary.isDefined(s)) { // if the word is not empty, and the string is defined in
																// the dictionary, add the word and it's page to the
																// myWordPages object.
				myWordPages.add(s, String.valueOf(currentPage));
			}

		}
	}

	/**
	 * Method to write the index to a file. The method gathers each word contained
	 * in myWordPages and iterates through them, writing an entry on the file
	 * displaying the word, it's definition, and the number of each page it appears
	 * on.
	 */
	private void writeToFile() { // Big(O) = O(n) - defined by number of lines to be written to the file

		ArrayList<String> ar = new ArrayList<String>();
		int wordCount = 0;
		StringBuilder sb = new StringBuilder();
		String nl = new String("\n");
		ar = myWordPages.returnWords();
		try {

			File f = new File(outputFile);

			if (f.createNewFile()) {
				System.out.println("File created: " + f.getName()); // if the file needed to be created, prompt this to
																	// the user
			} else {
				System.out.println(f.getName() + " exists; overwriting"); // if the file already existed, overwrite and
																			// notify user
			}
			FileWriter fr = new FileWriter(outputFile);

			// gather and write each index entry in turn to the file
			for (String s : ar) {
				sb = new StringBuilder();
				sb.append("-------------------------------------" + nl + "Word: " + s + nl);
				sb.append("Definition: " + myDictionary.getDefinition(s) + nl);
				sb.append("Pages: " + myWordPages.getPages(s) + nl);
				fr.write(sb.toString());
				wordCount++;
			}

			fr.close();
			System.out.println("Parsing complete! - " + wordCount + " unique words parsed.");
		} catch (IOException e) {
			System.out.println("Error: " + e.getLocalizedMessage()); // if there was an error in the file write process,
																		// output the error to the user
			endrun = true;
		}

	}

	/**
	 * Method to write the index to the screen. The method gathers each word
	 * contained in myWordPages and iterates through them, writing an entry to the
	 * screen for each word, it's definition, and the number of each page it appears
	 * on.
	 */
	private void writeToScreen() { // Big(O) = O(n) - defined by number of lines to be written to the screen

		ArrayList<String> ar = new ArrayList<String>();
		int wordCount = 0;
		StringBuilder sb;
		String nl = new String("\n");

		try {
			// gather a list of stored words in the myWordPages object (used to iterate the
			// results)
			ar = myWordPages.returnWords();
			// gather and write each index entry in turn to the System.out
			for (String s : ar) {
				sb = new StringBuilder();
				sb.append("-------------------------------------" + nl + "Word: " + s + nl);
				sb.append("Definition: " + myDictionary.getDefinition(s) + nl);
				sb.append("Pages: " + myWordPages.getPages(s) + nl);
				if (++wordCount % 4 == 0) {
					System.out.println("<Enter to Continue>");
					System.in.read();
				}
				;
				System.out.println(sb.toString());
			}
			System.out.println("Parsing complete! - " + wordCount + " unique words parsed.");
		} catch (Exception e) {
			System.out.println("Error: " + e.getLocalizedMessage()); // if there was an error in the file write process,
			// output the error to the user
			endrun = true;
		}

	}

	/**
	 * A method which trims and converts a String object to lower case.
	 * 
	 * @param s the String object to be processed
	 * @return a trimmed and converted to lower case representation of the
	 *         Stringobject passed to the routine.
	 */
	private String StripAndLower(String s) { // Big(O) = O(n) - defined by number of characters in the string

		// simply assures that the string is only alphabetic and is lower case
		return s.replaceAll("[^a-zA-Z]", "").toLowerCase();

	}

}
