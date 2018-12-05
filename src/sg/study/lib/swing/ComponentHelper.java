package sg.study.lib.swing;

import java.util.List;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * Swing Component�ɑ΂��鏈�����ȑf������ׂ̃w���p�[�N���X
 *
 */
public class ComponentHelper {
	// <condition>
	// JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT : ��M���R���|�[�l���g���t�H�[�J�X�����R���|�[�l���g�̏�ʂł��邩�A���ꎩ�̂��t�H�[�J�X�����R���|�[�l���g�ł���ꍇ�ɁA�R�}���h���Ăяo�����Ƃ������܂��B
	// JComponent.WHEN_FOCUSED : �R���|�[�l���g�Ƀt�H�[�J�X���ݒ肳�ꂽ�Ƃ��ɃR�}���h���Ăяo�����Ƃ������܂��B
	// JComponent.WHEN_IN_FOCUSED_WINDOW : ��M���R���|�[�l���g���t�H�[�J�X�����E�B���h�E���ɂ��邩�A���ꎩ�̂��t�H�[�J�X�����R���|�[�l���g�ł���ꍇ�ɁA�R�}���h���Ăяo�����Ƃ������܂��B
	
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
