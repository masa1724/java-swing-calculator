package sg.study.lib.calc.event;

/**
 * 
 * 数式(Formula)の変更時に呼び出されるイベントリスナー
 *
 */
public interface FormulaChangeEventListener {
	void onChange(FormulaChangeEvent e);
}
