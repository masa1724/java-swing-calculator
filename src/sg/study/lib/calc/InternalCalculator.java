package sg.study.lib.calc;

import java.math.BigDecimal;
import java.util.List;

import sg.study.lib.calc.node.FormulaNodeWrapper;
import sg.study.lib.calc.node.NumberNode;
import sg.study.lib.calc.node.OperatorNode;

/**
 * �d��̌v�Z�����̎����N���X
 * 
 */
// ���������J�ɂ���ׁA�p�b�P�[�W�A�N�Z�X
class InternalCalculator {
	private final CalculateHistoryManager historyMng;
	
	public InternalCalculator(CalculateHistoryManager historyMng) {
		this.historyMng = historyMng;
	}
	
	public BigDecimal calculate(List<FormulaNodeWrapper> nodes) {
		if (nodes.isEmpty()) return BigDecimal.ZERO;
		
		BigDecimal firstNodeVal = nodes.get(0).getNumberNode().getNumber();
		if (nodes.size() == 1) return firstNodeVal;
		
		BigDecimal result = firstNodeVal;
		
		for (int i = 1; i < nodes.size(); i+=2) {
			OperatorNode on = nodes.get(i).getOperatorNode();
			NumberNode nn = nodes.get(i+1).getNumberNode();
			
			Operator operator = on.getOperator();
			BigDecimal number2 = nn.getNumber();
			result = operator.calculate(result, number2);
			
			historyMng.addHistory(on, nn);
		}
		
		return result;
	}
}
