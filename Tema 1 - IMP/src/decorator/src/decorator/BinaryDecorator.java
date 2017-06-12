package decorator;

import java.util.ArrayList;

import ast.*;
import test.*;

public class BinaryDecorator extends ExpressionDecorator {

	public BinaryDecorator(Node expression) {
		super(expression);
	}

	/**
	 * Creates a node with two children. Can be: equality operation, assign
	 * expression, addition multiply or compare instruction. The array list will
	 * have the first element the operation type and the other two elements the
	 * lists for two children.
	 */
	@Override
	public Node createExpression(ArrayList<String> elements) {
		Node object = null;
		// create corresponding node
		switch (elements.get(0)) {
		case ("=="):
			object = new EqualityOperation();
			break;
		case ("="):
			object = new AssignExpression();
			break;
		case ("+"):
			object = new AdditionInstruction();
			break;
		case ("*"):
			object = new MultiplyInstruction();
			break;
		case ("<"):
			object = new CompareInstruction();
			break;
		default:
			throw new IllegalArgumentException("Not a binary operator!");
		}

		// create the list of elements for the two children
		ArrayList<String> childrenFirstNode = StringSplit.splitList(elements.get(1));
		ArrayList<String> childrenSecondNode = StringSplit.splitList(elements.get(2));

		// create the two nodes based on the array list of elements
		Node firstChild = Node.createExpression(childrenFirstNode);
		Node secondChild = Node.createExpression(childrenSecondNode);

		// connect the two nodes to the father node
		object.setChildren(firstChild);
		object.setChildren(secondChild);

		return object;
	}
}
