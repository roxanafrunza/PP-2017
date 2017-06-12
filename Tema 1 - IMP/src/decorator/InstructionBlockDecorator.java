package decorator;

import java.util.ArrayList;
import test.*;
import ast.*;

public class InstructionBlockDecorator extends ExpressionDecorator {

	public InstructionBlockDecorator(Node expression) {
		super(expression);
	}

	@Override
	public Node createExpression(ArrayList<String> elements) {

		// create an instruction block node
		Node object = new InstructionBlock(elements.size());

		ArrayList<String> childrenNode;
		Node child;
		// for every element in the array list of string
		for (int i = 1; i < elements.size(); i++) {
			// create the corresponding node
			childrenNode = StringSplit.splitList(elements.get(i));
			child = Node.createExpression(childrenNode);
			// connect node to father
			object.setChildren(child);
		}

		return object;

	}

}
