package sg.study.lib.calc.node;

import java.math.BigDecimal;
import java.util.List;

import sg.study.lib.calc.Function;

public class NumberNode {
	private BigDecimal number;
	private BigDecimal baseNumber;
	private List<Function> invokedFunctions;
	
	public NumberNode(BigDecimal number, BigDecimal baseNumber, List<Function> invokedFunctions) {
		this.number = number;
		this.baseNumber = baseNumber;
		this.invokedFunctions = invokedFunctions;
	}
	
	public BigDecimal getNumber() {
		return number;
	}
	
	public boolean isInvokedFunction() {
		return !(invokedFunctions.isEmpty());
	}
	
	public BigDecimal getBaseNumber() {
		return baseNumber;
	}
	
	public List<Function> getInvokedFunctions() {
		return invokedFunctions;
	}
	
	public String toString() {
		return String.format("%s,bn=%s,f=%s", number, baseNumber, invokedFunctions.toString());
	}
}
