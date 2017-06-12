package ast;

import visitor.Visitable;
import visitor.Visitor;

public class AdditionInstruction extends Node implements Visitable {
	int value;

	/**
	 * Addition node will always have 2 children: the two operands.
	 */
	public AdditionInstruction() {
		super(2);
	}

	@Override
	public Node accept(Visitor visit) {
		return visit.visit(this);
	}

	/**
	 * The numeric value for addition node is NO_SET.
	 */
	@Override
	public int evalNumericValue() {
		int firstValue = getChild(0).evalNumericValue();
		int secondValue = getChild(1).evalNumericValue();
		setNumericValue(NO_SET);
		if(firstValue == NO_SET || secondValue == NO_SET)
			return NO_SET;
		return firstValue + secondValue;
	}

	/**
	 * The boolean value for addition node is true if both children have boolean
	 * value true
	 */
	@Override
	public boolean evalBooleanValue() {
		boolean firstValue = getChild(0).evalBooleanValue();
		boolean secondValue = getChild(1).evalBooleanValue();
		if (firstValue && secondValue) {
			setBooleanValue(true);
			return true;
		}
		setBooleanValue(false);
		return false;
	}

	@Override
	public String toString() {
		return " Node + ";
	}

}
