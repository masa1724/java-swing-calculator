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
 * 電卓におけるエントリー部分の入出力管理を行う。
 * また、エントリー部分が変更されたことをビュー側へ通知するObserverとしての役割も担う。
 *
 */
// 実装を非公開にする為、パッケージアクセス
class EntryManager {
	private StringBuilder numberText;
	private Sign sign;
	private BigDecimal baseNumber;
	private List<Function> invokedFunctions;
	//private boolean enabledPublishEvent = true; // TODO:未使用の為、とりあえずコメントアウト

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
			throw new InvalidKeyException("不正なエントリーが入力されました。entry=" + entry.getValue());
		}
	}

	private void inputNumber(String inputNumber) {
		if (!invokedFunctions.isEmpty()) {
			init(); // 関数適用後にエントリ―への入力が行われた場合は、今までのエントリーを破棄する
		}
		
		if (numberText.toString().equals(INITIAL_VALUE)) {
			if (inputNumber.equals(INITIAL_VALUE)) {
				return; // 0を重複入力させない
			}
			
			numberText = new StringBuilder(); // 初期値0が入力されているため、削除する
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
			init(); // 関数適用後にエントリ―への入力が行われた場合は、今までのエントリーを破棄する
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
	
	// public void setEnabledPublishEvent(boolean enabledPublishEvent) {// TODO:未使用の為、とりあえずコメントアウト
	//	this.enabledPublishEvent = enabledPublishEvent;
	//}

	private void publishChangeEntryEvent() {
		//if (!enabledPublishEvent) return;// TODO:未使用の為、とりあえずコメントアウト
		System.out.println(DateTimeFormatter.ofPattern("HH:mm:ss.SSS").format(LocalDateTime.now()).toString() + " [entry] " + numberText.toString());
		entryChangeEventListeners.forEach(l -> l.onChange(new EntryChangeEvent(getNumber(), getPlainText())));
	}
}
