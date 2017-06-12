package test;

import java.util.ArrayList;
import java.util.LinkedList;

public class StringSplit {

	/**
	 * Given a string, it creates an array list of string with the elements.
	 * Example: [* [+ 1 2] [+ 4 3]] -> * [+ 1 2] [+ 4 3]
	 * 
	 * @param s
	 *            string to be split
	 * @return array list of string
	 */
	public static ArrayList<String> splitList(String s) {

		LinkedList<String> result = new LinkedList<String>();
		if (s.length() > 2) // eliminate the first '[' and the last ']'
			s = s.substring(1, s.length() - 1);
		int inside = 0;
		int start = 0, stop = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '[') {
				inside++;
				stop++;
				continue;
			}
			if (s.charAt(i) == ']') {
				inside--;
				stop++;
				continue;
			}
			if (s.charAt(i) == ' ' && inside == 0) {
				result.add(s.substring(start, stop));
				start = i + 1; // starting after whitespace
				stop = start;

				continue;
			}
			stop++;
		}
		if (stop > start) {
			result.add(s.substring(start, stop));
		}

		return new ArrayList<String>(result);

	}
}
