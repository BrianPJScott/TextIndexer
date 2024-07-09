package ie.atu.sw;

/**
 * @author Brian Scott
 * @version 1.0
 * @since 19.0.1
 *
 *          A simple runner class for the indexer application. Contains the main
 *          method for the application which simply insatntiates a menu object
 *          and calls it's show() method.
 *
 */
public class Runner { // Big(O) = O(1) - No loops

	public static void main(String[] args) throws Exception {

		// Create a menu object and show it.
		Menu myMenu = new Menu();
		myMenu.show();

	}

}