package sg.study.lib.calc;

import java.math.BigDecimal;
import java.util.List;

import sg.study.lib.calc.event.EntryChangeEventListener;
import sg.study.lib.calc.event.FormulaChangeEventListener;
import sg.study.lib.calc.node.FormulaNodeWrapper;
import sg.study.lib.calc.node.NumberNode;
import sg.study.lib.calc.node.OperatorNode;

/**
 * Calculator�̎����N���X
 * �r���[���Ƃ̂��Ƃ�ɂ��ẮA�������ׂĂ��̃N���X���o�R���čs���B
 *
 */
// ���������J�ɂ���ׁA�p�b�P�[�W�A�N�Z�X
class CalculatorImpl implements Calculator {
	private FormulaManager formulaMng = new FormulaManager();
	private EntryManager entryMng = new EntryManager();
	
	private CalculateHistoryManager historyMng = new CalculateHistoryManager();
	private InternalCalculator calculator = new InternalCalculator(historyMng);
	
	private boolean lastInputedOperator = false;
	
	
	//---
	// �G���g���[�A�����̃N���A
	//---
	
	@Override
	public void clearEntry() {
		entryMng.clear();
	}

	@Override
	public void clear() {
		entryMng.clear();
		formulaMng.clear();
		historyMng.clear();
		lastInputedOperator = false;
	}

	@Override
	public void deleteEntryRight() {
		if (lastInputedOperator) return;
		entryMng.deleteRight();
	}
	
	
	//---
	// �G���g���[
	//---
	
	@Override 
	public Calculator inputEntry(Entry entry) {
		if (lastInputedOperator) entryMng.clear();
		entryMng.inputEntry(entry);
		lastInputedOperator = false;
		return this;
	}

	@Override
	public Calculator setEntryNumber(BigDecimal number) {
		entryMng.setNumber(number);
		lastInputedOperator = false;
		return this;
	}

	@Override
	public Calculator reverseEntrySign() {
		entryMng.reverseSign();
		return this;
	}
	
	
	//---
	// ���Z
	//---
	
	@Override 
	public Calculator inputOperator(Operator operator) {
		if (lastInputedOperator) {
			formulaMng.changeLastNode(new OperatorNode(operator));
			return this;
		}
		
		formulaMng.setEnabledPublishEvent(false);
		formulaMng.addNode(new NumberNode(entryMng.getNumber(), entryMng.getBaseNumber(), entryMng.getInvokedFunctions()));
		BigDecimal result = calculator.calculate(formulaMng.getFormulaNodes());
		formulaMng.setEnabledPublishEvent(true);
		
		entryMng.setNumber(result);
		formulaMng.addNode(new OperatorNode(operator));
		
		lastInputedOperator = true;
		
		return this;
	}

	
	//---
	// �֐�
	//---

	@Override
	public Calculator inputFunction(Function function) {
		entryMng.invokeFunction(function);
		return this;
	}
	
	
	//---
	// �v�Z�̎��s
	//---
	
	@Override
	public void calculate() {
		List<FormulaNodeWrapper> nodes = formulaMng.getFormulaNodes();
		
		if (nodes.isEmpty()) {
			// �O��̍X�V�����������
			if (!historyMng.isEmpty()) {
				CalculateHistory history = historyMng.getLastHistory();
				OperatorNode on = history.getOperatorNode();
				NumberNode nn = history.getNumberNode();
				
				formulaMng.addNode(new NumberNode(entryMng.getNumber(), entryMng.getBaseNumber(), entryMng.getInvokedFunctions()));
				formulaMng.addNode(on);
				formulaMng.addNode(nn);
				
				BigDecimal result = calculator.calculate(formulaMng.getFormulaNodes());
				entryMng.setNumber(result);
				formulaMng.clear();
				lastInputedOperator = false;
				return;
			}
			
			return;
		}
		
		formulaMng.addNode(new NumberNode(entryMng.getNumber(), entryMng.getBaseNumber(), entryMng.getInvokedFunctions()));
		BigDecimal result = calculator.calculate(formulaMng.getFormulaNodes());
		entryMng.setNumber(result);
		formulaMng.clear();
		lastInputedOperator = false;
	}

	
	//---
	// �C�x���g���X�i�[�̓o�^
	//---
	
	@Override
	public void addEntryChangeEventListener(EntryChangeEventListener listener) {
		entryMng.addEntryChangeEventListener(listener);
	}
	
	@Override
	public void addFormulaChangeEventListener(FormulaChangeEventListener listener) {
		formulaMng.addFormulaChangeEventListener(listener);
	}
	
	
	// TODO: �����g�p�ׁ̈A�Ƃ肠�����R�����g�A�E�g
	// (�r���[���ł̓C�x���g���X�i�[�o�R�̎擾�ŏ[�����Ă��邽��)
	
	//---
	// �G���g���[�A�����̎擾
	//---
	/*
	@Override
	public List<FormulaNodeWrapper> getFormulaNodes() {
		return formulaMng.getFormulaNodes();
	}
	
	@Override
	public BigDecimal getEntryValue() {
		return entryMng.getNumber();
	}
	
	@Override
	public String getEntryPlainText() {
		return entryMng.getPlainText();
	}
	*/
}
