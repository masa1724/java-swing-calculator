package sg.study.app.calc;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import sg.study.lib.calc.Calculator;
import sg.study.lib.calc.Entry;
import sg.study.lib.calc.Function;
import sg.study.lib.calc.Operator;
import sg.study.lib.calc.event.EntryChangeEvent;
import sg.study.lib.calc.event.EntryChangeEventListener;
import sg.study.lib.calc.event.FormulaChangeEvent;
import sg.study.lib.calc.event.FormulaChangeEventListener;
import sg.study.lib.calc.node.FormulaNodeWrapper;
import sg.study.lib.calc.node.NumberNode;
import sg.study.lib.calc.node.OperatorNode;
import sg.study.lib.swing.ComponentHelper;
import sg.study.lib.swing.ListFocusTraversalPolicy;

public class SwingCalculatorApplication {
	private JFrame frame;
	private JTextField txtEntry;
	private JTextField txtFormula;

	private JButton btnClearEntry;
	private JButton btnClear;
	private JButton btnDeleteEntryRight;
	private JButton btnReverseSign;

	private List<JButton> functionButtons = new ArrayList<>();
	private List<JButton> operatorButtons = new ArrayList<>();
	private List<JButton> entryButtons = new ArrayList<>();

	private ButtonGroup btnGroupLAF = new ButtonGroup(); // LookAndFeelのMenuItemを単一選択のみ可能にするよう制御する
	private final String DEFAULT_LAF_CLASS_NAME = "javax.swing.plaf.nimbus.NimbusLookAndFeel";

	private Calculator calculator = Calculator.getCalculator();

	private static final String MSG_ARITHMETIC_EXCEPTION = "計算中にエラーが発生しました。";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingCalculatorApplication window = new SwingCalculatorApplication();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SwingCalculatorApplication() {
		initialize();
		initializeListener();
	}

	private void initializeListener() {
		calculator.addEntryChangeEventListener(new EntryChangeEventListener() {
			@Override
			public void onChange(EntryChangeEvent e) {
				txtEntry.setText(e.getEntryPlainText());
			}
		});

		calculator.addFormulaChangeEventListener(new FormulaChangeEventListener() {
			@Override
			public void onChange(FormulaChangeEvent e) {
				StringBuilder formulaText = new StringBuilder();

				for (FormulaNodeWrapper wrap : e.getFormulaNodes()) {
					if (wrap.isNumberNode()) {
						NumberNode nn = wrap.getNumberNode();

						String partText = null;

						if (nn.isInvokedFunction()) {
							partText = nn.getBaseNumber().toPlainString();

							for (Function func : nn.getInvokedFunctions()) {
								partText = func.getFunctionName() + "(" + partText + ")";
							}
						} else {
							partText = nn.getNumber().toPlainString();
						}

						formulaText.append(partText);

					} else {
						OperatorNode on = wrap.getOperatorNode();
						formulaText.append(on.getOperator().getSymbol());
					}
				}

				txtFormula.setText(formulaText.toString());
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 478);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		JPanel pnView = new JPanel();
		frame.getContentPane().add(pnView);
		pnView.setLayout(new BoxLayout(pnView, BoxLayout.Y_AXIS));

		txtFormula = new JTextField();
		txtFormula.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		txtFormula.setEditable(false);
		pnView.add(txtFormula);
		txtFormula.setColumns(10);

		txtEntry = new JTextField();
		txtEntry.setMargin(new Insets(2, 2, 2, 10));
		txtEntry.setFont(new Font("游ゴシック Medium", Font.PLAIN, 21));
		txtEntry.setHorizontalAlignment(SwingConstants.RIGHT);
		txtEntry.setText("0");
		txtEntry.setEditable(false);
		txtEntry.setColumns(10);
		pnView.add(txtEntry);

		JPanel pnKeys = new JPanel();
		frame.getContentPane().add(pnKeys);
		pnKeys.setLayout(new GridLayout(6, 4, 0, 0));

		JButton btnFuncSqrt = new JButton("\u221A");
		btnFuncSqrt.addActionListener(new InputMethodActionListener(Function.Sqrt));
		btnFuncSqrt.setMaximumSize(new Dimension(100, 100));
		btnFuncSqrt.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnFuncSqrt);

		JButton btnFuncSquare = new JButton("\uFF58\u00B2");
		btnFuncSquare.addActionListener(new InputMethodActionListener(Function.Square));
		btnFuncSquare.setMaximumSize(new Dimension(100, 100));
		btnFuncSquare.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnFuncSquare);

		JButton btnFuncDenominator = new JButton("1/x");
		btnFuncDenominator.addActionListener(new InputMethodActionListener(Function.Denominator));
		btnFuncDenominator.setMaximumSize(new Dimension(100, 100));
		btnFuncDenominator.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnFuncDenominator);

