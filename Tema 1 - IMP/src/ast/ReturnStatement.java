package ast;

import visitor.Visitable;
import visitor.Visitor;

public class ReturnStatement extends Node implements Visitable {

	/**
	 * The return node always has one child: can be a variable or an operation
	 */
	public ReturnStatement() {
		super(1);
	}

	@Override
	public Node accept(Visitor visit) {
		return visit.visit(this);
	}

	/**
	 * The return node always has the numeric value NO_SET
	 */
	@Override
	public int evalNumericValue() {
		return NO_SET;
	}

	/**
	 * The boolean value for the return node is the boolean value of the son
	 */
	@Override
	public boolean evalBooleanValue() {
		this.setBooleanValue(getChild(0).evalBooleanValue());
		return this.getChild(0).evalBooleanValue();
	}

	@Override
	public String toString() {
		return "node return";
	}
}
