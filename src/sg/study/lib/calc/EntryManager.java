package sg.study.lib.calc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;

import sg.study.lib.calc.event.EntryChangeEvent;
import sg.study.lib.calc.event.EntryChangeEventListener;

/**
 * �d��ɂ�����G���g���[�����̓��o�͊Ǘ����s���B
 * �܂��A�G���g���[�������ύX���ꂽ���Ƃ��r���[���֒ʒm����Observer�Ƃ��Ă̖������S���B
 *
 */
// ���������J�ɂ���ׁA�p�b�P�[�W�A�N�Z�X
class EntryManager {
	private StringBuilder numberText;
	private Sign sign;
	private BigDecimal baseNumber;
	private List<Function> invokedFunctions;
	//private boolean enabledPublishEvent = true; // TODO:���g�p�ׁ̈A�Ƃ肠�����R�����g�A�E�g

	private static final String DOT = ".";
	private static final String INITIAL_VALUE = "0";

	public EntryManager() {
		init();
	}

	private void init() {
		numberText = new StringBuilder(INITIAL_VALUE);
		sign = Sign.Plus;
		baseNumber = null;
		invokedFunctions = new ArrayList<>();
	}

	public void clear() {
		init();
		publishChangeEntryEvent();
	}

	public void deleteRight() {
		String text = numberText.toString();
		
		if (text.equals(INITIAL_VALUE)) return;
		
		if (text.length() == 1) {
			init();
			publishChangeEntryEvent();
			return;
		}
		
		numberText.delete(numberText.length() - 1, numberText.length());
		publishChangeEntryEvent();
	}

	public void inputEntry(Entry entry) {
		if (entry.isNumber()) {
			inputNumber(entry.getValue());
			return;
		}

		switch (entry) {
		case Dot:
			inputDot();
			break;
		default:
			throw new InvalidKeyException("�s���ȃG���g���[�����͂���܂����Bentry=" + entry.getValue());
		}
	}

	private void inputNumber(String inputNumber) {
		if (!invokedFunctions.isEmpty()) {
			init(); // �֐��K�p��ɃG���g���\�ւ̓��͂��s��ꂽ�ꍇ�́A���܂ł̃G���g���[��j������
		}
		
		if (numberText.toString().equals(INITIAL_VALUE)) {
			if (inputNumber.equals(INITIAL_VALUE)) {
				return; // 0���d�����͂����Ȃ�
			}
			
			numberText = new StringBuilder(); // �����l0�����͂���Ă��邽�߁A�폜����
		}
				
		numberText.append(inputNumber);
		publishChangeEntryEvent();
	}

	private void inputDot() {
		if (numberText.toString().contains(DOT)) return;
		numberText.append(DOT);
		publishChangeEntryEvent();
	}

	public void invokeFunction(Function function) {
		if (invokedFunctions.isEmpty()) baseNumber = getNumber();
		BigDecimal result = function.invoke(getNumber());
		invokedFunctions.add(function);
		_setNumber(result);
	}
	
	public void setNumber(BigDecimal inputNumber) {
		if (!invokedFunctions.isEmpty()) {
			init(); // �֐��K�p��ɃG���g���\�ւ̓��͂��s��ꂽ�ꍇ�́A���܂ł̃G���g���[��j������
		}
		
		_setNumber(inputNumber);
	}
	
	private void _setNumber(BigDecimal num) {
		numberText = new StringBuilder(num.abs().stripTrailingZeros().toPlainString());

		if (num.compareTo(BigDecimal.ZERO) == -1) {
			sign = Sign.Minus;
		} else {
			sign = Sign.Plus;
		}

		publishChangeEntryEvent();
	}
	
	public void reverseSign() {
		if (sign == Sign.Plus) {
			sign = Sign.Minus;
		} else {
			sign = Sign.Plus;
		}
		
		publishChangeEntryEvent();
	}

	public BigDecimal getNumber() {
		return new BigDecimal(sign.getSign() + numberText.toString());
	}
	
	public BigDecimal getBaseNumber() {
		if (baseNumber != null) return baseNumber;
		return getNumber();
	}

	public String getPlainText() {
		if(sign == Sign.Plus) return numberText.toString();
		return sign.getSign() + numberText.toString();
	}
	
	public List<Function> getInvokedFunctions() {
		return invokedFunctions;
	}

	enum Sign {
		Plus("+"), Minus("-");

		private String sign;

		Sign(String sign) {
			this.sign = sign;
		}

		public String getSign() {
			return sign;
		}
	}
	

	private List<EntryChangeEventListener> entryChangeEventListeners = new ArrayList<>();

	public void addEntryChangeEventListener(EntryChangeEventListener listener) {
		entryChangeEventListeners.add(listener);
	}
	
	// public void setEnabledPublishEvent(boolean enabledPublishEvent) {// TODO:���g�p�ׁ̈A�Ƃ肠�����R�����g�A�E�g
	//	this.enabledPublishEvent = enabledPublishEvent;
	//}

	private void publishChangeEntryEvent() {
		//if (!enabledPublishEvent) return;// TODO:���g�p�ׁ̈A�Ƃ肠�����R�����g�A�E�g
		System.out.println(DateTimeFormatter.ofPattern("HH:mm:ss.SSS").format(LocalDateTime.now()).toString() + " [entry] " + numberText.toString());
		entryChangeEventListeners.forEach(l -> l.onChange(new EntryChangeEvent(getNumber(), getPlainText())));
	}
}
