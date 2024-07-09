package ie.atu.sw;

import java.util.Scanner;

/**
 * @author Brian Scott
 * @version 1.0
 * @since 19.0.1
 * 
 *        The menu object generates an interface for the user to engage with the
 *        application. The menu allows the user to sets variables and passes
 *        these variables to the parser object. The menu also runs the parser
 *        object. The variables which can be set by the user are also set to
 *        default values for ease of use in a default situation. The menu will
 *        loop until the user chooses to quit the application.
 * 
 */
public class Menu {

	private Scanner scMenu, scInput; // scanners for user input
	private Parser myParser; // parser object
	private String textFile; // location of source text file
	private String dictionary; // location of dictionary
	private String commonWords; // location of exclusions
	private String outputFile; // location of output file

	/**
	 * Constructor class for the Menu object. Sets default variable values and
	 * initiates scanner objects for input.
	 */
	public Menu() { // Big(O) = O(1) - Constructor

		// sets the default values for the variables essential to the application
		// ToDo: enter into config file and read in instead of hard-coding
		outputFile = System.getProperty("user.dir") + "\\Output.txt";
		textFile = System.getProperty("user.dir") + "\\BibleGod.txt";
		dictionary = System.getProperty("user.dir") + "\\dictionary.csv";
		commonWords = System.getProperty("user.dir") + "\\google-1000.txt";

		// instantiate scanners
		scMenu = new Scanner(System.in);
		scInput = new Scanner(System.in);

	}

	/**
	 * Displays the menu to the user and holds for the user to choose an option.
	 * Allows the user to set the values of variables for the parser and allows
	 * execution of the parser.
	 */
	public void show() { // Big(O) = O(n), where n = number of iterations chosen by user

		/*
		 * This show function will display the menu and prompt the user for a choice
		 * (int) The menu will also display the current settings for each choice These
		 * settings can be updated by the user through the menu options
		 */

		int choice = 0;

		while (choice != 7) {
			//clearScreen(); // clears the screen (Note: chose to remove as it only output characters in runtime)
			scMenu = new Scanner(System.in);
			choice = 0;

			System.out.println(ConsoleColour.WHITE);
			System.out.println("************************************************************");
			System.out.println("*       ATU - Dept. Computer Science & Applied Physics     *");
			System.out.println("*                                                          *");
			System.out.println("*              Virtual Threaded Text Indexer               *");
			System.out.println("*                                                          *");
			System.out.println("************************************************************");

			System.out.println("(1) Configure Text File \t(Currently: " + textFile + ")");
			System.out.println("(2) Configure Dictionary \t(Currently: " + dictionary + ")");
			System.out.println("(3) Configure Common Words \t(Currently: " + commonWords + ")");
			System.out.println("(4) Specify Output File \t(Currently: " + outputFile + ")");
			System.out.println("(5) Execute & Write to Output File");
			System.out.println("(6) Execute & Print to Screen");
			System.out.println("(7) Quit");

			// Output a menu of options and solicit text from the user
			System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
			System.out.print("Select Option [1-7]>");
			System.out.println();

			try {
				choice = scMenu.nextInt(); // Prompts the user for an integer stating their choice
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage()); // output error if encountered
			}

			switch (choice) {
			case 1 -> setTextFile(); // redefine source text file
			case 2 -> setDictionary(); // redefine dictionary location
			case 3 -> setCommonWords(); // redefine exclusions file location
			case 4 -> setOutputFile(); // redefine output file location
			case 5 -> runParserFile(); // run the parser and output to file
			case 6 -> runParserScreen(); // run the parser and output to screen
			case 7 -> System.out.println("Quitting."); // quit
			default -> System.out.println("Invalid Selection!"); // invalid option
			}
		}

	}

	/**
	 * Sets the textfile variable. Textfile is the file for which the index is to be
	 * generated.
	 */
	private void setTextFile() { // Big(O) = O(1) - No loops

		// prompts user for path to the source file for processing
		System.out.print("Input File (incl. Directory): ");
		String input = scInput.nextLine();
		textFile = input;
		System.out.print("Input File set to: " + textFile);
	}

	/**
	 * Sets the dictionary variable. Dictionary is the dictionary for which each
	 * word in the textfile is to be defined.
	 */
	private void setDictionary() { // Big(O) = O(1) - No loops

		// prompts user for path to the dictionary file
		System.out.print("Dictionary File (incl. Directory): ");
		String input = scInput.nextLine();
		dictionary = input;
		System.out.print("Dictionary File set to: " + dictionary);
	}

	/**
	 * Sets the commonWords variable. CommonWords is a file containing a list of
	 * words to be excluded from the index.
	 */
	private void setCommonWords() { // Big(O) = O(1) - No loops

		// prompts user for path to the exclusions file
		System.out.print("Common Words File (incl. Directory): ");
		String input = scInput.nextLine();
		commonWords = input;
		System.out.print("Common Words File set to: " + commonWords);
	}

	/**
	 * Sets the outputFile variable. OutputFile is a file location where the user
	 * wishes to output the results of the parser to.
	 */
	private void setOutputFile() { // Big(O) = O(1) - No loops

		// prompts user for path to the output file
		System.out.print("Output File (incl. Directory): ");
		String input = scInput.nextLine();
		outputFile = input;
		System.out.print("Output File set to: " + outputFile);

	}

	/**
	 * Creates a new Parser object, passing the required constructor arguments to
	 * the new object. Sets the parser object to output to the defined file.
	 */
	private void runParserFile() { // Big(O) = O(1) - No loops

		// instantiates the Parser object and passes the required arguments
		// runs the parser, outputting to the designated file
		myParser = new Parser(outputFile, textFile, dictionary, commonWords);
		myParser.run(false);
	}

	/**
	 * Creates a new Parser object, passing the required constructor arguments to
	 * the new object. Sets the parser object to output to the the console.
	 */
	private void runParserScreen() { // Big(O) = O(1) - No loops

		// instantiates the Parser object and passes the required arguments
		// runs the parser, outputting to the user's System.out
		myParser = new Parser(outputFile, textFile, dictionary, commonWords);
		myParser.run(true);
	}

	/**
	 * Method to clear the user console/screen
	 * <i>Note: not currently used as only outputs characters to console</i>
	 */
	@SuppressWarnings("unused")
	private static void clearScreen() { // Big(O) = O(1) - No loops

		// Clears the screen for the user
		System.out.println("\033[H\033[2J");
		System.out.flush();
	}
}
