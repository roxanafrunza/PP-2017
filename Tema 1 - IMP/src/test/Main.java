package test;

import java.io.*;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws IOException {

		// Open files
		PrintWriter writer = new PrintWriter(new File(args[1]));
		FileInputStream is = new FileInputStream(args[0]);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		// Read input line by line
		String line = null, input = "";
		while ((line = reader.readLine()) != null) {
			input = input.concat(line);
			// replace all white spaces with only one blank spaces
			// the input will be on one line
			input = input.replaceAll("\\s+", " ");
		}

		// split the string
		ArrayList<String> strings = StringSplit.splitList(input);
		// check the variables initialization
		boolean checkVariables = AbstractSyntaxTree.checkVariables(strings);
		// get the return value
		int value = AbstractSyntaxTree.getExpressionValue(strings);
		// check if assert expression exist and if they are all true
		boolean assertExists = AbstractSyntaxTree.existsAssertStatement();
		boolean assertPassed = AbstractSyntaxTree.checkAssertStatements();

		if (checkVariables == false) {
			writer.print("Check failed");
		} else {
			boolean returnStatement = AbstractSyntaxTree.checkReturnStatement();
			if (returnStatement == false) {
				writer.print("Missing return");
			} else {
				if (assertExists == true && assertPassed == false)
					writer.print("Assert failed");
				else
					writer.print(value);
			}
		}

		reader.close();
		writer.close();
	}
}