		JButton btnOpeModule = new JButton("%");
		btnOpeModule.addActionListener(new InputOperatorActionListener(Operator.Modulu));
		pnKeys.add(btnOpeModule);
		btnOpeModule.setMaximumSize(new Dimension(100, 100));
		btnOpeModule.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		ComponentHelper.registerKeyboardAction(btnOpeModule, KeyEvent.VK_5, KeyEvent.SHIFT_DOWN_MASK,
				new InputOperatorAction(Operator.Modulu));
		operatorButtons.add(btnOpeModule);

		btnClearEntry = new JButton("CE");
		btnClearEntry.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnClearEntry);
		btnClearEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculator.clearEntry();
			}
		});
		btnClearEntry.setMaximumSize(new Dimension(100, 100));
		ComponentHelper.registerKeyboardAction(btnClearEntry, KeyEvent.VK_DELETE, 0, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				calculator.clearEntry();
			}
		});

		btnClear = new JButton("C");
		btnClear.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnClear);
		btnClear.setMaximumSize(new Dimension(100, 100));
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculator.clear();
				enableSwitchOtherThanClearButton(true);
			}
		});
		// TODO: ショートカットキー調べる

		btnDeleteEntryRight = new JButton("[x]");
		btnDeleteEntryRight.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnDeleteEntryRight);
		btnDeleteEntryRight.setMaximumSize(new Dimension(100, 100));
		btnDeleteEntryRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculator.deleteEntryRight();
			}
		});
		ComponentHelper.registerKeyboardAction(btnDeleteEntryRight, KeyEvent.VK_BACK_SPACE, 0, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				calculator.deleteEntryRight();
			}
		});

		JButton btnOpeDivide = new JButton("\u00F7");
		btnOpeDivide.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnOpeDivide);
		btnOpeDivide.addActionListener(new InputOperatorActionListener(Operator.Divide));
		ComponentHelper.registerKeyboardAction(btnOpeDivide, KeyEvent.VK_DIVIDE, 0,
				new InputOperatorAction(Operator.Divide));

		JButton btnEnt7 = new JButton("7");
		btnEnt7.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnEnt7);
		btnEnt7.addActionListener(new InputEntryActionListener(Entry.Seven));
		ComponentHelper.registerKeyboardAction(btnEnt7, Arrays.asList(KeyEvent.VK_7, KeyEvent.VK_NUMPAD7), 0,
				new InputEntryAction(Entry.Seven));

		JButton btnEnt8 = new JButton("8");
		btnEnt8.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnEnt8);
		btnEnt8.addActionListener(new InputEntryActionListener(Entry.Eight));
		ComponentHelper.registerKeyboardAction(btnEnt8, Arrays.asList(KeyEvent.VK_8, KeyEvent.VK_NUMPAD8), 0,
				new InputEntryAction(Entry.Eight));

		JButton btnEnt9 = new JButton("9");
		btnEnt9.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnEnt9);
		btnEnt9.addActionListener(new InputEntryActionListener(Entry.Nine));
		ComponentHelper.registerKeyboardAction(btnEnt9, Arrays.asList(KeyEvent.VK_9, KeyEvent.VK_NUMPAD9), 0,
				new InputEntryAction(Entry.Nine));

		JButton btnOpeMultiply = new JButton("\u00D7");
		btnOpeMultiply.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnOpeMultiply);
		btnOpeMultiply.addActionListener(new InputOperatorActionListener(Operator.Multiply));
		ComponentHelper.registerKeyboardAction(btnOpeMultiply, KeyEvent.VK_MULTIPLY, 0,
				new InputOperatorAction(Operator.Multiply));

		JButton btnEnt4 = new JButton("4");
		btnEnt4.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnEnt4);
		btnEnt4.addActionListener(new InputEntryActionListener(Entry.Four));
		ComponentHelper.registerKeyboardAction(btnEnt4, Arrays.asList(KeyEvent.VK_4, KeyEvent.VK_NUMPAD4), 0,
				new InputEntryAction(Entry.Four));

		JButton btnEnt5 = new JButton("5");
		btnEnt5.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnEnt5);
		btnEnt5.addActionListener(new InputEntryActionListener(Entry.Five));
		ComponentHelper.registerKeyboardAction(btnEnt5, Arrays.asList(KeyEvent.VK_5, KeyEvent.VK_NUMPAD5), 0,
				new InputEntryAction(Entry.Five));

		JButton btnEnt6 = new JButton("6");
		btnEnt6.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnEnt6);
		btnEnt6.addActionListener(new InputEntryActionListener(Entry.Six));
		ComponentHelper.registerKeyboardAction(btnEnt6, Arrays.asList(KeyEvent.VK_6, KeyEvent.VK_NUMPAD6), 0,
				new InputEntryAction(Entry.Six));

		JButton btnOpeSubtract = new JButton("-");
		btnOpeSubtract.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnOpeSubtract);
		btnOpeSubtract.addActionListener(new InputOperatorActionListener(Operator.Subtract));
		ComponentHelper.registerKeyboardAction(btnOpeSubtract, KeyEvent.VK_SUBTRACT, 0,
				new InputOperatorAction(Operator.Subtract));

		JButton btnEnt1 = new JButton("1");
		btnEnt1.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnEnt1);
		btnEnt1.addActionListener(new InputEntryActionListener(Entry.One));
		ComponentHelper.registerKeyboardAction(btnEnt1, Arrays.asList(KeyEvent.VK_1, KeyEvent.VK_NUMPAD1), 0,
				new InputEntryAction(Entry.One));

		JButton btnEnt2 = new JButton("2");
		btnEnt2.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnEnt2);
		btnEnt2.addActionListener(new InputEntryActionListener(Entry.Two));
		ComponentHelper.registerKeyboardAction(btnEnt2, Arrays.asList(KeyEvent.VK_2, KeyEvent.VK_NUMPAD2), 0,
				new InputEntryAction(Entry.Two));

		JButton btnEnt3 = new JButton("3");
		btnEnt3.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnEnt3);
		btnEnt3.addActionListener(new InputEntryActionListener(Entry.Three));
		ComponentHelper.registerKeyboardAction(btnEnt3, Arrays.asList(KeyEvent.VK_3, KeyEvent.VK_NUMPAD3), 0,
				new InputEntryAction(Entry.Three));

		JButton btnOpeAdd = new JButton("+");
		btnOpeAdd.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnOpeAdd);
		btnOpeAdd.addActionListener(new InputOperatorActionListener(Operator.Add));
		ComponentHelper.registerKeyboardAction(btnOpeAdd, Arrays.asList(KeyEvent.VK_ADD, KeyEvent.VK_PLUS), 0,
				new InputOperatorAction(Operator.Add));

		btnReverseSign = new JButton("\u00B1");
		btnReverseSign.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnReverseSign);
		btnReverseSign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculator.reverseEntrySign();
			}
		});

		JButton btnEnt0 = new JButton("0");
		btnEnt0.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnEnt0);
		btnEnt0.addActionListener(new InputEntryActionListener(Entry.Zero));
		ComponentHelper.registerKeyboardAction(btnEnt0, Arrays.asList(KeyEvent.VK_0, KeyEvent.VK_NUMPAD0), 0,
				new InputEntryAction(Entry.Zero));

		JButton btnEntDot = new JButton(".");
		btnEntDot.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnEntDot);
		btnEntDot.addActionListener(new InputEntryActionListener(Entry.Dot));
		ComponentHelper.registerKeyboardAction(btnEntDot, Arrays.asList(KeyEvent.VK_PERIOD, KeyEvent.VK_DECIMAL), 0,
				new InputEntryAction(Entry.Dot));

		JButton btnOpeEquals = new JButton("=");
		btnOpeEquals.setFont(new Font("游ゴシック Medium", Font.PLAIN, 14));
		pnKeys.add(btnOpeEquals);
		btnOpeEquals.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					calculator.calculate();
				} catch (Exception ex) {
					exceptionHandle(ex);
				}
			}
		});
		ComponentHelper.registerKeyboardAction(btnOpeEquals, KeyEvent.VK_ENTER, 0, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				try {
					calculator.calculate();
				} catch (Exception ex) {
					exceptionHandle(ex);
				}
			}
		});

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu menuSetting = new JMenu("\u8A2D\u5B9A");
		menuBar.add(menuSetting);

		JMenu menuLAFSwitch = new JMenu("\u30B9\u30BF\u30A4\u30EB\u5207\u66FF");
		menuSetting.add(menuLAFSwitch);

		for (LookAndFeelInfo lafInfo : UIManager.getInstalledLookAndFeels()) {
			JMenuItem lafMenuItem = createLookAndFeelMenuItem(lafInfo);
			btnGroupLAF.add(lafMenuItem);
			menuLAFSwitch.add(lafMenuItem);
		}

		try {
			updateLookAndFeel(DEFAULT_LAF_CLASS_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

		functionButtons.add(btnFuncSqrt);
		functionButtons.add(btnFuncSquare);
		functionButtons.add(btnFuncDenominator);
		
		operatorButtons.add(btnOpeDivide);
		operatorButtons.add(btnOpeMultiply);
		operatorButtons.add(btnOpeSubtract);
		operatorButtons.add(btnOpeAdd);
		operatorButtons.add(btnOpeEquals);
		
		entryButtons.add(btnEnt0);
		entryButtons.add(btnEnt1);
		entryButtons.add(btnEnt2);
		entryButtons.add(btnEnt3);
		entryButtons.add(btnEnt4);
		entryButtons.add(btnEnt5);
		entryButtons.add(btnEnt6);
		entryButtons.add(btnEnt7);
		entryButtons.add(btnEnt8);
		entryButtons.add(btnEnt9);
		entryButtons.add(btnEntDot);
		
		//
		// フォーカス順序設定
		//
		FocusTraversalPolicy policy = new ListFocusTraversalPolicy()
			.add(txtFormula)
			.add(txtEntry)
			.addAll(functionButtons)
			.addAll(operatorButtons)
			.add(btnClearEntry)
			.add(btnClear)
			.add(btnDeleteEntryRight)
			.addAll(entryButtons)
			.add(btnReverseSign);

		frame.setFocusTraversalPolicy(policy);
	}

	private void enableSwitchOtherThanClearButton(boolean enabled) {
		functionButtons.forEach(b -> b.setEnabled(enabled));
		operatorButtons.forEach(b -> b.setEnabled(enabled));
		entryButtons.forEach(b -> b.setEnabled(enabled));
		btnReverseSign.setEnabled(enabled);
		btnClearEntry.setEnabled(enabled);
		btnDeleteEntryRight.setEnabled(enabled);
	}

	//
	// Calculator ActionListener
	//

	private class InputEntryActionListener implements ActionListener {
		private Entry entry;

		public InputEntryActionListener(Entry entry) {
			this.entry = entry;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				calculator.inputEntry(entry);
			} catch (Exception ex) {
				exceptionHandle(ex);
			}
		}
	}

	private class InputOperatorActionListener implements ActionListener {
		private Operator operator;

		public InputOperatorActionListener(Operator operator) {
			this.operator = operator;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				calculator.inputOperator(operator);
			} catch (Exception ex) {
				exceptionHandle(ex);
			}
		}
	}

	private class InputMethodActionListener implements ActionListener {
		private Function function;

		public InputMethodActionListener(Function function) {
			this.function = function;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				calculator.inputFunction(function);
			} catch (Exception ex) {
				exceptionHandle(ex);
			}
		}
	}

	//
	// Calculator Action
	//

	private class InputEntryAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		private Entry entry;

		public InputEntryAction(Entry entry) {
			this.entry = entry;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				calculator.inputEntry(entry);
			} catch (Exception ex) {
				exceptionHandle(ex);
			}
		}
	}

	private class InputOperatorAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		private Operator operator;

		public InputOperatorAction(Operator operator) {
			this.operator = operator;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				calculator.inputOperator(operator);
			} catch (Exception ex) {
				exceptionHandle(ex);
			}
		}
	}
	
	private void exceptionHandle(Exception e) {
		String message = null;
		
		if (e instanceof ArithmeticException) {
			message = MSG_ARITHMETIC_EXCEPTION;
		} else {
			message = e.getMessage();
		}
		
		e.printStackTrace();
		enableSwitchOtherThanClearButton(false);
		txtFormula.setText(message);
	}

	//
	// Switch LookAndFeel
	//

	private JMenuItem createLookAndFeelMenuItem(LookAndFeelInfo lafInfo) {
		String lafName = lafInfo.getName();
		String lafClassName = lafInfo.getClassName();

		JRadioButtonMenuItem lafMenuItem = new JRadioButtonMenuItem();
		lafMenuItem.setHideActionText(true);
		lafMenuItem.setSelected(lafClassName.equals(DEFAULT_LAF_CLASS_NAME));
		lafMenuItem.setAction(new AbstractAction() {
			private static final long serialVersionUID = 1;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					updateLookAndFeel(btnGroupLAF.getSelection().getActionCommand());
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});

		lafMenuItem.setText(lafName);
		lafMenuItem.setActionCommand(lafClassName);

		return lafMenuItem;
	}

	private void updateLookAndFeel(String newLAFClassName) throws Exception {
		String current = UIManager.getLookAndFeel().getClass().getName();
		if (current.equals(newLAFClassName))
			return;

		UIManager.setLookAndFeel(newLAFClassName);
		SwingUtilities.updateComponentTreeUI(frame);
	}
}
