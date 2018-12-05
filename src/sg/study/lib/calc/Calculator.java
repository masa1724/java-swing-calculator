package sg.study.lib.calc;

import java.math.BigDecimal;

import sg.study.lib.calc.event.EntryChangeEventListener;
import sg.study.lib.calc.event.FormulaChangeEventListener;

/**
 * 電卓のインターフェースとして、必要な機能を定義する。
 * 
 * 当クラスでは電卓としてのロジックをビューに依存させない、また混在させないために、
 * 電卓のエントリーおよび数式等の情報を全て内部情報として隠ぺいしている。
 * 
 * 上記に伴い、ビュー側ではエントリ―や数式の変更を検知できないが(厳密にはできなくはないが、当クラスとビューで2重管理になるため非推奨)、
 * その代わりにエントリ―と数式の変更をビュー側へ通知する為のイベントリスナーを提供する。
 * 
 * @see EntryChangeEventListener
 * @see FormulaChangeEventListener
 */
public interface Calculator {
	// ファクトリメソッド
	public static Calculator getCalculator() {
		return new CalculatorImpl(); 
	}

	//---
	// エントリー、数式のクリア
	//---
	
	/**
	 * エントリ―を削除する。
	 * 
	 */
	void clearEntry();
	
	/**
	 * エントリ―および数式(Formula)を削除する。
	 * 
	 */
	void clear();
	
	/**
	 * エントリ―の右側1文字を削除する。
	 * 未入力の場合(0の場合)、何も行わない。
	 * 
	 */
	void deleteEntryRight();
	
	
	//---
	// エントリー
	//---
	
	/**
	 * エントリーの右側へ入力されたキーの値を追加する。
	 * 
	 * ただし、以下の条件の場合、入力を無視します。
	 * ・エントリ―が0の状態で、entry.Zeroが渡された場合
	 * ・既にエントリーへドット(.)が入力済の状態で、entry.Dotが渡された場合
	 * ・
	 * 
	 * @param entry 入力したエントリーキー
	 * @return this
	 */
	Calculator inputEntry(Entry entry);
	
	Calculator setEntryNumber(BigDecimal number);
	
	/**
	 * エントリーの正負を反転する。
	 * 
	 */
	Calculator reverseEntrySign();
	
	
	//---
	// 演算
	//---
	
	/**
	 * エントリー数式(Formula)へ追加する。
	 * 
	 * @param entry 演算子
	 * @return this
	 */
	Calculator inputOperator(Operator operator);
	
	
	//---
	// 関数
	//---
	
	/**
	 * エントリ―を数式(Formula)へ追加する。
	 * 
	 * @param function メソッドキー
	 * @return this
	 */
	Calculator inputFunction(Function function);
	
	
	//---
	// 計算の実行
	//---
	
	/**
	 *  現在入力されている計算式を実行する。
	 * 
	 */
	void calculate();
		
	
	//---
	// イベントリスナーの登録
	//---
	
	/**
	 * エントリーの変更時に呼び出すイベントリスナーを登録する。
	 * 
	 * @param listener
	 */
	void addEntryChangeEventListener(EntryChangeEventListener listener);
	
	/**
	 * 数式(Formula)の変更時に呼び出すイベントリスナーを登録する。
	 * 
	 * @param listener
	 */
	void addFormulaChangeEventListener(FormulaChangeEventListener listener);
	
	
	// TODO: ↓未使用の為、とりあえずコメントアウト
	// (ビュー側ではイベントリスナー経由の取得で充足しているため)
	
	//---
	// エントリー、数式の取得
	//---
	
	/**
	 * 数式(Formula)を返す。
	 * 
	 * @return
	 */
	//List<FormulaNodeWrapper> getFormulaNodes();
	
	/**
	 * エントリ―をBigDecimal型に変換した値を返す。
	 * 
	 * @return Entry
	 */
	//BigDecimal getEntryValue();
	
	/**
	 * エントリ―をそのまま文字列として返す。
	 * 
	 * @return
	 */
	//String getEntryPlainText();
}
