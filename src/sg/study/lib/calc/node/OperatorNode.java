package sg.study.lib.calc.node;

import sg.study.lib.calc.Operator;

public class OperatorNode {
	private Operator operator;
	
	public OperatorNode(Operator operator) {
		this.operator = operator;
	}
	
	public Operator getOperator() {
		return operator;
	}
	
	public String toString() {
		return operator.getSymbol();
	}
}
