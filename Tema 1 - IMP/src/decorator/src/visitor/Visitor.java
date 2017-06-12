package visitor;

import ast.*;

public interface Visitor {
	
	public Node visit(Node object);

	public Node visit(AdditionInstruction object);

	public Node visit(MultiplyInstruction object);

	public Node visit(EqualityOperation object);

	public Node visit(CompareInstruction object);

	public Node visit(AssertStatement object);

	public Node visit(AssignExpression object);

	public Node visit(ForInstruction object);

	public Node visit(IfInstruction object);

	public Node visit(InstructionBlock object);

	public Node visit(ReturnStatement object);

	public Node visit(Variable object);

	public Node visit(Value object);
}
