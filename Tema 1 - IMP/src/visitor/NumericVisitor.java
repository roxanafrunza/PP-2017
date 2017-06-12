package visitor;

import ast.AdditionInstruction;
import ast.AssertStatement;
import ast.AssignExpression;
import ast.CompareInstruction;
import ast.EqualityOperation;
import ast.ForInstruction;
import ast.IfInstruction;
import ast.InstructionBlock;
import ast.MultiplyInstruction;
import ast.Node;
import ast.ReturnStatement;
import ast.Value;
import ast.Variable;

public class NumericVisitor implements Visitor {

	/**
	 * A Numeric visitor evaluates every node and executes the corresponding
	 * operation. If a return node is reached, its value is calculated and saved
	 * in the treeValue variable in Node class. Also, when an assert statement
	 * is visited the assertVisited becomes true. The assertValues is true if
	 * all the assert conditions passed
	 */
	@Override
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

	@Override
	public Node visit(AdditionInstruction object) {

		int firstOperand = object.getChild(0).evalNumericValue();
		int secondOperand = object.getChild(1).evalNumericValue();
		Node result = new Value(firstOperand + secondOperand);
		return result;
	}

	@Override
	public Node visit(MultiplyInstruction object) {
		int firstOperand = object.getChild(0).evalNumericValue();
		int secondOperand = object.getChild(1).evalNumericValue();
		Node result = new Value(firstOperand * secondOperand);
		return result;
	}

	@Override
	public Node visit(EqualityOperation object) {
		visit(object.getChild(0));
		visit(object.getChild(1));
		return object;
	}

	@Override
	public Node visit(CompareInstruction object) {
		visit(object.getChild(0));
		visit(object.getChild(1));
		return object;
	}

	@Override
	public Node visit(AssertStatement object) {
		Node.assertVisited();
		boolean condition = object.evalBooleanValue();
		if (condition)
			Node.assertValues = true;
		else
			Node.assertValues = false;

		return object;
	}

	@Override
	public Node visit(AssignExpression object) {
		visit(object.getChild(0));
		int value = visit(object.getChild(1)).getNumericValue();
		String name = ((Variable) object.getChild(0)).getName();
		Node.removeFromContext(name);
		Node.addToContext(name, value);
		
		return object.getChild(0);

	}

	@Override
	public Node visit(ForInstruction object) {
		visit(object.getChild(0));
		while (visit(object.getChild(1)).evalBooleanValue()) {
			visit(object.getChild(3));
			visit(object.getChild(2));
		}
		return object;
	}

	@Override
	public Node visit(IfInstruction object) {
		if (visit(object.getChild(0)).evalBooleanValue()) {
			visit(object.getChild(1));
		} else if (object.getChildren().size() > 2) {
			visit(object.getChild(2));
		}
		return object;
	}

	@Override
	public Node visit(InstructionBlock object) {
		for (int i = 0; i < object.getChildren().size(); i++)
			visit(object.getChild(i));
		return object;
	}

	@Override
	public Node visit(ReturnStatement object) {

		Node.returnVisited();
		Node result = visit(object.getChild(0));
		Node.treeValue = result.getNumericValue();
		return result;
	}

	@Override
	public Node visit(Variable object) {
		object.evalNumericValue();
		return object;
	}

	@Override
	public Node visit(Value object) {
		return object;
	}

}
