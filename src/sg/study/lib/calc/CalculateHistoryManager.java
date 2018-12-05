package sg.study.lib.calc;

import java.util.ArrayList;
import java.util.List;

import sg.study.lib.calc.node.NumberNode;
import sg.study.lib.calc.node.OperatorNode;

/**
 * �v�Z���ʂ̗����Ǘ����s���B
 *
 */
// ���������J�ɂ���ׁA�p�b�P�[�W�A�N�Z�X
class CalculateHistoryManager {
	private List<CalculateHistory> historys; 

	public CalculateHistoryManager() {
		init();
	}
	
	private void init() {
		historys = new ArrayList<>();
	}
	
	public void clear() {
		init();
	}
	
	public boolean isEmpty() {
		return historys.isEmpty();
	}
	
	public void addHistory(OperatorNode on, NumberNode nn) {
		historys.add(new CalculateHistory(on, nn));
	}
	
	public CalculateHistory getLastHistory() {
		return historys.get(historys.size() - 1);
	}
}
