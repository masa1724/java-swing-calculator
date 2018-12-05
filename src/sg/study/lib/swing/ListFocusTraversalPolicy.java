package sg.study.lib.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * フォーカスサイクルルートを持つコンポーネントのトラバース順序を定義する
 * リストへ追加した順番がそのままフォーカス順序として、設定されます
 *
 */
public class ListFocusTraversalPolicy extends FocusTraversalPolicy {
	private List<Component> order = new ArrayList<>();

	public ListFocusTraversalPolicy add(Component c) {
		order.add(c);
		return this;
	}

	public ListFocusTraversalPolicy addAll(List<? extends Component> cs) {
		order.addAll(cs);
		return this;
	}

	@Override
	public Component getFirstComponent(Container focusCycleRoot) {
		return order.get(0);
	}

	@Override
	public Component getLastComponent(Container focusCycleRoot) {
		return order.get(order.size() - 1);
	}

	@Override
	public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
		int after = order.indexOf(aComponent) + 1;
		int cycled = after % order.size(); // indexがリストの末尾を超えたら先頭からサイクルさせる
		return order.get(cycled);
	}

	@Override
	public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
		int before = order.indexOf(aComponent) - 1;
		int cycled = (before + order.size()) % order.size(); // indexがリストの先頭を超えたら末尾からサイクルさせる
		return order.get(cycled);
	}

	@Override
	public Component getDefaultComponent(Container focusCycleRoot) {
		return order.get(0);
	}
}
