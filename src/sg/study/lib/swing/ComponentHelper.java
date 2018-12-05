package sg.study.lib.swing;

import java.util.List;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * Swing Componentに対する処理を簡素化する為のヘルパークラス
 *
 */
public class ComponentHelper {
	// <condition>
	// JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT : 受信側コンポーネントがフォーカスを持つコンポーネントの上位であるか、それ自体がフォーカスを持つコンポーネントである場合に、コマンドを呼び出すことを示します。
	// JComponent.WHEN_FOCUSED : コンポーネントにフォーカスが設定されたときにコマンドを呼び出すことを示します。
	// JComponent.WHEN_IN_FOCUSED_WINDOW : 受信側コンポーネントがフォーカスを持つウィンドウ内にあるか、それ自体がフォーカスを持つコンポーネントである場合に、コマンドを呼び出すことを示します。
	
	public static void registerKeyboardAction(JComponent component, int condition, int keyCode, int modifiers, Action action) {
		KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
				
		if (condition == JComponent.UNDEFINED_CONDITION) {
			component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, action);
		} else {
			component.getInputMap(condition).put(keyStroke, action);
		}
		
		component.getActionMap().put(action, action);
	}
	
	public static void registerKeyboardAction(JComponent component, int keyCode, int modifiers, Action action) {
		registerKeyboardAction(component, JComponent.UNDEFINED_CONDITION, keyCode, modifiers, action);
	}
	
	public static void registerKeyboardAction(JComponent component, List<Integer> keyCodes, int modifiers, Action action) {
		for (int keyCode : keyCodes) {
			registerKeyboardAction(component, JComponent.UNDEFINED_CONDITION, keyCode, modifiers, action);
		}
	}
}
