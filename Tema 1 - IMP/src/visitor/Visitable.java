package visitor;

import ast.Node;

public interface Visitable {
	public Node accept(Visitor visit);


}
