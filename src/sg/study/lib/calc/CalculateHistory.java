package sg.study.lib.calc;

import sg.study.lib.calc.node.NumberNode;
import sg.study.lib.calc.node.OperatorNode;

/**
 * 計算結果の履歴管理を表す。
 *
 */
// 実装を非公開にする為、パッケージアクセス
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
