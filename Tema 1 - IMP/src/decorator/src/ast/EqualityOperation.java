package ast;

import visitor.Visitable;
import visitor.Visitor;

public class EqualityOperation extends Node implements Visitable {

	/**
	 * An equality operation node always has two children
	 */
	public EqualityOperation() {
		super(2);
	}

	@Override
	public Node accept(Visitor visit) {
		return visit.visit(this);
	}

	/**
	 * The numeric value for a equality node is NO_SET if the the members
	 * contain variables that aren't initialed, 0 otherwise
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
	 * Returns true if the first child's numeric value is equal to the second
	 * child's numeric value
	 */
	@Override
	public boolean evalBooleanValue() {
		boolean value = children.get(0).evalNumericValue() == children.get(1).evalNumericValue();

		setBooleanValue(value);
		return value;

	}

	@Override
	public String toString() {
		return "node ==";
	}
}
