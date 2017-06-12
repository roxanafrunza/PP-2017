package decorator;

import java.util.ArrayList;

import ast.*;
import test.StringSplit;

public class StatementDecorator extends ExpressionDecorator {

	public StatementDecorator(Node expression) {
		super(expression);
	}

	@Override
	public Node createExpression(ArrayList<String> elements) {
		Node object = null;

		switch (elements.get(0)) {

		case ("assert"): // create an assert statement
			object = new AssertStatement();
			break;
		case ("return"): // create a return statement
			object = new ReturnStatement();
			break;
		}

		// create the child node
		ArrayList<String> childrenFirstNode = StringSplit.splitList(elements.get(1));
		Node firstChild = Node.createExpression(childrenFirstNode);

		// connect the child to the father node
		object.setChildren(firstChild);

		return object;

	}

}
