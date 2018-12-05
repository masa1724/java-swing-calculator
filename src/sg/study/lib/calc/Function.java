package sg.study.lib.calc;

import java.math.BigDecimal;
import java.math.MathContext;

import sg.study.lib.calc.exception.DividebyZeroException;

/**
 * 電卓における関数を表す。
 * 関数キーとは、エントリ―の値を引数に関数を実行し、その結果を再度エントリーへ反映させるものを指す。
 *
 */
public enum Function {
	Sqrt("Sqrt") {
		public BigDecimal invoke(BigDecimal number) {
			return sqrt(number);
		}
	},
	
	Square("Square") {
		public BigDecimal invoke(BigDecimal number) {
			return number.multiply(number);
		}
	},
	
	Denominator("Denominator") {
		public BigDecimal invoke(BigDecimal number) {
			if (number.compareTo(BigDecimal.ZERO) == 0) {
				throw new DividebyZeroException();
			}
			
			return BigDecimal.ONE.divide(number);
		}
	};
	
	private String functionName;
	
	Function(String functionName) {
		this.functionName = functionName;
	}
	
	public String getFunctionName() {
		return functionName;
	}
	
	public abstract BigDecimal invoke(BigDecimal number);
	
	
	// TODO: BigDecimal#sqrtはJava9から導入されているけど、Swing WindowBuilderがJava8までしか対応していないという。。。
	// ロジックを考えるのが面倒なので一旦ネットのコピペ。。。
    private static BigDecimal sqrt(BigDecimal number){
    	if (number.compareTo(BigDecimal.ZERO) == 0) return number;
    	
        BigDecimal x = new BigDecimal(Math.sqrt(number.doubleValue()), MathContext.DECIMAL64);
        int scale = 50;

        BigDecimal b2 = new BigDecimal(2);
        for(int tempScale = 16; tempScale < scale; tempScale *= 2){
            // x = x - (x * x - a) / (2 * x);
            x = x.subtract(
                    x.multiply(x).subtract(number).divide(
                    x.multiply(b2), scale, BigDecimal.ROUND_HALF_EVEN));
        }
        return x;
    }
}
