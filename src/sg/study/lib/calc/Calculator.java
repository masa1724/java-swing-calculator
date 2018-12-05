package sg.study.lib.calc;

import java.math.BigDecimal;

import sg.study.lib.calc.event.EntryChangeEventListener;
import sg.study.lib.calc.event.FormulaChangeEventListener;

/**
 * �d��̃C���^�[�t�F�[�X�Ƃ��āA�K�v�ȋ@�\���`����B
 * 
 * ���N���X�ł͓d��Ƃ��Ẵ��W�b�N���r���[�Ɉˑ������Ȃ��A�܂����݂����Ȃ����߂ɁA
 * �d��̃G���g���[����ѐ������̏���S�ē������Ƃ��ĉB�؂����Ă���B
 * 
 * ��L�ɔ����A�r���[���ł̓G���g���\�␔���̕ύX�����m�ł��Ȃ���(�����ɂ͂ł��Ȃ��͂Ȃ����A���N���X�ƃr���[��2�d�Ǘ��ɂȂ邽�ߔ񐄏�)�A
 * ���̑���ɃG���g���\�Ɛ����̕ύX���r���[���֒ʒm����ׂ̃C�x���g���X�i�[��񋟂���B
 * 
 * @see EntryChangeEventListener
 * @see FormulaChangeEventListener
 */
public interface Calculator {
	// �t�@�N�g�����\�b�h
	public static Calculator getCalculator() {
		return new CalculatorImpl(); 
	}

	//---
	// �G���g���[�A�����̃N���A
	//---
	
	/**
	 * �G���g���\���폜����B
	 * 
	 */
	void clearEntry();
	
	/**
	 * �G���g���\����ѐ���(Formula)���폜����B
	 * 
	 */
	void clear();
	
	/**
	 * �G���g���\�̉E��1�������폜����B
	 * �����͂̏ꍇ(0�̏ꍇ)�A�����s��Ȃ��B
	 * 
	 */
	void deleteEntryRight();
	
	
	//---
	// �G���g���[
	//---
	
	/**
	 * �G���g���[�̉E���֓��͂��ꂽ�L�[�̒l��ǉ�����B
	 * 
	 * �������A�ȉ��̏����̏ꍇ�A���͂𖳎����܂��B
	 * �E�G���g���\��0�̏�ԂŁAentry.Zero���n���ꂽ�ꍇ
	 * �E���ɃG���g���[�փh�b�g(.)�����͍ς̏�ԂŁAentry.Dot���n���ꂽ�ꍇ
	 * �E
	 * 
	 * @param entry ���͂����G���g���[�L�[
	 * @return this
	 */
	Calculator inputEntry(Entry entry);
	
	Calculator setEntryNumber(BigDecimal number);
	
	/**
	 * �G���g���[�̐����𔽓]����B
	 * 
	 */
	Calculator reverseEntrySign();
	
	
	//---
	// ���Z
	//---
	
	/**
	 * �G���g���[����(Formula)�֒ǉ�����B
	 * 
	 * @param entry ���Z�q
	 * @return this
	 */
	Calculator inputOperator(Operator operator);
	
	
	//---
	// �֐�
	//---
	
	/**
	 * �G���g���\�𐔎�(Formula)�֒ǉ�����B
	 * 
	 * @param function ���\�b�h�L�[
	 * @return this
	 */
	Calculator inputFunction(Function function);
	
	
	//---
	// �v�Z�̎��s
	//---
	
	/**
	 *  ���ݓ��͂���Ă���v�Z�������s����B
	 * 
	 */
	void calculate();
		
	
	//---
	// �C�x���g���X�i�[�̓o�^
	//---
	
	/**
	 * �G���g���[�̕ύX���ɌĂяo���C�x���g���X�i�[��o�^����B
	 * 
	 * @param listener
	 */
	void addEntryChangeEventListener(EntryChangeEventListener listener);
	
	/**
	 * ����(Formula)�̕ύX���ɌĂяo���C�x���g���X�i�[��o�^����B
	 * 
	 * @param listener
	 */
	void addFormulaChangeEventListener(FormulaChangeEventListener listener);
	
	
	// TODO: �����g�p�ׁ̈A�Ƃ肠�����R�����g�A�E�g
	// (�r���[���ł̓C�x���g���X�i�[�o�R�̎擾�ŏ[�����Ă��邽��)
	
	//---
	// �G���g���[�A�����̎擾
	//---
	
	/**
	 * ����(Formula)��Ԃ��B
	 * 
	 * @return
	 */
	//List<FormulaNodeWrapper> getFormulaNodes();
	
	/**
	 * �G���g���\��BigDecimal�^�ɕϊ������l��Ԃ��B
	 * 
	 * @return Entry
	 */
	//BigDecimal getEntryValue();
	
	/**
	 * �G���g���\�����̂܂ܕ�����Ƃ��ĕԂ��B
	 * 
	 * @return
	 */
	//String getEntryPlainText();
}
