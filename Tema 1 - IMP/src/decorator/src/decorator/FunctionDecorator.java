package decorator;

import java.util.ArrayList;
import ast.*;
import test.*;

public class FunctionDecorator extends ExpressionDecorator {

	public FunctionDecorator(Node expression) {
		super(expression);
	}

	@Override
	public Node createExpression(ArrayList<String> elements) {
		Node object = null;

		String operator = elements.get(0);

		// create a for instruction node
		if (operator.equals("for")) {
			// create root
			object = new ForInstruction();

			// get lists of elements for all the children
			ArrayList<String> childrenAssignNode = StringSplit.splitList(elements.get(1));
			ArrayList<String> childrenCompareNode = StringSplit.splitList(elements.get(2));
			ArrayList<String> childrenIncrementNode = StringSplit.splitList(elements.get(3));
			ArrayList<String> childrenInstructionsNode = StringSplit.splitList(elements.get(4));

			// create node based on the lists of elements
			Node firstChild = Node.createExpression(childrenAssignNode);
			Node secondChild = Node.createExpression(childrenCompareNode);
			Node thirdChild = Node.createExpression(childrenIncrementNode);
			Node fourthChild = Node.createExpression(childrenInstructionsNode);

			// connect children to the father node
			object.setChildren(firstChild);
			object.setChildren(secondChild);
			object.setChildren(thirdChild);
			object.setChildren(fourthChild);
		}
		// create a if instruction node
		else if (operator.equals("if")) {
			// create root
			object = new IfInstruction();

			// get lists of elements for the first two children
			ArrayList<String> childrenConditionNode = StringSplit.splitList(elements.get(1));
			ArrayList<String> childrenTrueNode = StringSplit.splitList(elements.get(2));

			// create the two nodes based on the elements
			Node firstChild = Node.createExpression(childrenConditionNode);
			Node secondChild = Node.createExpression(childrenTrueNode);

			// connect children to the father node
			object.setChildren(firstChild);
			object.setChildren(secondChild);

			// if it exists an else condition, create children
			if (elements.size() > 2) {
				ArrayList<String> childrenFalseNode = StringSplit.splitList(elements.get(3));
				Node thirdChild = Node.createExpression(childrenFalseNode);
				object.setChildren(thirdChild);
			}

		}
		return object;

	}

}
