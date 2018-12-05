package sg.study.lib.calc;

import java.math.BigDecimal;
import java.math.MathContext;

import sg.study.lib.calc.exception.DividebyZeroException;

/**
 * �d��ɂ�����֐���\���B
 * �֐��L�[�Ƃ́A�G���g���\�̒l�������Ɋ֐������s���A���̌��ʂ��ēx�G���g���[�֔��f��������̂��w���B
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
	
	
	// TODO: BigDecimal#sqrt��Java9���瓱������Ă��邯�ǁASwing WindowBuilder��Java8�܂ł����Ή����Ă��Ȃ��Ƃ����B�B�B
	// ���W�b�N���l����̂��ʓ|�Ȃ̂ň�U�l�b�g�̃R�s�y�B�B�B
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
