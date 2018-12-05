package sg.study.lib.calc.event;

import java.util.List;

import sg.study.lib.calc.node.FormulaNodeWrapper;

public class FormulaChangeEvent {
	private List<FormulaNodeWrapper> nodes;
	
	public FormulaChangeEvent(List<FormulaNodeWrapper> nodes) {
		this.nodes = nodes;
	}
	
	public List<FormulaNodeWrapper> getFormulaNodes() {
		return nodes;
	}
}
