package sg.study.lib.calc.event;

import java.math.BigDecimal;

public class EntryChangeEvent {
	private BigDecimal entryValue;
	private String entryPlainText;
	
	public EntryChangeEvent(BigDecimal entryValue, String entryPlainText) {
		this.entryValue = entryValue;
		this.entryPlainText = entryPlainText;
	}

	public BigDecimal getEntryValue() {
		return entryValue;
	}
	
	public String getEntryPlainText() {
		return entryPlainText;
	}
}