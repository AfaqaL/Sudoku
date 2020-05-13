package hw3;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.*;


public class SudokuFrame extends JFrame {

	private JTextArea solution;
	private JTextArea initial;
	private JButton checkBtn;
	private JCheckBox autocheck;
	private Sudoku sudoku;

	public SudokuFrame() {
		super("Sudoku Solver");

		// YOUR CODE HERE

		// Could do this:
		// setLocationByPlatform(true);
		JPanel mainPanel = new JPanel();
		BorderLayout border = new BorderLayout(4,4);
		mainPanel.setLayout(border);

		initial = new JTextArea(15, 20);
		initial.setBorder(new TitledBorder("Puzzle"));
		mainPanel.add(initial, BorderLayout.CENTER);

		solution = new JTextArea(15, 20);
		solution.setBorder(new TitledBorder("Solution"));
		mainPanel.add(solution, BorderLayout.EAST);

		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BorderLayout());

		checkBtn = new JButton("Check");
		lowerPanel.add(checkBtn, BorderLayout.WEST);

		autocheck = new JCheckBox("Auto Check", true);
		lowerPanel.add(autocheck);

		addListeners();
		mainPanel.add(lowerPanel, BorderLayout.SOUTH);
		super.add(mainPanel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void updateSol(){
		int[][] grid;
		try{
			grid = Sudoku.textToGrid(initial.getText());
		}catch (RuntimeException e){
			solution.setText("Parsing problem\n");
			return;
		}
		sudoku = new Sudoku(grid);
		int nsols = sudoku.solve();
		solution.setText(sudoku.getSolutionText() + "\n");
		solution.append("solutions:" + nsols + "\n");
		solution.append("elapsed:" + sudoku.getElapsed() + "ms\n");
	}
	private void addListeners() {
		checkBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateSol();
			}
		});

		Document puzzledoc = initial.getDocument();
		puzzledoc.addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(autocheck.isSelected()){
					updateSol();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if(autocheck.isSelected()){
					updateSol();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if(autocheck.isSelected()){
					updateSol();
				}
			}
		});
	}


	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }

		SudokuFrame frame = new SudokuFrame();
	}

}
