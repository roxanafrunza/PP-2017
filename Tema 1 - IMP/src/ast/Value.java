package ast;

import visitor.Visitable;
import visitor.Visitor;

public class Value extends Node implements Visitable {

	public Value(String value) {
		super(0);
		super.numericValue = Integer.parseInt(value);
	}
	public Value(int value) {
		super(0);
		super.numericValue = value;
	}
	@Override
	public Node accept(Visitor visit) {
		return visit.visit(this);
	}

	@Override
	public int evalNumericValue() {
		return numericValue;
	}

	/**
	 * A value always has the boolean value true
	 */
	@Override
	public boolean evalBooleanValue() {
		booleanValue = true;
		return true;
	}

	@Override
	public String toString() {
		return "node value " + numericValue;
	}

}
