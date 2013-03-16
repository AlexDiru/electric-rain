package xml;

/**
 * Contains functions to help with XML Parser
 * @author Alex
 *
 */
public abstract class XMLHelper {
	/**
	 * Parses an int from string
	 * @param text String to parse
	 * @return The number or -1
	 */
	public static int parseInt(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException ex) {
			return -1;
		}
	}

	/**
	 * Converts a string of CSV ints into an int array 1,2,34,6 => {1, 2, 34, 6}
	 * 
	 * @param text
	 *            The string to parse
	 * @return The array
	 */
	public static int[] parseCSVIntString(String text) {
		String[] textSplit = text.split("\\,");
		int[] arr = new int[textSplit.length];
		for (int i = 0; i < textSplit.length; i++)
			arr[i] = parseInt(textSplit[i]);
		return arr;
	}
}
