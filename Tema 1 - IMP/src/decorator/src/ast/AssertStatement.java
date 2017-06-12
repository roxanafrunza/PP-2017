package ast;

import visitor.*;

public class AssertStatement extends Node implements Visitable {

	/**
	 * The assert statement will have only one child: the condition
	 */
	public AssertStatement() {
		super(1);
	}

	@Override
	public Node accept(Visitor visit) {
		return visit.visit(this);
	}

	/**
	 * The assert numeric value is NO_SET if the condition uses uninitialized
	 * variables. Otherwise, the numeric value is 0.
	 */
	@Override
	public int evalNumericValue() {
		if (children.get(0).evalNumericValue() == NO_SET) {
			setNumericValue(NO_SET);
			return NO_SET;
		}
		setNumericValue(0);
		return 0;
	}

	/**
	 * The assert boolean value is true if the condition is true.
	 */
	@Override
	public boolean evalBooleanValue() {
		boolean value = children.get(0).evalBooleanValue();
		setBooleanValue(value);
		return value;
	}

	@Override
	public String toString() {
		return "node assert";
	}

}
