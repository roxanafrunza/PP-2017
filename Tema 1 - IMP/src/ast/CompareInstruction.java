package ast;

import visitor.Visitable;
import visitor.Visitor;

public class CompareInstruction extends Node implements Visitable {

	/**
	 * A compare instruction always has two children
	 */
	public CompareInstruction() {
		super(2);
	}

	@Override
	public Node accept(Visitor visit) {
		return visit.visit(this);
	}

	/**
	 * The numeric value for a compare node is NO_SET if the the members contain
	 * variables that aren't initialed, 0 otherwise
	 */
	@Override
	public int evalNumericValue() {

		if (children.get(0).evalBooleanValue() && children.get(1).evalBooleanValue()) {
			setNumericValue(0);
			return 0;
		}
		setNumericValue(NO_SET);
		return NO_SET;
	}

	/**
	 * Returns true if the first child has a smaller numeric value than the
	 * second child
	 */
	@Override
	public boolean evalBooleanValue() {
		boolean value = (children.get(0).evalNumericValue() < children.get(1).evalNumericValue());

		setBooleanValue(value);
		return value;

	}

	@Override
	public String toString() {
		return "node <";
	}

}
