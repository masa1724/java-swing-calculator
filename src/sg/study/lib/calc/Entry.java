package sg.study.lib.calc;

/**
 * 電卓におけるエントリー(数値/小数点)を表す。
 * エントリーを入力すると、入力した値がそのままエントリーの末尾へ追加される。
 *
 */
public enum Entry {
	Zero("0", true),
	One("1", true),
	Two("2", true),
	Three("3", true),
	Four("4", true),
	Five("5", true),
	Six("6", true),
	Seven("7", true),
	Eight("8", true),
	Nine("9", true),
	Dot(".", false);
	
	private String value;
	private boolean isNumber;
	
	Entry(String value, boolean isNumber) {
		this.value = value;
		this.isNumber = isNumber;
	}
	
	public String getValue() {
		return value;
	}
	
	public boolean isNumber() {
		return isNumber;
	}
}
