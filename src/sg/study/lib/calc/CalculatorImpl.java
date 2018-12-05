package sg.study.lib.calc;

import java.math.BigDecimal;
import java.util.List;

import sg.study.lib.calc.event.EntryChangeEventListener;
import sg.study.lib.calc.event.FormulaChangeEventListener;
import sg.study.lib.calc.node.FormulaNodeWrapper;
import sg.study.lib.calc.node.NumberNode;
import sg.study.lib.calc.node.OperatorNode;

/**
 * Calculatorの実装クラス
 * ビュー側とのやりとりについては、原則すべてこのクラスを経由して行う。
 *
 */
// 実装を非公開にする為、パッケージアクセス
class CalculatorImpl implements Calculator {
	private FormulaManager formulaMng = new FormulaManager();
	private EntryManager entryMng = new EntryManager();
	
	private CalculateHistoryManager historyMng = new CalculateHistoryManager();
	private InternalCalculator calculator = new InternalCalculator(historyMng);
	
	private boolean lastInputedOperator = false;
	
	
	//---
	// エントリー、数式のクリア
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
	// エントリー
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
	// 演算
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
	// 関数
	//---

	@Override
	public Calculator inputFunction(Function function) {
		entryMng.invokeFunction(function);
		return this;
	}
	
	
	//---
	// 計算の実行
	//---
	
	@Override
	public void calculate() {
		List<FormulaNodeWrapper> nodes = formulaMng.getFormulaNodes();
		
		if (nodes.isEmpty()) {
			// 前回の更新履歴があれば
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
	// イベントリスナーの登録
	//---
	
	@Override
	public void addEntryChangeEventListener(EntryChangeEventListener listener) {
		entryMng.addEntryChangeEventListener(listener);
	}
	
	@Override
	public void addFormulaChangeEventListener(FormulaChangeEventListener listener) {
		formulaMng.addFormulaChangeEventListener(listener);
	}
	
	
	// TODO: ↓未使用の為、とりあえずコメントアウト
	// (ビュー側ではイベントリスナー経由の取得で充足しているため)
	
	//---
	// エントリー、数式の取得
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
