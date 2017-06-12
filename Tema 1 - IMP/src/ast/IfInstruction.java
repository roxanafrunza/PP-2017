package ast;

import visitor.Visitable;
import visitor.Visitor;

public class IfInstruction extends Node implements Visitable {

	/**
	 * An if instruction node has three children: the condition, the block to be
	 * executed if the condition is true and the block to be executed if the
	 * condition is false (this one can be missing)
	 */
	public IfInstruction() {
		super(3);
	}

	@Override
	public Node accept(Visitor visit) {
		return visit.visit(this);
	}

	/**
	 * An if instruction has a 0 numeric value if all the children are true
	 * (they use initialized variables). Otherwise, its value is NO_SET.
	 */
	@Override
	public int evalNumericValue() {
		int firstValue = children.get(0).evalNumericValue();
		boolean secondValue = children.get(1).evalBooleanValue();

		if (firstValue != NO_SET && secondValue) {
			setNumericValue(0);
			return 0;
		}

		if (children.size() > 2) {
			boolean thirdValue = children.get(2).evalBooleanValue();
			if (firstValue != NO_SET && secondValue && thirdValue) {
				setNumericValue(0);
				return 0;
			}
		}

		setNumericValue(NO_SET);
		return NO_SET;
	}

	/**
	 * An if instruction has a true boolean value if all the children are true
	 * (they use initialized variables).
	 */
	@Override
	public boolean evalBooleanValue() {
		int firstValue = children.get(0).evalNumericValue();
		boolean secondValue = children.get(1).evalBooleanValue();

		if (children.size() > 2) {
			boolean thirdValue = children.get(2).evalBooleanValue();
			if (firstValue != NO_SET && secondValue && thirdValue) {
				setNumericValue(0);
				return true;
			}
		} else if (children.size() == 2) {
			if (firstValue != NO_SET && secondValue) {
				setNumericValue(0);
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return "node if";
	}
}
