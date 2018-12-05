package sg.study.lib.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;

import sg.study.lib.calc.exception.DividebyZeroException;

/**
 * �d��ɂ����鉉�Z�q��\���B
 * 
 */
public enum Operator {
	Add("+") {
		public BigDecimal calculate(BigDecimal number1, BigDecimal number2) {
			return number1.add(number2);
		}
	},
	
	Subtract("-") {
		public BigDecimal calculate(BigDecimal number1, BigDecimal number2) {
			return number1.subtract(number2);
		}
	},
	
	Multiply("�~") {
		public BigDecimal calculate(BigDecimal number1, BigDecimal number2) {
			return number1.multiply(number2);
		}
	},
	
	Divide("��") {
		public BigDecimal calculate(BigDecimal number1, BigDecimal number2) {
			if (number2.compareTo(BigDecimal.ZERO) == 0) {
				throw new DividebyZeroException();
			}
			
			return number1.divide(number2, 30, RoundingMode.HALF_UP); // TODO: �����Ă��Ƃ��B��Ō������B
		}
	},
		
	Modulu("%") {
		public BigDecimal calculate(BigDecimal number1, BigDecimal number2) {
			return number1.remainder(number2);
		}
	};
	
	private String symbol;
	
	Operator(String symbol) {
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public abstract BigDecimal calculate(BigDecimal number1, BigDecimal number2);
}
