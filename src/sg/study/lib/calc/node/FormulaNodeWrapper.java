package sg.study.lib.calc.node;

public class FormulaNodeWrapper {
	private NumberNode nn;
	private OperatorNode on;
	
	public FormulaNodeWrapper(NumberNode nn, OperatorNode on) {
		this.nn = nn;
		this.on = on;
	}
	
	public boolean isNumberNode() {
		return nn != null;
	}
	
	public boolean isOperatorNode() {
		return on != null;
	}
	
	public NumberNode getNumberNode() {
		return nn;
	}
	
	public OperatorNode getOperatorNode() {
		return on;
	}
	
	public String toString() {
		if (isNumberNode()) return "(" + nn.toString() + ")";
		if (isOperatorNode()) return "(" + on.toString() + ")";
		return "()";
	}
}
