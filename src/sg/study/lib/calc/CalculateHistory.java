package sg.study.lib.calc;

import sg.study.lib.calc.node.NumberNode;
import sg.study.lib.calc.node.OperatorNode;

/**
 * �v�Z���ʂ̗����Ǘ���\���B
 *
 */
// ���������J�ɂ���ׁA�p�b�P�[�W�A�N�Z�X
class CalculateHistory {
	private OperatorNode on;
	private NumberNode nn;
	
	public CalculateHistory(OperatorNode on, NumberNode nn) {
		this.on = on;
		this.nn = nn;
	}
		
	public OperatorNode getOperatorNode() {
		return on;
	}
	
	public NumberNode getNumberNode() {
		return nn;
	}
}
