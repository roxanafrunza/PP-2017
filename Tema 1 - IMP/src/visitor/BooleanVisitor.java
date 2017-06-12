package visitor;

import ast.*;

public class BooleanVisitor implements Visitor {

	/**
	 * A boolean visitor visits every node in the tree and checks if all the
	 * variables are initialized. If a return node is visited, the returnVisited
	 * variable in Node class becomes true.
	 */
	public Node visit(Node object) {
		if (object instanceof AdditionInstruction)
			return visit((AdditionInstruction) object);
		if (object instanceof AssertStatement)
			return visit((AssertStatement) object);
		if (object instanceof AssignExpression)
			return visit((AssignExpression) object);
		if (object instanceof CompareInstruction)
			return visit((CompareInstruction) object);
		if (object instanceof EqualityOperation)
			return visit((EqualityOperation) object);
		if (object instanceof ForInstruction)
			return visit((ForInstruction) object);
		if (object instanceof IfInstruction)
			return visit((IfInstruction) object);
		if (object instanceof InstructionBlock)
			return visit((InstructionBlock) object);
		if (object instanceof MultiplyInstruction)
			return visit((MultiplyInstruction) object);
		if (object instanceof ReturnStatement)
			return visit((ReturnStatement) object);
		if (object instanceof Value)
			return visit((Value) object);
		if (object instanceof Variable)
			return visit((Variable) object);

		return null;

	}

	public Node visit(AdditionInstruction object) {
		object.evalBooleanValue();
		return object;
	}

	public Node visit(MultiplyInstruction object) {
		
		object.evalBooleanValue();
		return object;
	}

	public Node visit(EqualityOperation object) {

		object.evalNumericValue();
		return object;

	}

	public Node visit(CompareInstruction object) {
		
		object.evalNumericValue();
		return object;
	}

	public Node visit(AssertStatement object) {
		
		object.evalNumericValue();
		return object;

	}

	public Node visit(AssignExpression object) {

		object.evalBooleanValue();
		return object;

	}

	public Node visit(ForInstruction object) {

		object.evalBooleanValue();
		return object;
	}

	public Node visit(IfInstruction object) {

		object.evalBooleanValue();
		return object;

	}

	public Node visit(InstructionBlock object) {

		object.evalBooleanValue();
		return object;
	}

	public Node visit(ReturnStatement object) {

		object.evalBooleanValue();
		return object;

	}

	public Node visit(Variable object) {

		object.evalBooleanValue();
		return object;
	}

	public Node visit(Value object) {

		object.evalBooleanValue();
		return object;

	}
}
