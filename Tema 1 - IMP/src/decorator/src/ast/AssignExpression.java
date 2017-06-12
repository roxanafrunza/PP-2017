package ast;

import visitor.Visitable;
import visitor.Visitor;

public class AssignExpression extends Node implements Visitable {

	/**
	 * The assign node always has two children: a variables and an expression
	 */
	public AssignExpression() {
		super(2);
	}

	@Override
	public Node accept(Visitor visit) {
		return visit.visit(this);
	}

	/**
	 * The assign node always has the NO_SET numeric value. It adds to the
	 * context the variable which is always the first child. The variable's
	 * value is the second child after it has been evaluated.
	 */
	@Override
	public int evalNumericValue() {
		int value = children.get(1).evalNumericValue();
		if (value != NO_SET) {
			children.get(0).setNumericValue(value);
			children.get(0).setBooleanValue(true);
			addToContext(((Variable) children.get(0)).getName(), value);
		}
		return NO_SET;
	}

	/**
	 * The assign node will have the boolean value true if the left member has
	 * the boolean value true (all the variables used are initialized)
	 */
	@Override
	public boolean evalBooleanValue() {
		evalNumericValue();
		
		boolean value = children.get(1).evalBooleanValue();
		children.get(0).setBooleanValue(value);
		this.setBooleanValue(value);
		//children.get(0).evalNumericValue();
		
		System.out.println("ASSIGN <3");
		System.out.println("Copil 0 " + children.get(0));
		System.out.println(children.get(0).getBooleanValue());
		System.out.println(children.get(1));
		System.out.println("Val COPIL " + children.get(1).getBooleanValue());


		return value;
	}

	@Override
	public String toString() {
		return "node asign";
	}

}
