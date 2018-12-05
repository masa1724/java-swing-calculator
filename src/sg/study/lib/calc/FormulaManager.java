package sg.study.lib.calc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import sg.study.lib.calc.event.FormulaChangeEvent;
import sg.study.lib.calc.event.FormulaChangeEventListener;
import sg.study.lib.calc.node.FormulaNodeWrapper;
import sg.study.lib.calc.node.NumberNode;
import sg.study.lib.calc.node.OperatorNode;

/**
 * 電卓における数式部分の入出力管理を行う。
 * また、数式部分が変更されたことをビュー側へ通知するObserverとしての役割も担う。
 *
 */
// 実装を非公開にする為、パッケージアクセス
class FormulaManager {
	private List<FormulaNodeWrapper> nodes;
	private boolean enabledPublishEvent = true;

	public FormulaManager() {
		init();
	}
	
	private void init() {
		nodes = new ArrayList<>();
	}
	
	public void clear() {
		init();
		publishChangeFormulaEvent();
	}
	
	public void addNode(NumberNode node) {
		nodes.add(new FormulaNodeWrapper(node, null));
		publishChangeFormulaEvent();
	}
	
	public void addNode(OperatorNode node) {
		nodes.add(new FormulaNodeWrapper(null, node));
		publishChangeFormulaEvent();
	}
	
	public void changeLastNode(OperatorNode node) {
		nodes.remove(nodes.size() - 1); // 最後の要素を取り出して、演算子を書き換える
		nodes.add(new FormulaNodeWrapper(null, node));
		publishChangeFormulaEvent();
	}
	
	public FormulaNodeWrapper getLastNode() {
		if (nodes.isEmpty()) return null;
		return nodes.get(nodes.size() - 1);
	}
	
	public List<FormulaNodeWrapper> getFormulaNodes() {
		return nodes;
	}
	
	
	private List<FormulaChangeEventListener> formulaChangeEventListeners = new ArrayList<>();

	public void addFormulaChangeEventListener(FormulaChangeEventListener listener) {
		formulaChangeEventListeners.add(listener);
	}
	
	public void setEnabledPublishEvent(boolean enabledPublishEvent) {
		this.enabledPublishEvent = enabledPublishEvent;
	}
	
	private void publishChangeFormulaEvent() {
		if (!enabledPublishEvent) return;
		System.out.println(DateTimeFormatter.ofPattern("HH:mm:ss.SSS").format(LocalDateTime.now()).toString() + " [formula] " + nodes.toString());
		formulaChangeEventListeners.forEach(l -> l.onChange(new FormulaChangeEvent(nodes)));
	}
}
