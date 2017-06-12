package ast;

import visitor.Visitable;
import visitor.Visitor;

public class ForInstruction extends Node implements Visitable {

	/**
	 * A for instruction node has 4 child: one for first variable
	 * initialization, one for the comparison, one for the increment and other
	 * for the instruction block.
	 */
	public ForInstruction() {
		super(4);
	}

	@Override
	public Node accept(Visitor visit) {
		return visit.visit(this);
	}

	/**
	 * A for instruction has a NO_SET numeric value
	 */
	@Override
	public int evalNumericValue() {
		setNumericValue(NO_SET);
		return NO_SET;
	}

	/**
	 * A for instruction has a true boolean value if the variable has
	 * initialized before, if the comparison and increment use initialized
	 * variables and if the instruction block boolean value is true.
	 */
	@Override
	public boolean evalBooleanValue() {

		boolean firstValue = children.get(0).getChild(0).evalBooleanValue();
		int secondValue = children.get(1).evalNumericValue();
		boolean thirdValue = children.get(2).evalBooleanValue();
		boolean fourthValue = children.get(3).evalBooleanValue();

		if (!firstValue || secondValue == NO_SET || !thirdValue || !fourthValue)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "node for";
	}

}
