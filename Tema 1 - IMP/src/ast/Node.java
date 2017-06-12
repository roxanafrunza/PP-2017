package ast;

import java.util.*;

import decorator.*;
import visitor.*;

public abstract class Node implements Visitable {
	ArrayList<Node> children; // a node's children
	// context to save all initialized variables
	static HashMap<String, Integer> context = new HashMap<String, Integer>();
	int numericValue; // every node has a numericValue ; it's calculated
						// according to the operation
	boolean booleanValue; // every node has a booleanValue; it's true if
							// all the variables are initialized
	public static boolean returnVisited; // variable to see if a return
											// statement exists
	public static boolean assertVisited; // variable to see if assert
											// statements exist
	public static boolean assertValues; // variable to see if all assert
										// statements are true

	public static int treeValue; // variable to save the final tree value

	public static final int NO_SET = -424242; // constant for uninitialized
												// variables

	/**
	 * Create a node with count children
	 * 
	 * @param count
	 *            number of children
	 */
	public Node(int count) {
		this.children = new ArrayList<Node>(count);
	}

	/**
	 * Create a node based on array of strings. Creating a node is done using
	 * the Expression Decorator
	 * 
	 * @param elements
	 *            array of strings
	 * @return a node
	 */
	public static Node createExpression(ArrayList<String> elements) {
		Node aux = new Value("0");
		Node expr = new ExpressionDecorator(aux).createExpression(elements);
		return expr;

	}

	/**
	 * 
	 * @return current node's children
	 */
	public ArrayList<Node> getChildren() {
		return this.children;
	}

	/**
	 * Add children to current node
	 * 
	 * @param n
	 *            node
	 */
	public void setChildren(Node n) {
		children.add(n);
	}

	public Node getChild(int i) {
		return children.get(i);
	}

	/**
	 * Evaluates the current node's numeric value
	 * 
	 * @return numeric value
	 */
	public abstract int evalNumericValue();

	/**
	 * Evaluates the current node's boolean value. The node's boolean value is
	 * true if all its children's boolean value are true.
	 * 
	 * @return boolean value
	 */
	public abstract boolean evalBooleanValue();

	public void setNumericValue(int value) {
		this.numericValue = value;
	}

	public void setBooleanValue(boolean value) {
		this.booleanValue = value;
	}

	public boolean getBooleanValue() {
		return this.booleanValue;
	}

	public int getNumericValue() {
		return this.numericValue;
	}

	/**
	 * When a variables is assigned, it's added to context
	 * 
	 * @param name
	 *            variable's name
	 * @param value
	 *            variable's value
	 */
	public static void addToContext(String name, int value) {
		context.put(name, value);
	}

	/**
	 * Remove variable from context
	 * 
	 * @param name
	 */
	public static void removeFromContext(String name) {
		context.remove(name);
	}

	@Override
	public Node accept(Visitor visit) {
		return visit.visit(this);
	}

	/**
	 * A return node was visited
	 */
	public static void returnVisited() {
		returnVisited = true;
	}

	/**
	 * An assert node was visited
	 */
	public static void assertVisited() {
		assertVisited = true;
	}
}
