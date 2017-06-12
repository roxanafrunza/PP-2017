package ast;

import visitor.Visitable;
import visitor.Visitor;

public class InstructionBlock extends Node implements Visitable {
	/**
	 * An instruction block node can have a variable number of children
	 * 
	 * @param count
	 *            number of children
	 */
	public InstructionBlock(int count) {
		super(count);
	}

	@Override
	public Node accept(Visitor visit) {
		return visit.visit(this);
	}

	/**
	 * An instruction block always has a numeric NO_SET value. It also evaluate
	 * the numericValue for all its children
	 */
	@Override
	public int evalNumericValue() {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).evalNumericValue();
		}
		return NO_SET;
	}

	/**
	 * An instruction block has a true boolean value if all its children have
	 * the boolean value true (or numeric value is 0 for the assert,
	 * compare and equality node)
	 */
	@Override
	public boolean evalBooleanValue() {
		for (int i = 0; i < children.size(); i++) {
			Node child = children.get(i);
			if (child instanceof AssertStatement || child instanceof CompareInstruction
					|| child instanceof EqualityOperation) {
				if (child.evalNumericValue() == NO_SET) {
					this.setBooleanValue(false);
					return false;
				}
			} else if (!children.get(i).evalBooleanValue()) {
				this.setBooleanValue(false);
				return false;
			}
		}
		this.setBooleanValue(true);
		return true;
	}

	@Override
	public String toString() {
		return "node block";
	}
}
