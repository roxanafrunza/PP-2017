package test;

import java.util.ArrayList;
import ast.*;
import visitor.*;

public class AbstractSyntaxTree {

	private static boolean hasReturn;
	private static boolean checkPassed;
	private static boolean assertPassed;
	private static boolean assertExists;

	public static Node createTree(ArrayList<String> elements) {
		return Node.createExpression(elements);
	}

	public static boolean checkVariables(ArrayList<String> elements) {
		Node tree = createTree(elements);
		tree = tree.accept(new BooleanVisitor());
		//visitNode(tree);
		//tree = tree.accept(new NumericVisitor());
		checkPassed = tree.getBooleanValue();
		return checkPassed;
	}

	public static boolean checkReturnStatement() {

		return hasReturn;
	}

	public static boolean existsAssertStatement() {
		return assertExists;
	}

	public static boolean checkAssertStatements() {
		return assertPassed;
	}

	public static int getExpressionValue(ArrayList<String> elements) {
		Node tree = createTree(elements);
		tree = tree.accept(new NumericVisitor());
		assertExists = Node.assertVisited;
		assertPassed = Node.assertValues;
		hasReturn = Node.returnVisited;
		return Node.treeValue;
	}

	public static void visitNode(Node node) {

		for (int i = 0; i < node.getChildren().size(); i++) {
			System.out.println(node.getChild(i));
			System.out.println("Valoare booleana: " + node.getChild(i).evalBooleanValue());
			visitNode(node.getChild(i));
		}
	}
}
