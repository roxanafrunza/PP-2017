package decorator;

import java.util.ArrayList;
import ast.*;

public class ExpressionDecorator {
	protected Node expression;

	public ExpressionDecorator(Node expression) {
		this.expression = expression;
	}

	/**
	 * Creates a node based on an array elements of strings
	 * 
	 * @param elements
	 *            array list of strings
	 * @return created node
	 */
	public Node createExpression(ArrayList<String> elements) {
		return decorateExpression(elements);
	}

	/**
	 * Based on the first elements from the array list (which is a operation, an
	 * instruction, a variable or a value), create the corresponding node
	 * 
	 * @param elements
	 * @return
	 */
	public Node decorateExpression(ArrayList<String> elements) {

		switch (elements.get(0)) {
		case ("=="):
			return new BinaryDecorator(expression).createExpression(elements);
		case ("="):
			return new BinaryDecorator(expression).createExpression(elements);
		case ("+"):
			return new BinaryDecorator(expression).createExpression(elements);
		case ("*"):
			return new BinaryDecorator(expression).createExpression(elements);
		case ("<"):
			return new BinaryDecorator(expression).createExpression(elements);
		case ("for"):
			return new FunctionDecorator(expression).createExpression(elements);
		case ("if"):
			return new FunctionDecorator(expression).createExpression(elements);
		case ("return"):
			return new StatementDecorator(expression).createExpression(elements);
		case ("assert"):
			return new StatementDecorator(expression).createExpression(elements);
		case (";"):
			return new InstructionBlockDecorator(expression).createExpression(elements);
		default:
			if (elements.get(0).matches("[0-9]+")) {
				return new Value(elements.get(0));
			} else if (elements.get(0).matches("[a-zA-Z]+")) {
				return new Variable(elements.get(0));
			}

		}
		throw new IllegalArgumentException("Operation not recognized");
	}

}
